package pl.coderslab.charity.controller;

import org.springframework.context.MessageSource;
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
import pl.coderslab.charity.exceptions.UserNotFoundException;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.service.EmailerService;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.service.VerificationTokenService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Locale;

@Controller
class ResetPasswordController {
    private final UserService userService;
    private final EmailerService emailerService;
    private final MessageSource messageSource;
    private final VerificationTokenService tokenService;

    ResetPasswordController(UserService userService,
                            EmailerService emailerService,
                            MessageSource messageSource,
                            VerificationTokenService tokenService) {
        this.userService = userService;
        this.emailerService = emailerService;
        this.messageSource = messageSource;
        this.tokenService = tokenService;
    }

    @GetMapping("reset-password")
    public String showResetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("reset-password")
    public String resetPassword(@RequestParam String email, Model model, Locale locale)
            throws MessagingException, UserNotFoundException {

        emailerService.sendResetPasswordEmail(email, locale);

        String message = messageSource.getMessage("message.resetPassword", null, locale);
        model.addAttribute("successMsg", message);
        return "login";
    }

    @GetMapping("change-password")
    public String showChangePasswordForm(@RequestParam("token") String token, Model model) {

        VerificationToken verificationToken = tokenService.getVerificationToken(token);

        //TODO check user if not null
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
}
