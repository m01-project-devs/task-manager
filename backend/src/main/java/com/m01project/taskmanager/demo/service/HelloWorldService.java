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
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        return now.format(formatter);
    }

    public String getGreetingWithTime(String name) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = now.format(formatter);
        String guestName = name != null && !name.isBlank() ? name : "Guest";
        return String.format("Hello %s! Current time is %s", guestName, time);
    }
}