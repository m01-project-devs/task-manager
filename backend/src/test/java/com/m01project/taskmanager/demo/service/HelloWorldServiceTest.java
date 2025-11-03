package com.m01project.taskmanager.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HelloWorldServiceTest {

    @Test
    void testGetHelloMessage() {
        HelloWorldService service = new HelloWorldService();
        String message = service.sayHello();
        assertEquals("Hello World!", message);
    }

    @Test
    void sayHelloTo() {
        //...
    }

    @Test
    void getCurrentTime() {
        //...
    }
}

