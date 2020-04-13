package com.finartz.homework.TicketService.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotBeforeValidation.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBefore {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String message() default "";

    String departureField();

    String arrivalField();
}
