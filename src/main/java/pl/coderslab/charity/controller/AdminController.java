package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.service.InstitutionService;

import java.util.Set;

@Controller
@RequestMapping("admin")
class AdminController {
    private final InstitutionService institutionService;

    AdminController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping
    public String panel() {
        return "admin/panel";
    }

    @GetMapping("institutions")
    public String institutions() {
        return "admin/institutions";
    }

//    @GetMapping("admins")
//    public String admins() {
//        return "admin/admins";
//    }
//
//    @GetMapping("users")
//    public String users() {
//        return "admin/users";
//    }


    @ModelAttribute("allInstitutions")
    public Set<Institution> getAllInstitutions() {
        return institutionService.getAll();
    }
}
