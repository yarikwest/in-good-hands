package pl.coderslab.charity.controller.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("user/donations")
class UserDonationsController {
    private final UserService userService;
    private final DonationService donationService;

    UserDonationsController(UserService userService, DonationService donationService) {
        this.userService = userService;
        this.donationService = donationService;
    }

    @GetMapping
    public String panel(Model model,
                        @RequestParam(defaultValue = "1") Integer page,
                        @RequestParam(defaultValue = "5") Integer size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "true") Boolean asc) throws Throwable {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userEmail);

        Page<Donation> donationPage;

        if (asc) {
            donationPage = donationService.getAllByUserSotred(
                    user, PageRequest.of(page - 1, size, Sort.Direction.ASC, sortBy));
        } else {
            donationPage = donationService.getAllByUserSotred(
                    user, PageRequest.of(page - 1, size, Sort.Direction.DESC, sortBy));
        }

        int totalPages = donationPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("donations", donationPage);

        return "user/donations";
    }
}
