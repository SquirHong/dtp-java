package io.dynamic.threadpool.starter.enable;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DynamicThreadPoolMarkerConfiguration.class)
public @interface EnableDynamicThreadPool {

}
