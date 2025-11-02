package com.m01project.taskmanager.demo.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloWorldServiceTest {

    @Test
    void testGetHelloMessage() {
        HelloWorldService service = new HelloWorldService();
        String message = service.sayHello();
        assertEquals("Hello World!", message);
    }
}

