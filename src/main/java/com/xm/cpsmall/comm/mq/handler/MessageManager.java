package com.xm.cpsmall.comm.mq.handler;

import com.xm.cpsmall.comm.mq.message.AbsUserActionMessage;

import java.util.*;

/**
 * 消息管理器
 */
public class MessageManager {

    private Map<Class,List<MessageHandler>> handlers = Collections.synchronizedMap(new HashMap<>());

    public synchronized void add(MessageHandler handler){
        handler.getType().stream().forEach(o->{
            List<MessageHandler> handlerList = getHandlersByType(o);
            handlerList.add(handler);
            handlers.put(o,handlerList);
        });
    }

    private List<MessageHandler> getHandlersByType(Class clz){
        List<MessageHandler> list = handlers.get(clz);
        if(list != null)
            return list;
        return Collections.synchronizedList(new ArrayList<>());
    }
    
    public void handleMessage(AbsUserActionMessage message){
        List<MessageHandler> handlerList = handlers.get(message.getClass());
        if(handlerList == null)
            return;
        handlerList.stream().forEach(o->{
            try {
                o.handle(message);
            }catch (Exception e){
                e.printStackTrace();
                o.onError(e);
            }
        });
    }
}
