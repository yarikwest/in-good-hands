package pl.coderslab.charity.dto;

import lombok.*;
import pl.coderslab.charity.validation.PasswordMatcher;
import pl.coderslab.charity.validation.PasswordMatches;
import pl.coderslab.charity.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class RegisterUserDto implements PasswordMatcher {

    @Email
    @NotBlank
    String email;

    @ValidPassword
    String password;

    String matchingPassword;
}
