package org.yui.base.annotation.validator;

import org.apache.commons.lang3.StringUtils;
import org.yui.base.util.ValidationUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验 字符串是否是 手机号
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = { MobileNo.MobileNoValidator.class })
public @interface MobileNo {

    String message() default "手机号码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean required() default true;

    /**
     * 校验器
     */
    class MobileNoValidator implements ConstraintValidator<MobileNo,String> {

        private boolean required = true;

        @Override
        public void initialize(MobileNo mobileNo) {
            required = mobileNo.required();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isBlank(value)) {
                return !required;
            }

            return ValidationUtil.isMobile(value);
        }
    }
}
