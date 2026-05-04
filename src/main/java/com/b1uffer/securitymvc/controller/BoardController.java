package com.b1uffer.securitymvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BoardController {
    private final List<String> posts = new ArrayList<>();

    @GetMapping("/board")
    public String board(Model model) {
        model.addAttribute("posts", posts);
        return "board";
    }

    @PostMapping("/board")
    public String addPost(@RequestParam String content) {
        posts.add(content);
        return "redirect:/board";
    }
}
