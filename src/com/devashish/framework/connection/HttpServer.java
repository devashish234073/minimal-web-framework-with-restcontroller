package com.devashish.framework.connection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import com.devashish.framework.annotations.GetMapping;
import com.devashish.framework.annotations.PostMapping;
import com.devashish.framework.annotations.ResponseType;
import com.devashish.framework.annotations.ResponseType.TYPE;
import com.devashish.framework.annotations.RestController;

public class HttpServer {
	
	private static final String HTTP_RESP_PREFIX = "HTTP/1.1 200 OK\r\nContent-Length: _XX_\r\nConnection: close\r\nContent-Type: _YY_\r\n\r\n";
	private static final java.util.Map<String, Handler> urlMappings = new java.util.HashMap<>();
	private static final String POST_MAPPING_PREFIX = "[POST]";
	private static final String GET_MAPPING_PREFIX = "[GET]";

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
            if (method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(PostMapping.class)) {
            	TYPE respType = TYPE.HTML;
            	if (method.isAnnotationPresent(ResponseType.class)) {
            		ResponseType responseType = method.getAnnotation(ResponseType.class);
            		respType = responseType.value();
                }
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if(getMapping!=null) {
                	String mappedUrl = GET_MAPPING_PREFIX+getMapping.value();
                    registerHandler(mappedUrl, controller, method, respType);	
                } else {
                	PostMapping postMapping = method.getAnnotation(PostMapping.class);
                	if(postMapping!=null) {
                    	String mappedUrl = POST_MAPPING_PREFIX+postMapping.value();
                        registerHandler(mappedUrl, controller, method, respType);	
                    }
                }
            }
        }
    }

    private static void registerHandler(String url, Object controller, java.lang.reflect.Method method, TYPE respType) {
        urlMappings.put(url, new Handler(controller, method, respType));
    }

    public static String handleRequest(String url,Map<String,String> queryParamsMap, Map<String,String> headers,String body) {
        Handler handler = urlMappings.get(url);
        String prefix = HTTP_RESP_PREFIX;
        String message = "";
        if (handler != null) {
            message = handler.handle(queryParamsMap,headers,body);
            if(handler.getRespType()==TYPE.HTML) {
            	prefix = prefix.replace("_YY_", "text/html");
            } else if(handler.getRespType()==TYPE.XML) {
            	prefix = prefix.replace("_YY_", "application/xml");
            } else if(handler.getRespType()==TYPE.JSON) {
            	prefix = prefix.replace("_YY_", "application/json");
            }
        } else {
        	prefix = prefix.replace("_YY_", "text/html");
            message = "404 Not Found: No handler registered for " + url;
        }
        if(queryParamsMap!=null && message.indexOf("${")>-1) {
        	for(String key : queryParamsMap.keySet()) {
        		String value = queryParamsMap.get(key);
        		message = message.replace("${"+key+"}", value);
        	}
        }
        return prefix.replace("_XX_", ""+message.length())+message;
    }

}
