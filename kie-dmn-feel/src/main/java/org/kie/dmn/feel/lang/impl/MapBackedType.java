package org.kie.dmn.feel.lang.impl;

import org.kie.dmn.feel.lang.CompositeType;
import org.kie.dmn.feel.lang.Type;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapBackedType
        implements CompositeType {

    public static final String TYPE_NAME = "__TYPE_NAME__";

    private String            name       = "[anonymous]";
    private Map<String, Type> properties = new LinkedHashMap<>();

    public MapBackedType() {
    }

    /**
     * Utility constructor by reflection over key-value pairs.
     */
    public MapBackedType(String typeName, Map<String, Type> fields) {
        this.name = typeName;
        fields.putAll( fields );
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object fromString(String value) {
        return null;
    }

    @Override
    public String toString(Object value) {
        return null;
    }

    public MapBackedType addField(String name, Type type) {
        properties.put( name, type );
        return this;
    }

    @Override
    public Map<String, Type> getFields() {
        return properties;
    }
}
