package com.devashish.framework.connection;

import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Map;

import com.devashish.framework.annotations.RequestParam;
import com.devashish.framework.annotations.ResponseType.TYPE;
import com.devashish.framework.injectable.HttpHeader;
import com.devashish.framework.annotations.RequestBody;

public class Handler {
    private final Object controller;
    private final java.lang.reflect.Method method;
    private final TYPE respType;

    Handler(Object controller, java.lang.reflect.Method method, TYPE respType) {
        this.controller = controller;
        this.method = method;
        this.respType = respType;
    }

    String handle(Map<String,String> queryParamsMap,Map<String,String> headers,String body) {
        try {
        	Object[] args = null;
        	if(method.getParameterCount()>0) {
            	args = new Object[method.getParameterCount()];
            	for(int i=0;i<method.getParameters().length;i++) {
            		Parameter parameter = method.getParameters()[i];
            		RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            		if(annotation!=null) {
            			String annotationName = annotation.value();
            			String annotationValue = queryParamsMap.get(annotationName);
            			args[i] = annotationValue;
            		} else {
            			if(parameter.getType().isAssignableFrom(HttpHeader.class)) {
            				args[i] = new HttpHeader(headers);
            			} else if(parameter.getAnnotation(RequestBody.class)!=null) {
            				RequestBody requestBodyAnnotation = parameter.getAnnotation(RequestBody.class);
            				if(parameter.getType().isAssignableFrom(String.class)) {
            					args[i] = body;
            				} else {
            					if(requestBodyAnnotation.value()==TYPE.JSON) {
            						args[i] = parseJson(body,parameter.getType());	
            					} else if(requestBodyAnnotation.value()==TYPE.XML) {
            						args[i] = parseXml(body,parameter.getType());
            					} else {
            						args[i] = null;
            					}
            				}
            			} else {
            				args[i] = null;
            			}
            		}
            	}	
        	}
        	if(args==null) {
        		return (String) method.invoke(controller);
        	} else {
        		return (String) method.invoke(controller,args);
        	}
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

	private Object parseXml(String body, Class<?> type) {
		try {
			Object obj = type.getDeclaredConstructor().newInstance();
			return obj;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Object parseJson(String body, Class<?> type) {
		try {
			Object obj = type.getDeclaredConstructor().newInstance();
			return obj;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public TYPE getRespType() {
		return respType;
	}
}
