package org.kie.dmn.backend.marshalling;

import com.thoughtworks.xstream.XStream;

/**
 * Created by akoufoudakis on 21/03/17.
 */
public interface DMNExtensionElementRegister {

    public void registerExtensionConverters(XStream xstream);

}
