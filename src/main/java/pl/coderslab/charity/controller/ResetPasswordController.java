package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.dto.ResetPasswordDto;
import pl.coderslab.charity.dto.UpdatePasswordDto;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.service.VerificationTokenService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

@Controller
class ResetPasswordController {
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;
    private final VerificationTokenService tokenService;

    @Value("${appUrl}")
    private String appUlr;

    ResetPasswordController(UserService userService, JavaMailSender mailSender, MessageSource messageSource, VerificationTokenService tokenService) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.messageSource = messageSource;
        this.tokenService = tokenService;
    }


    @GetMapping("reset-password")
    public String showResetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("reset-password")
    public String resetPassword(@RequestParam String email, Model model, Locale locale) {
        User user = userService.getUserByEmail(email);
        VerificationToken token = tokenService.createVerificationToken(user);

        mailSender.send(constructResetTokenEmail(locale, token.getToken(), user));

        String message = messageSource.getMessage("message.resetPassword", null, locale);
        model.addAttribute("successMsg", message);
        return "login";
    }

    @GetMapping("change-password")
    public String showChangePasswordForm(@RequestParam("token") String token, Model model, Locale locale) {

        VerificationToken verificationToken = tokenService.getVerificationToken(token);

        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "error/custom";
        }

        if (LocalDateTime.now().isAfter(verificationToken.getExpiryDate())) {
            String message = messageSource.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", message);
            return "error/custom";
        }

        User user = verificationToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.singletonList(
                new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        model.addAttribute("newPassword", new UpdatePasswordDto());
        return "update-password";
    }

    @PostMapping("save-password")
    public String savePassword(@AuthenticationPrincipal User user,
                               @Valid @ModelAttribute("newPassword") ResetPasswordDto newPassword,
                               BindingResult bindingResult,
                               Model model,
                               Locale locale) {

        if (bindingResult.hasErrors()) {
            return "update-password";
        }

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
