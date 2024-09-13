package io.dynamic.threadpool.common.toolkit;

public class UserContext {

    private static String username;

    public static void setUserName(String username) {
        UserContext.username = username;
    }

    public static String getUserName() {
        return username;
    }

}
