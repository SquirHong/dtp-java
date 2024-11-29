package io.dynamic.threadpool.common.monitor;

import io.dynamic.threadpool.common.monitor.AbstractMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageWrapper extends AbstractMessage implements Serializable {

    /**
     * messageObj
     */
    private Object messageObj;

}
