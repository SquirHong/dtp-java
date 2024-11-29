package io.dynamic.threadpool.common.monitor;


import java.io.Serializable;
import java.util.List;

public interface Message extends Serializable {


    String getGroupKey();

    MessageTypeEnum getMessageType();

    List<Message> getMessages();

}
