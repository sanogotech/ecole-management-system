package com.ecole.controller;
import com.ecole.model.Etudiant;
import com.ecole.service.EtudiantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
public class EtudiantController {
    private final EtudiantService service;
    public EtudiantController(EtudiantService service) {
        this.service = service;
    }
    @GetMapping("/")
    public String accueil() {
        return "index";
    }
    @GetMapping("/etudiants")
    public String lister(Model model) {
        model.addAttribute("etudiants", service.lister());
        return "etudiants";
    }
    @GetMapping("/etudiants/nouveau")
    public String nouveau(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        return "form-etudiant";
    }
    @PostMapping("/etudiants")
    public String ajouter(@ModelAttribute Etudiant e) {
        service.ajouter(e);
        return "redirect:/etudiants";
    }
    @PostMapping("/etudiants/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return "redirect:/etudiants";
    }
}
