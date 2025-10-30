package com.m01project.taskmanager.demo.controller;

import com.m01project.taskmanager.demo.service.HelloService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")

public class HelloController {

    public HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return helloService.sayHello();
    }


    @GetMapping("/hello/{name}")
    public String sayHelloToName(@PathVariable String name) {
        return helloService.sayHelloTo(name);
    }




}
