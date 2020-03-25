package pl.coderslab.charity.registration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.service.UserService;
import pl.coderslab.charity.service.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Value("${appUrl}")
    private String appUrl;

    private final UserService service;
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;
    private final VerificationTokenService tokenService;


    public RegistrationListener(UserService service, JavaMailSender mailSender, MessageSource messageSource, VerificationTokenService tokenService) {
        this.service = service;
        this.mailSender = mailSender;
        this.messageSource = messageSource;
        this.tokenService = tokenService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        VerificationToken token = tokenService.createVerificationToken(user);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "/registrationConfirm?token=" + token.getToken();
        String message = messageSource.getMessage("auth.message.emailTextRegSuccess", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + appUrl + confirmationUrl);
        mailSender.send(email);
    }
}
