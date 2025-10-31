package com.m01project.taskmanager.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
class DbTestController {
    @Autowired
    DataSource ds;

    @GetMapping("/api/test/db")
    String test() throws Exception {
        try (var c = ds.getConnection()) {
            return "Connected to: " + c.getMetaData().getURL();
        }
    }
}
