package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.coderslab.charity.model.Institution;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;

import java.util.Set;

@Controller
public class HomeController {

    private final DonationService donationService;
    private final InstitutionService institutionService;

    public HomeController(DonationService donationService, InstitutionService institutionService) {
        this.donationService = donationService;
        this.institutionService = institutionService;
    }


    @GetMapping("/")
    public String homeAction(Model model) {
        Set<Institution> institutions = institutionService.getAll();
        long countAllDonations = donationService.getCountAllDonations();
        long sumOfAllPackage = donationService.getSumOfAllPackage();

        model.addAttribute("institutions", institutions);
        model.addAttribute("countAllDonations", countAllDonations);
        model.addAttribute("sumOfAllPackage", sumOfAllPackage);

        return "index";
    }
}
