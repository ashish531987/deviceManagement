package com.emergent.devicemgmt.domain.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InRangeValidator implements ConstraintValidator<InRange, Long> {
    private long min;
    private long max;

    @Override
    public void initialize(InRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return false;
        return value >= min && value <= max;
    }
}
