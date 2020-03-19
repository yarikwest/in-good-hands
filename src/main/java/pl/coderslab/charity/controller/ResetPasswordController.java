package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import pl.coderslab.charity.dto.ResetPasswordDto;
import pl.coderslab.charity.dto.UpdatePasswordDto;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

@Controller
class ResetPasswordController {
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Value("${appUrl}")
    private String appUlr;

    ResetPasswordController(UserService userService, JavaMailSender mailSender, MessageSource messageSource) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.messageSource = messageSource;
    }


    @GetMapping("reset-password")
    public String showResetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("reset-password")
    public String resetPassword(WebRequest request, @RequestParam String email, Model model) {
        User user = userService.getUserByEmail(email);
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        mailSender.send(constructResetTokenEmail(request.getLocale(), token, user));
        String message = messageSource.getMessage("message.resetPassword", null, request.getLocale());
        model.addAttribute("successMsg", message);
        return "login";
    }

    @GetMapping("change-password")
    public String showChangePasswordForm(Locale locale, Model model, @RequestParam("token") String token) {

        VerificationToken verificationToken = userService.getVerificationToken(token);

        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "error/bad-user";
        }

        if (LocalDateTime.now().isAfter(verificationToken.getExpiryDate())) {
            String message = messageSource.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", message);
            return "error/bad-user";
        }

        User user = verificationToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(
                new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        model.addAttribute("newPassword", new UpdatePasswordDto());
        return "update-password";
    }

    @PostMapping("save-password")
    public String savePassword(Locale locale, @Valid @ModelAttribute("newPassword") ResetPasswordDto newPassword,
                               BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "update-password";
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userService.changeUserPassword(user, newPassword.getPassword());
        SecurityContextHolder.getContext().setAuthentication(null);
        
        String message = messageSource.getMessage("message.resetPasswordSuccess", null, locale);
        model.addAttribute("successMsg", message);
        return "login";
    }

    private SimpleMailMessage constructResetTokenEmail(Locale locale, String token, User user) {
        String url = appUlr + "/change-password?token=" + token;
        String message = messageSource.getMessage("message.emailTextResetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        return email;
    }
}
