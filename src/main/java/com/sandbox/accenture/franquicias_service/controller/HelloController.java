package com.sandbox.accenture.franquicias_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
    @GetMapping("/hello")
    public String hello() {


        return "Hola mundoo desde Cloud Run! bd host:"  +System.getenv("DB_HOST");
    }
}