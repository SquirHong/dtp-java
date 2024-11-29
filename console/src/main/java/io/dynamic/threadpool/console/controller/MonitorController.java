package io.dynamic.threadpool.console.controller;


import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.monitor.MessageTypeEnum;
import io.dynamic.threadpool.common.monitor.MessageWrapper;
import io.dynamic.threadpool.common.monitor.RuntimeMessage;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.config.toolkit.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(Constants.BASE_PATH + "/monitor")
public class MonitorController {

    @PostMapping
    public Result dataAcquisition(@RequestBody MessageWrapper messageWrapper) {
        if (messageWrapper.getMessageType().name().equals(MessageTypeEnum.RUNTIME.name())) {
            log.info("Receive runtime data uploaded by the client. messageWrapper :: {}", JSON.toJSONString(messageWrapper));
            RuntimeMessage convert = BeanUtil.convert(messageWrapper.getMessageObj(), RuntimeMessage.class);
            // 暂时处理
            log.info("Receive runtime data uploaded by the client. message :: {}", JSON.toJSONString(convert));
        }
        return Results.success();
    }

}
