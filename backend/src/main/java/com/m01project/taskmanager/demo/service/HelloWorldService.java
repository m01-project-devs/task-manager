package com.m01project.taskmanager.demo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HelloWorldService - A practice service for beginners
 * 
 * This service is designed for learning purposes. 
 * Add your hello world functions here to practice Spring Boot development.
 */
@Service
public class HelloWorldService {

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

    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        return now.format(formatter);
    }

    package com.taskmanager.demo.service;

    public class HelloWorldService {
        public String sayHello() {
            return "Hello guys!";  // <- i add this for modify text "Test Commit"
        }
    }


    /**
     * Examples you can implement:
     * - public String sayHelloTo(String name)
     * - public String getCurrentTime()
     * - public int addNumbers(int a, int b)
     */
}