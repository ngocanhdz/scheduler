package vn.ngocanh.timetable.service.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HustEmailValidator implements ConstraintValidator<HustEmail, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.toLowerCase().endsWith("@sis.hust.edu.vn");
    }
}
