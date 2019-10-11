package org.yui.base.annotation.validator;

import org.apache.commons.lang3.StringUtils;
import org.yui.base.util.ValidationUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验 字符串是否是座机号
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = { PhoneNo.PhoneNoValidator.class })
public @interface PhoneNo {

    String message() default "座机号码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean required() default true;

    /**
     * 校验器
     */
    class PhoneNoValidator implements ConstraintValidator<PhoneNo,String> {

        private boolean required = true;

        @Override
        public void initialize(PhoneNo phoneNo) {
            required = phoneNo.required();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isBlank(value)) {
                return !required;
            }

            return ValidationUtil.isPhone(value);
        }
    }
}
