package org.yui.base.annotation.validator;

import org.apache.commons.lang3.StringUtils;
import org.yui.base.util.ValidationUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证字符串是否是身份证号
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = {CertNo.CertNoValidator.class})
public @interface CertNo {

    /**
     * 是否必填
     * @return
     */
    boolean required() default true;

    String message() default "身份证号码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    /**
     * 校验器
     */
    class CertNoValidator implements ConstraintValidator<CertNo,String> {

        private boolean required = true;

        @Override
        public void initialize(CertNo certNo) {
            required = certNo.required();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isBlank(value)) {
                return !required;
            }
            return ValidationUtil.isCert(value);
        }
    }
}
