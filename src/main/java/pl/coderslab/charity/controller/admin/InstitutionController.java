package pl.coderslab.charity.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.service.InstitutionService;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("admin/institutions")
class InstitutionController {
    private final InstitutionService institutionService;

    InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping
    public String institutionsPage() {
        return "admin/institutions/institutions";
    }

    @GetMapping("add")
    public String addForm(Model model) {
        model.addAttribute("institution", new Institution());
        return "admin/institutions/add";
    }

    @PostMapping("add")
    public String add(@Valid @ModelAttribute Institution institution, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/institutions/add";
        }

        institutionService.create(institution);
        return "redirect:/admin/institutions";
    }

    @GetMapping("edit/{id}")
    public String editForm(Model model, @PathVariable long id) {
        Institution institutionForEdit = institutionService.getById(id);
        model.addAttribute("institution", institutionForEdit);
        return "admin/institutions/edit";
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable long id, @Valid @ModelAttribute Institution institution, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/institutions/edit";
        }

        institutionService.update(id, institution);
        return "redirect:/admin/institutions";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable long id) {
        institutionService.delete(id);
        return "redirect:/admin/institutions";
    }

    @ModelAttribute("allInstitutions")
    public Set<Institution> getAllInstitutions() {
        return institutionService.getAll();
    }
}
