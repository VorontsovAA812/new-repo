package com.example.demo.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "redirect:/documents"; // Перенаправляем на список документов
    }
    @GetMapping("/documents")
    public String documents() {
        return "documents"; // documents.html
    }

    @GetMapping("/")
    public String homePage() {
        return "index"; // Указывает на файл src/main/resources/templates/index.html
    }


}
