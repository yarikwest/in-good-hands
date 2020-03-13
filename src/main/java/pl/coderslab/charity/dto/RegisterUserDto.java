package pl.coderslab.charity.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import pl.coderslab.charity.validation.PasswordMatches;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class RegisterUserDto {

    @Email
    @NotBlank
    String email;

    @NotBlank
    @Length(min = 8)
    String password;

    String matchingPassword;
}
