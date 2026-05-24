package com.grupo9.todolistapi.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class HomeController {

    @GetMapping("/")
    public void redirectToTasks(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/tasks");
    }
}
