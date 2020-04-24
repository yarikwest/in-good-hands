package pl.coderslab.charity.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.model.Category;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("donations")
class DonationController {
    private final UserService userService;
    private final DonationService donationService;
    private final CategoryService categoryService;
    private final InstitutionService institutionService;

    DonationController(UserService userService, DonationService donationService, CategoryService categoryService, InstitutionService institutionService) {
        this.userService = userService;
        this.donationService = donationService;
        this.categoryService = categoryService;
        this.institutionService = institutionService;
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("donation", new Donation());

        return "form";
    }

    @PostMapping
    public String createDonation(@Valid @ModelAttribute Donation donation,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "form";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User userByEmail = userService.getUserByEmail(username);
        donation.setUser(userByEmail);
        donationService.create(donation);
        return "form-confirmation";
    }

    @ModelAttribute("allCategories")
    public Set<Category> getAllCategories() {
        return categoryService.getAll();
    }

    @ModelAttribute("allInstitutions")
    public Set<Institution> getAllInstitutions() {
        return institutionService.getAll();
    }
}
