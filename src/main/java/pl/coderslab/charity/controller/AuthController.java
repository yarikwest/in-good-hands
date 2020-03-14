package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.dto.RegisterUserDto;
import pl.coderslab.charity.dto.RegisterUserMapper;
import pl.coderslab.charity.exceptions.EmailExistsException;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;

@Controller
class AuthController {

    private final UserService userService;
    private final RegisterUserMapper registerUserMapper;

    AuthController(UserService userService, RegisterUserMapper registerUserMapper) {
        this.userService = userService;
        this.registerUserMapper = registerUserMapper;
    }

    @GetMapping("login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new RegisterUserDto());

        return "register";
    }

    @PostMapping("register")
    public String createUser(@Valid @ModelAttribute("user") RegisterUserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = registerUserMapper.registerUserDtoToUser(userDto);
        try {
            userService.registerNewUser(user);
        } catch (EmailExistsException e) {
            bindingResult.rejectValue("email", "message.regError");
            return "register";
        }
        return "index";
    }
}
