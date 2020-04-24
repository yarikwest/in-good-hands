package pl.coderslab.charity.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pl.coderslab.charity.service.UserService;

@MockBean(UserService.class)
@MockBean(BCryptPasswordEncoder.class)
@MockBean(AuthenticationSuccessHandler.class)
@MockBean(AuthenticationFailureHandler.class)
@TestConfiguration
public class TestSecurityConfiguration {

}

