package com.m01project.taskmanager;

import com.m01project.taskmanager.demo.service.HelloWorldService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldServiceTest {

    private HelloWorldService service;

    @BeforeEach
    void setUp() {
        service = new HelloWorldService();
    }

    @Test
    void sayHelloTest() {
        String result = service.sayHello();
        assertEquals("Hello World!", result);
    }

    @Test
    void sayHelloToHamzaTest() {
        String result = service.sayHelloTo("hamza");
        assertEquals("hello hamza", result);
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
}
