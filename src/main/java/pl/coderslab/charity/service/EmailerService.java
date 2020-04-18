package pl.coderslab.charity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import pl.coderslab.charity.dto.EmailDto;
import pl.coderslab.charity.exceptions.UserNotFoundException;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
public class EmailerService {

    private static final String LOGO_IMAGE = "static/images/logo/materialize-logo-color-full.png";
    private static final String PNG_MIME = "image/png";

    @Value("${appUrl}")
    private String appUlr;

    private final JavaMailSender mailSender;
    private final ITemplateEngine htmlTemplateEngine;
    private final UserService userService;
    private final VerificationTokenService tokenService;

    EmailerService(JavaMailSender mailSender, ITemplateEngine htmlTemplateEngine, UserService userService, VerificationTokenService tokenService) {
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public void sendResetPasswordEmail(String email, Locale locale) throws MessagingException, UserNotFoundException {
        final User user = userService.getUserByEmail(email);
        final VerificationToken token = tokenService.createVerificationToken(user);
        final String resetUrl = appUlr + "/change-password?token=" + token.getToken();

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final Context context = new Context(locale);
        context.setVariable("appUrl", appUlr);
        context.setVariable("urlToResetPassword", resetUrl);
        context.setVariable("logoImageName", "logoImageName");

        EmailDto emailDto = new EmailDto(
                email,
                "Reset Password",
                htmlTemplateEngine.process("reset-password", context));

        prepareHtmlMessage(mimeMessage, emailDto);

        mailSender.send(mimeMessage);
    }

    public void sendConfirmRegistrationEmail(String email, Locale locale) throws MessagingException, UserNotFoundException {
        final User user = userService.getUserByEmail(email);
        final VerificationToken token = tokenService.createVerificationToken(user);
        String confirmationUrl = appUlr + "/registration-confirm?token=" + token.getToken();

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final Context context = new Context(locale);
        context.setVariable("appUrl", appUlr);
        context.setVariable("urlToConfirmRegistration", confirmationUrl);
        context.setVariable("logoImageName", "logoImageName");

        EmailDto emailDto = new EmailDto(
                email,
                "Registration Confirmation",
                htmlTemplateEngine.process("confirm-registration", context));

        prepareHtmlMessage(mimeMessage, emailDto);

        mailSender.send(mimeMessage);
    }

    private void prepareHtmlMessage(MimeMessage mimeMessage, EmailDto emailDto) throws MessagingException {

        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getMessage(), true);
        message.addInline("logoImageName", new ClassPathResource(LOGO_IMAGE), PNG_MIME);
    }
}
