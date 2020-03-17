package pl.coderslab.charity.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import pl.coderslab.charity.validation.PasswordMatcher;
import pl.coderslab.charity.validation.PasswordMatches;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordMatches
public class UpdatePasswordDto implements PasswordMatcher {
    @NotBlank
    String oldPassword;

    @NotBlank
    @Length(min = 8)
    String password;

    String matchingPassword;
}
