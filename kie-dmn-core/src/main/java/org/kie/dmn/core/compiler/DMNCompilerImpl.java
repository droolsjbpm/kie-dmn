/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.kie.dmn.core.compiler;

import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNCompiler;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.ast.BusinessKnowledgeModelNode;
import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.api.core.ast.DMNNode;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.api.core.ast.ItemDefNode;
import org.kie.dmn.backend.marshalling.v1_1.DMNMarshallerFactory;
import org.kie.dmn.core.ast.*;
import org.kie.dmn.core.impl.BaseDMNTypeImpl;
import org.kie.dmn.core.impl.CompositeTypeImpl;
import org.kie.dmn.core.impl.DMNModelImpl;
import org.kie.dmn.core.impl.FeelTypeImpl;
import org.kie.dmn.feel.lang.Type;
import org.kie.dmn.feel.lang.types.BuiltInType;
import org.kie.dmn.feel.model.v1_1.*;
import org.kie.dmn.feel.parser.feel11.FEELParser;
import org.kie.dmn.feel.runtime.UnaryTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DMNCompilerImpl
        implements DMNCompiler {

    private static final Logger logger = LoggerFactory.getLogger( DMNCompilerImpl.class );
    private final DMNEvaluatorCompiler evaluatorCompiler;
    private final DMNFEELHelper feel;

    public DMNCompilerImpl() {
        this.feel = new DMNFEELHelper();
        this.evaluatorCompiler = new DMNEvaluatorCompiler( this, feel );
    }

    @Override
    public DMNModel compile(Resource resource) {
        try {
            return compile( resource.getReader() );
        } catch ( IOException e ) {
            logger.error( "Error retrieving reader for resource: " + resource.getSourcePath(), e );
        }
        return null;
    }

    @Override
    public DMNModel compile(Reader source) {
        try {
            Definitions dmndefs = DMNMarshallerFactory.newDefaultMarshaller().unmarshal( source );
            if ( dmndefs != null ) {
                DMNModelImpl model = new DMNModelImpl( dmndefs );
                DMNCompilerContext ctx = new DMNCompilerContext();

                processItemDefinitions( ctx, feel, model, dmndefs );
                processDrgElements( ctx, feel, model, dmndefs );
                return model;
            }
        } catch ( Exception e ) {
            logger.error( "Error compiling model from source.", e );
        }
        return null;
    }

    private void processItemDefinitions(DMNCompilerContext ctx, DMNFEELHelper feel, DMNModelImpl model, Definitions dmndefs) {
        for ( ItemDefinition id : dmndefs.getItemDefinition() ) {
            ItemDefNodeImpl idn = new ItemDefNodeImpl( id );
            DMNType type = buildTypeDef( ctx, feel, model, idn, id );
            idn.setType( type );
            model.addItemDefinition( idn );
        }
    }

    private void processDrgElements(DMNCompilerContext ctx, DMNFEELHelper feel, DMNModelImpl model, Definitions dmndefs) {
        for ( DRGElement e : dmndefs.getDrgElement() ) {
            if ( e instanceof InputData ) {
                InputData input = (InputData) e;
                String variableName = input.getVariable() != null ? input.getVariable().getName() : null;
                if ( !variableNameIsValid( variableName ) ) {
                    logger.error( "Invalid variable name '" + variableName + "' in input data '" + input.getId() + "'" );
                    model.addMessage( DMNMessage.Severity.ERROR, "Invalid variable name '" + variableName + "' in input data '" + input.getId() + "'", input.getId() );
                }
                InputDataNodeImpl idn = new InputDataNodeImpl( input );
                DMNType type = resolveTypeRef( model, idn, e, input.getVariable(), input.getVariable().getTypeRef() );
                idn.setType( type );
                model.addInput( idn );
                model.getTypeRegistry().put( input.getVariable().getTypeRef(), type );
            } else if ( e instanceof Decision ) {
                Decision decision = (Decision) e;
                DecisionNodeImpl dn = new DecisionNodeImpl( decision );
                DMNType type = null;
                if ( decision.getVariable() != null && decision.getVariable().getTypeRef() != null ) {
                    type = resolveTypeRef( model, dn, decision, decision.getVariable(), decision.getVariable().getTypeRef() );
                } else {
                    type = resolveTypeRef( model, dn, decision, decision, null );
                }
                dn.setResultType( type );
                model.addDecision( dn );
            } else if ( e instanceof BusinessKnowledgeModel ) {
                BusinessKnowledgeModel bkm = (BusinessKnowledgeModel) e;
                BusinessKnowledgeModelNodeImpl bkmn = new BusinessKnowledgeModelNodeImpl( bkm );
                DMNType type = null;
                if ( bkm.getVariable() != null && bkm.getVariable().getTypeRef() != null ) {
                    type = resolveTypeRef( model, bkmn, bkm, bkm.getVariable(), bkm.getVariable().getTypeRef() );
                } else {
                    // TODO: need to handle cases where the variable is not defined or does not have a type;
                    // for now the call bellow will return type UNKNOWN
                    type = resolveTypeRef( model, bkmn, bkm, bkm, null );
                }
                bkmn.setResultType( type );
                model.addBusinessKnowledgeModel( bkmn );
            } else if ( e instanceof KnowledgeSource ) {
                // don't do anything as KnowledgeSource is a documentation element
                // without runtime semantics
            } else {
                model.addMessage( DMNMessage.Severity.ERROR, "Element " + e.getClass().getSimpleName() + " with id='" + e.getId() + "' not supported.", e.getId() );
            }
        }

        for ( BusinessKnowledgeModelNode bkm : model.getBusinessKnowledgeModels() ) {
            BusinessKnowledgeModelNodeImpl bkmi = (BusinessKnowledgeModelNodeImpl) bkm;
            linkRequirements( model, bkmi );
            FunctionDefinition funcDef = bkm.getBusinessKnowledModel().getEncapsulatedLogic();
            DMNExpressionEvaluator exprEvaluator = evaluatorCompiler.compileExpression( ctx, model, bkmi, bkm.getName(), funcDef );
            bkmi.setEvaluator( exprEvaluator );
        }
        for ( DecisionNode d : model.getDecisions() ) {
            DecisionNodeImpl di = (DecisionNodeImpl) d;
            linkRequirements( model, di );

            ctx.enterFrame();
            try {
                for( DMNNode dep : di.getDependencies().values() ) {
                    if( dep instanceof DecisionNode ) {
                        ctx.setVariable( dep.getName(), ((DecisionNode) dep).getResultType() );
                    } else if( dep instanceof InputDataNode ) {
                        ctx.setVariable( dep.getName(), ((InputDataNode) dep).getType() );
                    } else if( dep instanceof BusinessKnowledgeModelNode ) {
                        // might need to create a DMNType for "functions" and replace the type here by that
                        ctx.setVariable( dep.getName(), ((BusinessKnowledgeModelNode)dep).getResultType() );
                    }
                }
                DMNExpressionEvaluator evaluator = evaluatorCompiler.compileExpression( ctx, model, di, d.getName(), d.getDecision().getExpression() );
                di.setEvaluator( evaluator );
            } finally {
                ctx.exitFrame();
            }
        }
    }

    private boolean variableNameIsValid(String variableName) {
        return FEELParser.isVariableNameValid( variableName );
    }

    private void linkRequirements(DMNModelImpl model, DMNBaseNode node) {
        for ( InformationRequirement ir : node.getInformationRequirement() ) {
            if ( ir.getRequiredInput() != null ) {
                String id = getId( ir.getRequiredInput() );
                InputDataNode input = model.getInputById( id );
                if ( input != null ) {
                    node.addDependency( input.getName(), input );
                } else {
                    String message = "Required input '" + id + "' not found for node '" + node.getName() + "'";
                    logger.error( message );
                    model.addMessage( DMNMessage.Severity.ERROR, message, node.getId() );
                }
            } else if ( ir.getRequiredDecision() != null ) {
                String id = getId( ir.getRequiredDecision() );
                DecisionNode dn = model.getDecisionById( id );
                if ( dn != null ) {
                    node.addDependency( dn.getName(), dn );
                } else {
                    String message = "Required decision '" + id + "' not found for node '" + node.getName() + "'";
                    logger.error( message );
                    model.addMessage( DMNMessage.Severity.ERROR, message, node.getId() );
                }
            }
        }
        for ( KnowledgeRequirement kr : node.getKnowledgeRequirement() ) {
            if ( kr.getRequiredKnowledge() != null ) {
                String id = getId( kr.getRequiredKnowledge() );
                BusinessKnowledgeModelNode bkmn = model.getBusinessKnowledgeModelById( id );
                if ( bkmn != null ) {
                    node.addDependency( bkmn.getName(), bkmn );
                } else {
                    String message = "Required Business Knowledge Model '" + id + "' not found for node '" + node.getName() + "'";
                    logger.error( message );
                    model.addMessage( DMNMessage.Severity.ERROR, message, node.getId() );
                }
            }
        }
    }

    private String getId(DMNElementReference er) {
        String href = er.getHref();
        return href.contains( "#" ) ? href.substring( href.indexOf( '#' ) + 1 ) : href;
    }

    private DMNType buildTypeDef(DMNCompilerContext ctx, DMNFEELHelper feel, DMNModelImpl dmnModel, DMNNode node, ItemDefinition itemDef) {
        BaseDMNTypeImpl type = null;
        if ( itemDef.getTypeRef() != null ) {
            // this is a reference to an existing type, so resolve the reference
            type = (BaseDMNTypeImpl) resolveTypeRef( dmnModel, node, itemDef, itemDef, itemDef.getTypeRef() );
            if ( type != null ) {
                // we have to clone this type definition into a new one
                type = type.clone();

                UnaryTests allowedValuesStr = itemDef.getAllowedValues();
                if ( allowedValuesStr != null ) {
                    List<UnaryTest> av = feel.evaluateUnaryTests(
                            ctx,
                            allowedValuesStr.getText(),
                            dmnModel,
                            itemDef,
                            evaluatorCompiler.createErrorMsg( node, itemDef.getName(), itemDef, 0, allowedValuesStr.getText() )
                    );
                    type.setAllowedValues( av );
                }
                if ( itemDef.isIsCollection() ) {
                    type.setCollection( itemDef.isIsCollection() );
                }
            } else {
                String message = "Unknown type reference '" + itemDef.getTypeRef() + "' on node '" + node.getName() + "'";
                logger.error( message );
                dmnModel.addMessage( DMNMessage.Severity.ERROR, message, node.getId() );
            }
        } else {
            // this is a composite type
            CompositeTypeImpl compType = new CompositeTypeImpl( itemDef.getName(), itemDef.getId(), itemDef.isIsCollection() );
            for ( ItemDefinition fieldDef : itemDef.getItemComponent() ) {
                DMNType fieldType = buildTypeDef( ctx, feel, dmnModel, node, fieldDef );
                compType.getFields().put( fieldDef.getName(), fieldType );
            }
            type = compType;
        }
        return type;
    }

    public DMNType resolveTypeRef(DMNModelImpl dmnModel, DMNNode node, NamedElement model, DMNModelInstrumentedBase localElement, QName typeRef) {
        if ( typeRef != null ) {
            String prefix = typeRef.getPrefix();
            String namespace = localElement.getNamespaceURI( prefix );
            if ( namespace != null && DMNModelInstrumentedBase.URI_FEEL.equals( namespace ) ) {
                Type feelType = BuiltInType.determineTypeFromName( typeRef.getLocalPart() );
                if ( feelType == BuiltInType.CONTEXT || feelType == BuiltInType.UNKNOWN ) {
                    if ( model instanceof Decision && ((Decision) model).getExpression() instanceof DecisionTable ) {
                        DecisionTable dt = (DecisionTable) ((Decision) model).getExpression();
                        if ( dt.getOutput().size() > 1 ) {
                            CompositeTypeImpl compType = new CompositeTypeImpl( "__t" + model.getName(), model.getId() );
                            for ( OutputClause oc : dt.getOutput() ) {
                                DMNType fieldType = resolveTypeRef( dmnModel, node, model, oc, oc.getTypeRef() );
                                compType.getFields().put( oc.getName(), fieldType );
                            }
                            return compType;
                        } else if ( dt.getOutput().size() == 1 ) {
                            return resolveTypeRef( dmnModel, node, model, dt.getOutput().get( 0 ), dt.getOutput().get( 0 ).getTypeRef() );
                        }
                    }
                }
                return new FeelTypeImpl( model.getName(), model.getId(), feelType, false, null );
            } else if ( dmnModel.getNamespace() != null && namespace != null && dmnModel.getNamespace().equals( namespace ) ) {
                // locally defined type
                List<ItemDefNode> itemDefs = dmnModel.getItemDefinitions().stream()
                        .filter( id -> id.getName() != null && id.getName().equals( typeRef.getLocalPart() ) )
                        .collect( toList() );
                if ( itemDefs.size() == 1 ) {
                    return itemDefs.get( 0 ).getType();
                } else if ( itemDefs.isEmpty() ) {
                    if ( model.getName() != null && node.getName() != null && model.getName().equals( node.getName() ) ) {
                        logger.error( "No '" + typeRef.toString() + "' type definition found for node '" + node.getName() + "'" );
                    } else {
                        logger.error( "No '" + typeRef.toString() + "' type definition found for element '" + model.getName() + "' on node '" + node.getName() + "'" );
                    }
                } else {
                    if ( model.getName() != null && node.getName() != null && model.getName().equals( node.getName() ) ) {
                        logger.error( "Multiple types found for type reference '" + typeRef.toString() + "' on node '" + node.getName() + "'" );
                    } else {
                        logger.error( "Multiple types found for type reference '" + typeRef.toString() + "' on element '" + model.getName() + "' on node '" + node.getName() + "'" );
                    }
                }
            } else {
                if ( model.getName() != null && node.getName() != null && model.getName().equals( node.getName() ) ) {
                    logger.error( "Unknown namespace for type reference prefix '" + prefix + "' on node '" + node.getName() + "'" );
                } else {
                    logger.error( "Unknown namespace for type reference prefix '" + prefix + "' on element '" + model.getName() + "' on node '" + node.getName() + "'" );
                }
            }
            return null;
        }
        return new FeelTypeImpl( model.getName(), model.getId(), BuiltInType.UNKNOWN, false, null );
    }



}