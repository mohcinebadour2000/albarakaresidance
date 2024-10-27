package com.example.albarakaResidence.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        // Vous pouvez ajouter des attributs de modèle ici si nécessaire
        return "login"; // Assurez-vous que login.html existe dans le répertoire templates
    }

    @GetMapping("/home")
    public String home() {
        return "home"; // Page d'accueil après la connexion
    }
}
