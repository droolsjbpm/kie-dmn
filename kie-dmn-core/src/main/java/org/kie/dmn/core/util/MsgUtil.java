package org.kie.dmn.core.util;

import org.kie.dmn.core.impl.DMNMessageTypeImpl;
import org.kie.dmn.core.util.Msg.Message;
import org.kie.dmn.core.util.Msg.Message0;
import org.kie.dmn.core.util.Msg.Message1;
import org.kie.dmn.core.util.Msg.Message2;
import org.kie.dmn.core.util.Msg.Message3;
import org.kie.dmn.core.util.Msg.Message4;

public class MsgUtil {
    public static DMNMessageTypeImpl createMessage( Message0 message) {
        return MsgUtil.buildMessage(message);
    }
    public static DMNMessageTypeImpl createMessage( Message1 message, Object p1) {
        return MsgUtil.buildMessage(message, p1);
    }
    public static DMNMessageTypeImpl createMessage( Message2 message, Object p1, Object p2) {
        return MsgUtil.buildMessage(message, p1, p2);
    }
    public static DMNMessageTypeImpl createMessage( Message3 message, Object p1, Object p2, Object p3) {
        return MsgUtil.buildMessage(message, p1, p2, p3);
    }
    public static DMNMessageTypeImpl createMessage( Message4 message, Object p1, Object p2, Object p3, Object p4) {
        return MsgUtil.buildMessage(message, p1, p2, p3, p4);
    }
    
    private static DMNMessageTypeImpl buildMessage( Message message, Object... params ) {
        return new DMNMessageTypeImpl(String.format( message.getMask(), params ), message.getId());
    }
}
