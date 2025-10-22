package com.m01project.taskmanager.demo.service;

import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HelloWorldServiceTest {

    @Test
    void sayHello_ShouldReturnHelloWorld() {
        HelloWorldService service = new HelloWorldService();
        String result = service.sayHello();
        assertEquals("Hello, World", result);
    }
}
