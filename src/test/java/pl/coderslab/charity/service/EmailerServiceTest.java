package pl.coderslab.charity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EmailerServiceTest {

    @Mock
    JavaMailSender mailSender;

    @Mock
    ITemplateEngine htmlTemplateEngine;

    @Mock
    UserService userService;

    @Mock
    VerificationTokenService tokenService;

    @InjectMocks
    EmailerService emailerService;

    String email;
    User user;
    VerificationToken token;
    MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        //given
        email = "test@email";
        user = new User();
        token = new VerificationToken();
        mimeMessage = new MimeMessage((Session) null);

        given(userService.getUserByEmail(anyString())).willReturn(user);
        given(tokenService.createVerificationToken(any(User.class))).willReturn(token);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);
        given(htmlTemplateEngine.process(anyString(), any(Context.class))).willReturn("some message");
    }

    @Test
    void shouldSendResetPasswordEmail() throws MessagingException {
        //when
        emailerService.sendResetPasswordEmail(email, null);

        //then
        then(userService).should().getUserByEmail(anyString());
        then(tokenService).should().createVerificationToken(any(User.class));
        then(mailSender).should().send(any(MimeMessage.class));
        assertAll(
                () -> assertThat(mimeMessage.getAllRecipients()[0].toString()).isEqualTo(email),
                () -> assertThat(mimeMessage.getSubject()).isEqualTo("Reset Password")
        );
    }

    @Test
    void shouldSendConfirmRegistrationEmail() throws MessagingException {
        //when
        emailerService.sendConfirmRegistrationEmail(email, null);

        //then
        then(userService).should().getUserByEmail(anyString());
        then(tokenService).should().createVerificationToken(any(User.class));
        then(mailSender).should().send(any(MimeMessage.class));
        assertAll(
                () -> assertThat(mimeMessage.getAllRecipients()[0].toString()).isEqualTo(email),
                () -> assertThat(mimeMessage.getSubject()).isEqualTo("Registration Confirmation")
        );
    }
}
