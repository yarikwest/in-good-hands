package pl.coderslab.charity.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
class DashboardController {

    @GetMapping
    public String panel() {
        return "admin/panel";
    }

}
