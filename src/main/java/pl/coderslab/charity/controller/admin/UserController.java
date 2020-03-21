package pl.coderslab.charity.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.dto.UserDto;
import pl.coderslab.charity.dto.UserMapper;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @GetMapping
    public String usersPage() {
        return "admin/users/users";
    }

    @GetMapping("edit/{id}")
    public String editForm(@PathVariable long id, Model model) throws Throwable {
        UserDto userDto = userMapper.userToUserDto(userService.getById(id));
        model.addAttribute("user", userDto);
        return "admin/users/edit";
    }

    @PostMapping("edit/{id}")
    public String edit(@PathVariable long id, @Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult) throws Throwable {
        if (bindingResult.hasErrors()) {
            return "admin/users/edit";
        }

        User user = userMapper.userDtoToUser(userDto);
        userService.updateUserData(id, user);
        return "redirect:/admin/users";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @GetMapping("block/{id}")
    public String block(@PathVariable long id) throws Throwable {
        userService.toggleActive(id);
        return "redirect:/admin/users";
    }

    @ModelAttribute("allUsers")
    public Set<UserDto> getAllUsers() throws Throwable {
        return userMapper.usersToUserDtos(userService.getAllUsers());
    }
}
