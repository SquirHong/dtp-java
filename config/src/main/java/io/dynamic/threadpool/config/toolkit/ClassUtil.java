package io.dynamic.threadpool.config.toolkit;

import java.util.Objects;

/**
 * Class Util.
 */
public class ClassUtil {

    /**
     * 检查是否可以将cls实例赋值给clazz类型的变量
     */
    public static boolean isAssignableFrom(Class clazz, Class cls) {
        Objects.requireNonNull(cls, "cls");
        return clazz.isAssignableFrom(cls);
    }

    /**
     * 获取类的全限定名
     */
    public static String getCanonicalName(Class cls) {
        Objects.requireNonNull(cls, "cls");
        return cls.getCanonicalName();
    }
}
