package org.yui.base.annotation.validator;

import org.apache.commons.lang3.StringUtils;
import org.yui.base.enums.ChineseValidatorEnum;
import org.yui.base.util.ValidationUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证 是否是汉字，是否包含汉字，是否不包含汉字 等
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = {Chinese.ChineseValidator.class})
public @interface Chinese {

    /**
     * 校验类型
     * @return
     */
    ChineseValidatorEnum checkType();

    String message() default "字符串是否包含汉字校验不通过";

    /**
     * 是否必填
     * @return
     */
    boolean required() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    /**
     * 校验器
     */
    class ChineseValidator implements ConstraintValidator<Chinese,String> {

        private ChineseValidatorEnum chineseValidatorEnum = null;
        private boolean required = true;

        @Override
        public void initialize(Chinese chinese) {
            chineseValidatorEnum = chinese.checkType();
            required = chinese.required();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isBlank(value)) {
                return !required;
            }

            switch (chineseValidatorEnum) {
                case CONTAIN_CHINESE:{
                    return ValidationUtil.containChinese(value);
                } case ALL_CHINESE:{
                    return !ValidationUtil.containUnChinese(value);
                } case NO_CHINESE:{
                    return !ValidationUtil.containChinese(value);
                } default:{
                    return false;
                }
            }
        }
    }
}
