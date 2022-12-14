package com.pokemartspringboot.views;

import com.pokemartspringboot.reports.ReportService;
import com.pokemartspringboot.user.UserService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

//@Controller
@RestController
@RequestMapping("/users")
public class HomeController {

    @Autowired
    UserService userService;
    @Autowired
    ReportService reportService;

//    @GetMapping
//    public String viewHomePage(Model model) {
//        model.addAttribute("users", userService.findAll());
//        return "index";
//    }

    @GetMapping("/report")
    public String generateReport() throws JRException, FileNotFoundException {
        reportService.exportReport();
        return "File generated!";
    }

}
