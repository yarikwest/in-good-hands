package pl.coderslab.charity.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.UserService;

@Controller
@RequestMapping("admin")
class DashboardController {
    private final UserService userService;
    private final DonationService donationService;
    private final InstitutionService institutionService;

    DashboardController(UserService userService, DonationService donationService, InstitutionService institutionService) {
        this.userService = userService;
        this.donationService = donationService;
        this.institutionService = institutionService;
    }

    @GetMapping
    public String panel(Model model)  {
        model.addAttribute("institutionsCount", institutionService.getCountAll());
        model.addAttribute("usersCount", userService.getCountAll());
        model.addAttribute("newUsersCount", userService.getCountAllNewUserFromLastMonth());
        model.addAttribute("donationsCount", donationService.getCountAllDonations());
        model.addAttribute("newDonationsCount", donationService.getCountAllDonationsFromLastMonth());
        model.addAttribute("newQuantityCount", donationService.getCountAllPackagesFromLastMonth());
        model.addAttribute("supportedInstitutions", donationService.getCountAllSupportedInstitutionsFromLastMonth());
        model.addAttribute("notReceivedDonations", donationService.getAllNotReceivedDonations());
        return "admin/panel";
    }


}
