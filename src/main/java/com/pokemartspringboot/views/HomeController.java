package com.pokemartspringboot.views;

import com.pokemartspringboot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping
    public String viewHomePage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "index";
    }

}
