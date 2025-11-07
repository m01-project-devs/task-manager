package com.m01project.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private com.m01project.taskmanager.service.Service service;

    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestParam String name) {
        String result = service.sayHelloTo(name);
        return ResponseEntity.ok(result);
    }
}
