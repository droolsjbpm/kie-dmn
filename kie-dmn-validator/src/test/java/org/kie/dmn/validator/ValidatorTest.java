package org.kie.dmn.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.kie.dmn.core.DMNInputRuntimeTest;
import org.kie.dmn.core.api.DMNModel;
import org.kie.dmn.core.api.DMNRuntime;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.kie.dmn.feel.model.v1_1.Definitions;
import org.xml.sax.SAXException;

public class ValidatorTest {

    @Test
    public void testDryRun() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "0001-input-data-string.dmn", DMNInputRuntimeTest.class );
        DMNModel dmnModel = runtime.getModel( "https://github.com/droolsjbpm/kie-dmn", "0001-input-data-string" );
        assertThat( dmnModel, notNullValue() );

        Definitions definitions = dmnModel.getDefinitions();
        assertThat( definitions, notNullValue() );
        
        Validator.newInstance().validate(definitions);
    }
    
    private Definitions utilDefinitions(String filename, String modelName) {
        List<Problem> validateXML;
        try {
            validateXML = Validator.newInstance().validateXML( new File(this.getClass().getResource(filename).toURI()) );
            assertThat( "using unit test method utilDefinitions must received a XML valid DMN file", validateXML, IsEmptyCollection.empty() );
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail("Unable for the test suite to locate the file for XML validation.");
        }
        
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( filename, this.getClass() );
        DMNModel dmnModel = runtime.getModel( "https://github.com/droolsjbpm/kie-dmn", modelName );
        assertThat( dmnModel, notNullValue() );

        Definitions definitions = dmnModel.getDefinitions();
        assertThat( definitions, notNullValue() );
        return definitions;
    }
        
    @Test
    public void testInvalidXml() throws URISyntaxException {
        List<Problem> validateXML = Validator.newInstance().validateXML(new File(this.getClass().getResource( "invalidXml.dmn" ).toURI()));
        assertTrue( !validateXML.isEmpty() );
        
        validateXML.forEach(System.err::println);
    }

    @Test
    public void testITEMDEF_NOT_UNIQUE() {
        Definitions definitions = utilDefinitions( "ITEMDEF_NOT_UNIQUE.dmn", "ITEMDEF_NOT_UNIQUE" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.ITEMDEF_NOT_UNIQUE ) ) );
    }
    
    @Test
    public void testITEMCOMP_DUPLICATED() {
        Definitions definitions = utilDefinitions( "ITEMCOMP_DUPLICATED.dmn", "ITEMCOMP_DUPLICATED" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.ITEMCOMP_DUPLICATED ) ) );
    }
    
    @Test
    public void testDECISION_MISSING_VAR() {
        Definitions definitions = utilDefinitions( "DECISION_MISSING_VAR.dmn", "DECISION_MISSING_VAR" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.DECISION_MISSING_VAR ) ) );
    }
    
    @Test
    public void testDECISION_MISSING_VARbis() {
        Definitions definitions = utilDefinitions( "DECISION_MISSING_VARbis.dmn", "DECISION_MISSING_VARbis" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.DECISION_MISSING_VAR ) ) );
    }
    
    @Test
    public void testDECISION_MISSING_EXPR() {
        Definitions definitions = utilDefinitions( "DECISION_MISSING_EXPR.dmn", "DECISION_MISSING_EXPR" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.DECISION_MISSING_EXPR ) ) );
    }
    
    @Test
    public void testBKM_MISSING_VAR() {
        Definitions definitions = utilDefinitions( "BKM_MISSING_VAR.dmn", "BKM_MISSING_VAR" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.BKM_MISSING_VAR ) ) );
    }
    
    @Test
    public void testBKM_MISSING_EXPR() {
        Definitions definitions = utilDefinitions( "BKM_MISSING_EXPR.dmn", "BKM_MISSING_EXPR" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.BKM_MISSING_EXPR ) ) );
    }
    
    @Test
    public void testFORMAL_PARAM_DUPLICATED() {
        Definitions definitions = utilDefinitions( "FORMAL_PARAM_DUPLICATED.dmn", "FORMAL_PARAM_DUPLICATED" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.FORMAL_PARAM_DUPLICATED ) ) );
    }
    
    @Test
    public void testDRGELEM_NOT_UNIQUE() {
        Definitions definitions = utilDefinitions( "DRGELEM_NOT_UNIQUE.dmn", "DRGELEM_NOT_UNIQUE" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.DRGELEM_NOT_UNIQUE ) ) );
    }
    
    @Test
    public void testINPUTDATA_MISSING_VAR() {
        Definitions definitions = utilDefinitions( "INPUTDATA_MISSING_VAR.dmn", "INPUTDATA_MISSING_VAR" );
        List<Problem> validate = Validator.newInstance().validate(definitions);
        
        assertTrue( validate.stream().anyMatch( p -> p.getP().equals( P.INPUTDATA_MISSING_VAR ) ) );
    }
}
