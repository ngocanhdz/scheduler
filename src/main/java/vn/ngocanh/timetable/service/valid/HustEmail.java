package vn.ngocanh.timetable.service.valid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = HustEmailValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HustEmail {
    String message() default "Email phải là địa chỉ email HUST (kết thúc bằng @sis.hust.edu.vn)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
