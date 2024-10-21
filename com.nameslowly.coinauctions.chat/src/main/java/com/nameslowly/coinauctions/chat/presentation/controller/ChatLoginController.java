package com.nameslowly.coinauctions.chat.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatLoginController {


    @GetMapping("/api/chatUser/login-page")
    public String login() {
        return "login";
    }

}

