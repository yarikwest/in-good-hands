package pl.coderslab.charity.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import pl.coderslab.charity.dto.RegisterUserDto;
import pl.coderslab.charity.dto.RegisterUserMapper;
import pl.coderslab.charity.exceptions.EmailExistsException;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.registration.OnRegistrationCompleteEvent;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;

@Controller
class AuthController {

    private final UserService userService;
    private final MessageSource messageSource;
    private final RegisterUserMapper registerUserMapper;
    private final ApplicationEventPublisher eventPublisher;

    AuthController(UserService userService, MessageSource messageSource, RegisterUserMapper registerUserMapper, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.registerUserMapper = registerUserMapper;
        this.eventPublisher = eventPublisher;
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
    public String createUser(@Valid @ModelAttribute("user") RegisterUserDto userDto, BindingResult bindingResult, WebRequest request, Model model) {
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

        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(
                registered, request.getLocale(), appUrl
        ));
        Locale locale = request.getLocale();
        String message = messageSource.getMessage("auth.message.regSuccess", null, locale);
        model.addAttribute("successMsg", message);
        return "login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "error/bad-user";
        }

        User user = verificationToken.getUser();
        if (LocalDateTime.now().isAfter(verificationToken.getExpiryDate())) {
            String message = messageSource.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", message);
            return "error/bad-user";
        }

        user.setActive(true);
        userService.saveRegisteredUser(user);
        return "redirect:/login";
    }
}
