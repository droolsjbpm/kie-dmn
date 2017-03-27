package org.kie.dmn.backend.marshalling.v1_1;

import org.junit.Test;
import org.kie.dmn.api.marshalling.v1_1.DMNMarshaller;
import org.kie.dmn.model.v1_1.DMNElement;
import org.kie.dmn.model.v1_1.Definitions;
import org.kie.dmn.model.v1_1.InputData;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertThat;

/**
 * Created by akoufoudakis on 15/03/17.
 */
public class UnmarshalExtensionElementsTest {

    @Test
    public void testExtensionElement() {
        final DMNMarshaller DMNMarshaller = DMNMarshallerFactory.newDefaultMarshaller();
        final InputStream is = this.getClass().getResourceAsStream("dakkapel_uitvoering_en_conversie.xml");
        final InputStreamReader isr = new InputStreamReader( is );
        final Definitions def = DMNMarshaller.unmarshal( isr );

        DMNElement.ExtensionElements idata = def.getExtensionElements();

        assert(idata != null);

    }

}
