package com.devashish.framework.connection;

public class Handler {
    private final Object controller;
    private final java.lang.reflect.Method method;

    Handler(Object controller, java.lang.reflect.Method method) {
        this.controller = controller;
        this.method = method;
    }

    String handle() {
        try {
            return (String) method.invoke(controller);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
