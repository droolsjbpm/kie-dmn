package org.kie.dmn.backend.unmarshalling.v1_1.xstream;

import org.kie.dmn.feel.model.v1_1.ContextEntry;
import org.kie.dmn.feel.model.v1_1.Expression;
import org.kie.dmn.feel.model.v1_1.InformationItem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ContextEntryConverter extends DMNModelInstrumentedBaseConverter {
    @Override
    protected void assignChildElement(Object parent, String nodeName, Object child) {
        super.assignChildElement(parent, nodeName, child);
        ContextEntry ce = (ContextEntry) parent;
        
        if ("variable".equals(nodeName)) {
            ce.setVariable((InformationItem) child);
        } else if ("expression".equals(nodeName)) {
            ce.setExpression((Expression) child);
        } else {
            super.assignChildElement(parent, nodeName, child);
        }
    }

    @Override
    protected void assignAttributes(HierarchicalStreamReader reader, Object parent) {
        super.assignAttributes(reader, parent);
        
        // no attributes.
    }

    @Override
    protected void writeChildren(HierarchicalStreamWriter writer, MarshallingContext context, Object parent) {
        super.writeChildren(writer, context, parent);
        ContextEntry ce = (ContextEntry) parent;
        
        if (ce.getVariable() != null) writeChildrenNode(writer, context, ce.getVariable());
        writeChildrenNode(writer, context, ce.getExpression());
    }

    @Override
    protected void writeAttributes(HierarchicalStreamWriter writer, Object parent) {
        super.writeAttributes(writer, parent);
        
        // no attributes.
    }

    public ContextEntryConverter(XStream xstream) {
        super(xstream);
    }

    @Override
    protected Object createModelObject() {
        return new ContextEntry();
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals( ContextEntry.class );
    }

}
