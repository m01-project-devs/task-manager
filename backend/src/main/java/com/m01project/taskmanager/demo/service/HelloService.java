package com.m01project.taskmanager.demo.service;

import org.springframework.stereotype.Service;

/**
 * HelloWorldService - A practice service for beginners
 * 
 * This service is designed for learning purposes. 
 * Add your hello world functions here to practice Spring Boot development.
 */
@Service
public class HelloService {

    public String sayHello() {
        return "Hello World!";
    }

    public String sayHelloTo(String name) {
        if(name != null && !name.isBlank()) {
            return "hello " + name.trim();
        }
        else {
            return "hello nobody!";
        }
    }

}