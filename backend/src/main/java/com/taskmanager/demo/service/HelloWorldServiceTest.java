package com.taskmanager.demo.service;



import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class HelloWorldServiceTest
{
    private final HelloWorldService helloWorldService = new HelloWorldService();
    @Test
    public void testHelloWorldServiceDefault()
    {
        String result = helloWorldService.sayHello(null);
        assertEquals(result, "Hello World!");
    }
    public void testHelloWorldServiceWithName()
    {
        String result = helloWorldService.sayHello("Jack");
        assertEquals(result, "Hello Jack!");
    }
}