package pl.coderslab.charity.validation;

import pl.coderslab.charity.dto.RegisterUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        RegisterUserDto userDto = (RegisterUserDto) value;
        return userDto.getPassword().equals(userDto.getMatchingPassword());
    }
}
