package org.kie.dmn.feel.lang;

import java.util.Map;

public interface CompositeType
        extends Type {

    Map<String, Type> getFields();

}
