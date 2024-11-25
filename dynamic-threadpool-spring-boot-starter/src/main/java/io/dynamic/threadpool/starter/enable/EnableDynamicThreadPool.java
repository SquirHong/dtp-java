package io.dynamic.threadpool.starter.enable;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BeforeCheckConfiguration.class, MarkerConfiguration.class})
public @interface EnableDynamicThreadPool {

}
