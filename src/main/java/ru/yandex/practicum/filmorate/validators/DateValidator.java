package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<AfterDate, LocalDate> {
    private LocalDate date;

    @Override
    public void initialize(AfterDate constraintAnnotation) {
        date = LocalDate.parse(constraintAnnotation.value);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(date);
    }
}
