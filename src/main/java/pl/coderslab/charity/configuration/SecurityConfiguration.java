package pl.coderslab.charity.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.coderslab.charity.service.UserService;

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    public SecurityConfiguration(UserService userDetailsService,
                                 BCryptPasswordEncoder bCryptPasswordEncoder,
                                 AuthenticationSuccessHandler successHandler,
                                 AuthenticationFailureHandler failureHandler) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userDetailsService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/sass/**", "/vendors/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String loginPage = "/login";
        String logoutPage = "/logout";

        http.
                authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/user/**").hasAuthority("ROLE_USER")
                .antMatchers("/donations").hasAnyRole("ADMIN", "USER")
                .anyRequest().permitAll()
                .and().csrf().disable()
                .formLogin()
                .loginPage(loginPage).permitAll()
                .usernameParameter("email")
                .passwordParameter("password")
                .failureHandler(failureHandler)
                .successHandler(successHandler)
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage))
                .logoutSuccessUrl(loginPage).and().exceptionHandling();
    }
}
