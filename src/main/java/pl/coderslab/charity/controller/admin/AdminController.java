package pl.coderslab.charity.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.dto.AdminDto;
import pl.coderslab.charity.dto.AdminMapper;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("admin/admins")
class AdminController {
    private final AdminMapper adminMapper;
    private final UserService userService;

    AdminController(AdminMapper adminMapper, UserService userService) {
        this.adminMapper = adminMapper;
        this.userService = userService;
    }

    @GetMapping
    public String adminsPage() {
        return "admin/administrators/administrators";
    }

    @GetMapping("add")
    public String addForm(Model model) {
        model.addAttribute("user", new AdminDto());
        return "admin/administrators/add";
    }

    @PostMapping("add")
    public String add(@Valid @ModelAttribute("user") AdminDto adminDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/administrators/add";
        }

        User user = adminMapper.adminDtoToUser(adminDto);
        userService.createAdmin(user);
        return "redirect:/admin/admins";
    }

    @GetMapping("edit/{id}")
    public String editForm(@PathVariable long id, Model model) {
        AdminDto adminDto = adminMapper.userToAdminDto(userService.getById(id));
        model.addAttribute("user", adminDto);
        return "admin/administrators/edit";
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable long id, @Valid @ModelAttribute("user") AdminDto adminDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/administrators/edit";
        }

        User user = adminMapper.adminDtoToUser(adminDto);
        userService.updateAdminData(id, user);
        return "redirect:/admin/admins";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/admin/admins";
    }

    @ModelAttribute("allAdmins")
    public Set<AdminDto> getAllAdmins() {
        return adminMapper.usersToAdminDtos(userService.getAllAdmins());
    }
}
