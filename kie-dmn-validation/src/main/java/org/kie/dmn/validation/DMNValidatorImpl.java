/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.validation;

import org.drools.core.util.Drools;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.dmn.api.core.DMNCompiler;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.backend.marshalling.v1_1.DMNMarshallerFactory;
import org.kie.dmn.core.compiler.DMNCompilerImpl;
import org.kie.dmn.core.impl.DMNMessageImpl;
import org.kie.dmn.core.util.KieHelper;
import org.kie.dmn.core.util.MsgUtil;
import org.kie.dmn.model.v1_1.DMNModelInstrumentedBase;
import org.kie.dmn.model.v1_1.Definitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_COMPILATION;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_MODEL;
import static org.kie.dmn.validation.DMNValidator.Validation.VALIDATE_SCHEMA;

public class DMNValidatorImpl implements DMNValidator {
    public static Logger LOG = LoggerFactory.getLogger(DMNValidatorImpl.class);
    static Schema schema;
    static {
        try {
            schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * A KieContainer is normally available,
     * unless at runtime some problem prevented building it correctly.
     */
    private Optional<KieContainer> kieContainer;

    /**
     * Collect at init time the runtime issues which prevented to build the `kieContainer` correctly.
     */
    private List<DMNMessage> failedInitMsg;

    public DMNValidatorImpl() {
        final KieServices ks = KieServices.Factory.get();
        final KieContainer kieContainer = KieHelper.getKieContainer(
                ks.newReleaseId( "org.kie", "kie-dmn-validation", Drools.getFullVersion() ),
                ks.getResources().newClassPathResource("dmn-validation-rules.drl", getClass() ) );
        if( kieContainer != null ) {
            this.kieContainer = Optional.of( kieContainer );
        } else {
            this.kieContainer = Optional.empty();
            LOG.error("Unable to load embedded DMN validation rules file." );
            String message = MsgUtil.createMessage( Msg.FAILED_VALIDATOR );
            failedInitMsg.add(new DMNMessageImpl(DMNMessage.Severity.ERROR, message, Msg.FAILED_VALIDATOR.getType(), null ) );
        }
    }
    
    public void dispose() {
        kieContainer.ifPresent( KieContainer::dispose );
    }

    @Override
    public List<DMNMessage> validate(Definitions dmnModel) {
        return validate( dmnModel, VALIDATE_MODEL );
    }

    @Override
    public List<DMNMessage> validate(Definitions dmnModel, Validation... options) {
        List<DMNMessage> results = new ArrayList<>(  );
        EnumSet<Validation> flags = EnumSet.copyOf( Arrays.asList( options ) );
        if( flags.contains( VALIDATE_SCHEMA ) ) {
            throw new IllegalArgumentException( "Schema validation not supported for in memory object. Please use the validate method with the file or reader signature." );
        }
        validateModelCompilation( dmnModel, results, flags );
        return results;
    }

    @Override
    public List<DMNMessage> validate(File xmlFile) {
        return validate( xmlFile, VALIDATE_MODEL );
    }

    @Override
    public List<DMNMessage> validate(File xmlFile, Validation... options) {
        List<DMNMessage> results = new ArrayList<>(  );
        EnumSet<Validation> flags = EnumSet.copyOf( Arrays.asList( options ) );
        if( flags.contains( VALIDATE_SCHEMA ) ) {
            results.addAll( validateSchema( xmlFile ) );
        }
        if( flags.contains( VALIDATE_MODEL ) || flags.contains( VALIDATE_COMPILATION ) ) {
            Definitions dmndefs = null;
            try {
                dmndefs = DMNMarshallerFactory.newDefaultMarshaller().unmarshal( new FileReader( xmlFile ) );
                validateModelCompilation( dmndefs, results, flags );
            } catch ( FileNotFoundException e ) {
                LOG.error( "Error reading file {}. Unable to validate it.", xmlFile.getAbsolutePath(), e );
                throw new IllegalArgumentException( "Error reading file "+xmlFile.getAbsolutePath(), e );
            }
        }
        logDebugMessages( results );
        return results;
    }

    @Override
    public List<DMNMessage> validate(Reader reader) {
        return validate( reader, VALIDATE_MODEL );
    }

    @Override
    public List<DMNMessage> validate(Reader reader, Validation... options) {
        List<DMNMessage> results = new ArrayList<>(  );
        EnumSet<Validation> flags = EnumSet.copyOf( Arrays.asList( options ) );
        if( flags.contains( VALIDATE_SCHEMA ) ) {
            results.addAll( validateSchema( reader ) );
        }
        if( flags.contains( VALIDATE_MODEL ) || flags.contains( VALIDATE_COMPILATION ) ) {
            Definitions dmndefs = DMNMarshallerFactory.newDefaultMarshaller().unmarshal( reader );
            validateModelCompilation( dmndefs, results, flags );
        }
        logDebugMessages( results );
        return results;
    }

    private void validateModelCompilation(Definitions dmnModel, List<DMNMessage> results, EnumSet<Validation> flags) {
        if( flags.contains( VALIDATE_MODEL ) ) {
            results.addAll( validateModel( dmnModel ) );
        }
        if( flags.contains( VALIDATE_COMPILATION ) ) {
            results.addAll( validateCompilation( dmnModel ) );
        }
    }

    private List<DMNMessage> validateSchema(File xmlFile) {
        Source s = new StreamSource(xmlFile);
        return validateSchema( s );
    }

    private List<DMNMessage> validateSchema(Reader reader) {
        Source s = new StreamSource(reader);
        return validateSchema( s );
    }

    private List<DMNMessage> validateSchema(Source s) {
        List<DMNMessage> problems = new ArrayList<>();
        try {
            schema.newValidator().validate(s);
        } catch (SAXException | IOException e) {
            problems.add(new DMNMessageImpl( DMNMessage.Severity.ERROR, MsgUtil.createMessage( Msg.FAILED_XML_VALIDATION), Msg.FAILED_VALIDATOR.getType(), null, e));
            logDebugMessages( problems );
        }
        // TODO detect if the XSD is not provided through schemaLocation, and validate against embedded
        return problems;
    }

    private List<DMNMessage> validateModel(Definitions dmnModel) {
        if (!kieContainer.isPresent()) {
            return failedInitMsg;
        }
        
        StatelessKieSession kieSession = kieContainer.get().newStatelessKieSession();
        MessageReporter reporter = new MessageReporter();
        kieSession.setGlobal( "reporter", reporter );
        
        kieSession.execute(allChildren(dmnModel).collect(toList()));

        return reporter.getMessages();
    }

    private List<DMNMessage> validateCompilation(Definitions dmnModel) {
        if( dmnModel != null ) {
            DMNCompiler compiler = new DMNCompilerImpl();
            DMNModel model = compiler.compile( dmnModel );
            if( model != null ) {
                return model.getMessages();
            } else {
                LOG.error( "Compilation failed for model {}. Unable to validate compilation.", dmnModel.getName() );
            }
        }
        return Collections.emptyList();
    }

    private static Stream<DMNModelInstrumentedBase> allChildren(DMNModelInstrumentedBase root) {
        return Stream.concat( Stream.of(root),
                              root.getChildren().stream().flatMap(DMNValidatorImpl::allChildren) );
    }

    private void logDebugMessages(List<DMNMessage> messages) {
        if ( LOG.isDebugEnabled() ) {
            for ( DMNMessage m : messages ) {
                LOG.debug("{}", m);
            }
        }
    }
}
