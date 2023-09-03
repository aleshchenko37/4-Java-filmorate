package ru.yandex.practicum.filmorate.validators;

import javax.validation.Constraint;
import javax.validation.constraints.Past;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Past
public @interface AfterDate {
    String message() default "Дата не должна быть ранее {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value = "1895-12-28";
}
