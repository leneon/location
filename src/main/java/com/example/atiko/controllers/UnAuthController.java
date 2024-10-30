package com.example.Atiko.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@RequestMapping
@Controller
public class UnAuthController {

    @Value("${app.name}")
    private String appName;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("appName", appName);
        model.addAttribute("title", "Accueil");
        return ("front-office/pages/index");
    }

    @GetMapping("/about")
    public String abut(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("title", "A propos");
        return "front-office/pages/about";
    }
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("title", "Contact");
        return "front-office/pages/contact";
    }
    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("title", "Blog");
        return "front-office/pages/blog";
    }

    @GetMapping("/details-article")
    public String details(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("title", "Details");
        return "front-office/pages/details";
    }

    @GetMapping("/services")
    public String service(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("title", "Services");
        return "front-office/pages/service";
    }

        
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("title", "Se connecter");        
    return "auth/login";
    }
            
    @GetMapping("/reinitialiser")
    public String reinitialiser(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("title", "Réinitialiser mon mot de passe");        
    return "auth/reset-password";
    }
            
    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("title", "Nouveau mot de passe");        
    return "auth/new-password";
    }

    
}
