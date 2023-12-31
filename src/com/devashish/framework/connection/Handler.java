package com.devashish.framework.connection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.devashish.framework.annotations.RequestParam;

public class Handler {
    private final Object controller;
    private final java.lang.reflect.Method method;

    Handler(Object controller, java.lang.reflect.Method method) {
        this.controller = controller;
        this.method = method;
    }

    String handle(Map<String,String> queryParamsMap) {
        try {
        	Object[] args = null;
        	if(method.getParameterCount()>0) {
        		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            	args = new Object[method.getParameterCount()];
            	for(int i=0;i<method.getParameters().length;i++) {
            		Parameter parameter = method.getParameters()[i];
            		RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            		if(annotation!=null) {
            			String annotationName = annotation.value();
            			String annotationValue = queryParamsMap.get(annotationName);
            			args[i] = annotationValue;
            		} else {
            			args[i] = null;
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
}
