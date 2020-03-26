package pl.coderslab.charity.controller;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.dto.RegisterUserDto;
import pl.coderslab.charity.dto.RegisterUserMapper;
import pl.coderslab.charity.exceptions.EmailExistsException;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.service.EmailerService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.service.VerificationTokenService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Locale;

@Controller
class AuthController {

    private final UserService userService;
    private final MessageSource messageSource;
    private final EmailerService emailerService;
    private final VerificationTokenService tokenService;
    private final RegisterUserMapper registerUserMapper;

    AuthController(UserService userService,
                   MessageSource messageSource,
                   EmailerService emailerService,
                   VerificationTokenService tokenService,
                   RegisterUserMapper registerUserMapper) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.emailerService = emailerService;
        this.tokenService = tokenService;
        this.registerUserMapper = registerUserMapper;
    }

    @GetMapping("login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("registration")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new RegisterUserDto());

        return "registration";
    }

    @PostMapping("registration")
    public String createUser(@Valid @ModelAttribute("user") RegisterUserDto userDto,
                             BindingResult bindingResult,
                             Model model,
                             Locale locale) throws MessagingException {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        User registered = registerUserMapper.registerUserDtoToUser(userDto);
        try {
            registered = userService.registerNewUser(registered);
        } catch (EmailExistsException e) {
            bindingResult.rejectValue("email", "emailExists.error.message");
            return "registration";
        }

        emailerService.sendConfirmRegistrationEmail(registered.getEmail(), locale);

        String message = messageSource.getMessage("auth.message.regSuccess", null, locale);
        model.addAttribute("successMsg", message);
        return "login";
    }

    @GetMapping("/registration-confirm")
    public String confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = tokenService.getVerificationToken(token);
        userService.activateUser(verificationToken.getUser());

        return "redirect:/login";
    }
}
