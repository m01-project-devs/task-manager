package com.m01project.taskmanager.service;

@org.springframework.stereotype.Service
public class Service {

    public String sayHelloTo(String name) {
        return "Hello, " + name + "!";
    }
}
