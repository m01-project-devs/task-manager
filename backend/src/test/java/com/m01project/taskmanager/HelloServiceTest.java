package com.m01project.taskmanager;

import com.m01project.taskmanager.demo.service.HelloService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {

    private HelloService helloService;

    @BeforeEach
    void setUp() {
        helloService = new HelloService();
    }

    @Test
    void generateGreeting_withValidName_returnsCorrectGreeting() {
        // Given
        String name = "John";

        // When
        String result = helloService.generateGreeting(name);

        // Then
        assertEquals("Hello John", result);
    }

    @Test
    void generateGreeting_withNameHavingSpaces_trimsAndReturnsGreeting() {
        // Given
        String name = "  Alice  ";

        // When
        String result = helloService.generateGreeting(name);

        // Then
        assertEquals("Hello Alice", result);
    }

    @Test
    void generateGreeting_withNullName_throwsException() {
        // Given
        String name = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            helloService.generateGreeting(name);
        });
    }

    @Test
    void generateGreeting_withEmptyName_throwsException() {
        // Given
        String name = "";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            helloService.generateGreeting(name);
        });
    }

    @Test
    void generateGreeting_withBlankName_throwsException() {
        // Given
        String name = "   ";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            helloService.generateGreeting(name);
        });
    }
}
