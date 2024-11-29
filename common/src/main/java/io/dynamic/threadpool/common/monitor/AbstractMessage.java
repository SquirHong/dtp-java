package io.dynamic.threadpool.common.monitor;

import lombok.Data;

import java.util.List;


@Data
public abstract class AbstractMessage implements Message {

    /**
     * groupKey: tenant + item + tpId + identify
     */
    private String groupKey;

    /**
     * messageTypeEnum
     */
    private MessageTypeEnum messageType;

    /**
     * message
     */
    private List<Message> messages;

}
