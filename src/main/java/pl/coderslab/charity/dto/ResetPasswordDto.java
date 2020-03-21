package pl.coderslab.charity.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.coderslab.charity.validation.PasswordMatcher;
import pl.coderslab.charity.validation.PasswordMatches;
import pl.coderslab.charity.validation.ValidPassword;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordMatches
public class ResetPasswordDto implements PasswordMatcher {

    @ValidPassword
    String password;

    String matchingPassword;
}
