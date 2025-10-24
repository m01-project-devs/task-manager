package com.taskmanager.demo.service;

public class HelloWorldService
{
    public String sayHello(String name) {

        if(name == null || name.trim().isEmpty())
        {
            return "Hello World!";
        };
        return "Hello " + name;
    };
};
