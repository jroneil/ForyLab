package com.example.demo.compare.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompareController {

    @Value("${forylab.session.backend:Servlet Container (In-Memory)}")
    private String sessionBackend;

    @GetMapping("/compare")
    public String compare(Model model) {
        model.addAttribute("sessionBackend", sessionBackend);
        return "compare";
    }
}
