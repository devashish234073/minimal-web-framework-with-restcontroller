package com.devashish.controller;

import com.devashish.rawspringboot.annotations.GetMapping;
import com.devashish.rawspringboot.annotations.RestController;

@RestController
public class MyController {
    @GetMapping("/hello1")
    public String sayHello1() {
        return "Hello, <input type='text'>";
    }
    
    @GetMapping("/hello2")
    public String sayHello2() {
    	return "Hello2, <input type='text'><input type='button' value='click it'>";
    }
}
