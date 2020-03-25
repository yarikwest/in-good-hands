package pl.coderslab.charity.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

@Controller
@RequestMapping("user")
class UserDashboardController {
    private final UserService userService;
    private final DonationService donationService;

    UserDashboardController(UserService userService, DonationService donationService) {
        this.userService = userService;
        this.donationService = donationService;
    }

    @GetMapping
    public String panel(Model model, @AuthenticationPrincipal User authUser) {
        User user = userService.getUserByEmail(authUser.getEmail());
        model.addAttribute("donationsCount", donationService.getCountAllDonationsByUser(user));
        model.addAttribute("quantityCount", donationService.getSumOfAllPackageByUser(user));
        model.addAttribute("institutionsCount", donationService.getCountAllSupportedInstitutionsByUser(user));
        model.addAttribute("donations", donationService.getAllByUser(user));
        return "user/panel";

    }
}
