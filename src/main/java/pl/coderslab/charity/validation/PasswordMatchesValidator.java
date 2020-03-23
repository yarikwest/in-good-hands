package pl.coderslab.charity.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        PasswordMatcher passwordMatcher = (PasswordMatcher) value;
        return passwordMatcher.getPassword().equals(passwordMatcher.getMatchingPassword());
    }
}
