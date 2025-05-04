package fr.upsaclay.easychair.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Cette méthode s'exécute lorsque l'utilisateur accède à l'URL '/'
    @GetMapping("/")
    public String homePage(Model model) {
        // Ajouter des données dynamiques dans le modèle (ex: un titre de conférence)
        model.addAttribute("conferenceTitle", "EasyChair Conference");

        // Retourne le nom du fichier de vue Thymeleaf, sans l'extension .html
        return "index";
    }
}

