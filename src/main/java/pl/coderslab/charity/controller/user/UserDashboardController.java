package pl.coderslab.charity.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
class UserDashboardController {

    @GetMapping
    public String panel(Model model) {
        return "user/panel";
    }
}
