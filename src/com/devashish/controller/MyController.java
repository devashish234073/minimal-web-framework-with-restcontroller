package com.devashish.controller;

import java.util.Map;

import com.devashish.framework.annotations.GetMapping;
import com.devashish.framework.annotations.RestController;

@RestController
public class MyController {
    @GetMapping("/hello1")
    public String sayHello1() {
        return "<h1>name is ${name} and id is ${id}</h1>, <input type='text'>";
    }
    
    @GetMapping("/hello2")
    public String sayHello2() {
    	return "Hello2, <input type='text'><input type='button' value='click it'>";
    }
}
