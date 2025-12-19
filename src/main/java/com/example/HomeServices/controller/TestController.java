package com.example.HomeServices.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin("*")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        System.out.println("✅ Получен запрос на /api/test/hello");
        return "✅ Spring Boot работает!";
    }

    @GetMapping("/check")
    public String check() {
        return "{\"status\": \"ok\", \"message\": \"Контроллеры работают\"}";
    }
}