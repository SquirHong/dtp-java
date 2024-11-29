package io.dynamic.threadpool.starter.monitor;


import io.dynamic.threadpool.common.monitor.Message;


public interface MessageSender {


    void send(Message message);

}
