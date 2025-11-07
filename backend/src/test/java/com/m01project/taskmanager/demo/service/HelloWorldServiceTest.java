package com.m01project.taskmanager.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HelloWorldServiceTest {

    @InjectMocks
    private HelloWorldService service;

    @Test
    void sayHelloTo() {
        //...
    }

    @Test
    void getCurrentTime() {
        //...
    }

    @Test
    void testSayHello() {
        String result = service.sayHello();
        assertEquals("Hello World!", result);
    }

    @Test
    void testSayHelloTo_withNullValue() {
        String result = service.sayHelloTo(null);
        assertEquals("hello nobody!", result);
    } //boundary test or edge case test

    @Test
    void sayHelloToHamzaTest() {
        String result = service.sayHelloTo("hamza");
        assertEquals("hello hamza", result);
    }
    @Test
    void sayHelloToToylyTest() {
        String result = service.sayHelloTo("Toyly");
        assertEquals("hello Toyly", result);
    }

    @Test
    void sayHelloToAbdullahWithBlanksTest() {
        String result = service.sayHelloTo("   abdullah       ");
        assertEquals("hello abdullah", result);
    }

    @Test
    void sayHelloToBlankTest() {
        String result = service.sayHelloTo("");
        assertEquals("hello nobody!", result);
    }

    @Test
    void sayHelloToNobodyTest() {
        String result = service.sayHelloTo(null);
        assertEquals("hello nobody!", result);
    }

    @Test
    void sayHelloToShermatovTest() {
        String result = service.sayHelloTo("Shermatov");
        assertEquals("hello Shermatov", result);
    }

    @Test
    void sayHelloToShermatovTest2() {
        String result = service.sayHelloTo("Shermatov2");
        assertEquals("hello Shermatov2", result);
    }
}


