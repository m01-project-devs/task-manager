package com.m01project.taskmanager;

import com.m01project.taskmanager.demo.service.HelloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldServiceTest {

    private HelloService service;

    @BeforeEach
    void setUp() {
        service = new HelloService();
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

}
