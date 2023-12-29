package com.devashish.framework.connection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.devashish.framework.annotations.GetMapping;
import com.devashish.framework.annotations.RestController;

public class HttpServer {
	
	private static final String HTTP_RESP_PREFIX = "HTTP/1.1 200 OK\r\nContent-Length: _XX_\r\nConnection: close\r\nContent-Type: text/html\r\n\r\n";
	private static final java.util.Map<String, Handler> urlMappings = new java.util.HashMap<>();

    public static void scanAndInitializeControllers(String basePackage) {
        try {
            List<Class<?>> controllerClasses = getClasses(basePackage);
            System.out.println(controllerClasses);

            for (Class<?> controllerClass : controllerClasses) {
                if (controllerClass.isAnnotationPresent(RestController.class)) {
                	System.out.println("RestController annotation found in "+controllerClass);
                    Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                    registerController(controllerInstance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        List<Class<?>> classes = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());

            if (directory.exists()) {
                // Get all .class files in the package
                File[] files = directory.listFiles((file, name) -> name.endsWith(".class"));

                if (files != null) {
                    for (File file : files) {
                        String className = packageName + '.' + file.getName().replace(".class", "");
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            }
        }

        return classes;
    }

    private static void registerController(Object controller) {
        for (java.lang.reflect.Method method : controller.getClass().getMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                String mappedUrl = getMapping.value();
                registerHandler(mappedUrl, controller, method);
            }
        }
    }

    private static void registerHandler(String url, Object controller, java.lang.reflect.Method method) {
        urlMappings.put(url, new Handler(controller, method));
    }

    public static String handleRequest(String url) {
        Handler handler = urlMappings.get(url);
        String message = "";
        if (handler != null) {
            message = handler.handle();
        } else {
            message = "404 Not Found: No handler registered for " + url;
        }
        return HTTP_RESP_PREFIX.replace("_XX_", ""+message.length())+message;
    }

}
