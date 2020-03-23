package pl.coderslab.charity.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface ValidPassword {

    String message() default "{invalidPassword.error.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
