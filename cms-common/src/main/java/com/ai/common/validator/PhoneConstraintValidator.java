package com.ai.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone phone) {
    }

    @Override
    public boolean isValid(String target, ConstraintValidatorContext cxt) {
        if (target == null) {
            return false;
        }
        return target.matches("[0-9()-]*");
    }

}
