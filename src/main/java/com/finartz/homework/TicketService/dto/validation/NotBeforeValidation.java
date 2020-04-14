package com.finartz.homework.TicketService.dto.validation;

import com.finartz.homework.TicketService.repositories.FlightRepository;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NotBeforeValidation implements ConstraintValidator<NotBefore, Object> {
    private String departureField;
    private String arrivalField;

    public void initialize(NotBefore constraintAnnotation) {
        this.departureField = constraintAnnotation.departureField();
        this.arrivalField = constraintAnnotation.arrivalField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object departureValue = new BeanWrapperImpl(value)
                .getPropertyValue(departureField);
        Object arrivalValue = new BeanWrapperImpl(value)
                .getPropertyValue(arrivalField);

        LocalDateTime departureDate = (LocalDateTime) departureValue;
        LocalDateTime arrivalDate = (LocalDateTime) arrivalValue;
        if (arrivalDate.isBefore(departureDate)) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Arrival Date("+arrivalDate.toString()+"), cannot be before from Departure Date("+departureDate.toString()+")")
                    .addPropertyNode("departureDate")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("Arrival Date("+arrivalDate.toString()+"), cannot be before from Departure Date("+departureDate.toString()+")")
                    .addPropertyNode("arrivalDate")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
