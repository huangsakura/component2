package org.yui.web.controller.advice;

import org.yui.base.annotation.doc.ApiField;
import org.yui.base.annotation.validator.CertNo;
import org.yui.base.annotation.validator.Chinese;
import org.yui.base.annotation.validator.MobileNo;
import org.yui.base.annotation.validator.PhoneNo;
import org.yui.base.bean.api.JsonResult;
import org.yui.base.bean.constant.StringConstant;
import org.yui.base.exception.BusinessException;
import org.yui.base.util.BeanUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.*;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.constraints.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangjinlong
 */
@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {


    private static volatile Map<String, String> FIELD_MEANING_MAP = null;

    /**
     * 单例模式
     */
    private static void singletonMeaningMap() {
        if (null == FIELD_MEANING_MAP) {
            synchronized (GlobalControllerAdvice.class) {
                if (null == FIELD_MEANING_MAP) {
                    FIELD_MEANING_MAP = new ConcurrentHashMap<>(64);
                }
            }
        }
    }

    /**
     * 组装 com.yunhuakeji.component.web.controller.advice.GlobalControllerAdvice#FIELD_MEANING_MAP
     * 的key
     * @param clazz
     * @param fieldName
     * @return
     */
    private static String packageFieldMapKey(Class<?> clazz,String fieldName) {
        return clazz.getCanonicalName() + StringConstant.UNDERLINE + fieldName;
    }

    /**
     * 请求失败
     */
    private static final String OPERATE_FAIL_STRING = "操作失败";

    /**
     * 全局exception 处理
     * 程序中未捕获的异常会被该方法拦截，做统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public JsonResult globalExceptionHandler(Exception e) {
        return dealException(e);
    }

    /**
     * 把exception转为 ResultEntityAdapter
     * @param e
     * @return
     */
    public static JsonResult dealException(Exception e) {
        singletonMeaningMap();

        JsonResult jsonResult = new JsonResult();
        if (e instanceof BindException) {
            /**
             * 如果是parameter bean校验出错，会走这个分支
             */
            BindException bindException = (BindException)e;
            BindingResult bindingResult = bindException.getBindingResult();
            jsonResult.setCode("PARAMETER_VALIDATE_ERROR");
            jsonResult.setMessage(dealBindingResult(bindingResult));
        } else if (e instanceof MissingServletRequestParameterException) {
            /**
             * 如果是parameter 缺失，会走这个分支
             */
            MissingServletRequestParameterException missingServletRequestParameterException =
                    (MissingServletRequestParameterException)e;
            jsonResult.setCode("REQUEST_PARAMETER_IS_NECESSARY");
            jsonResult.setMessage("缺失参数" + missingServletRequestParameterException.getParameterName());
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            /**
             * 如果是parameter 类型转换错误，会走这个分支
             */
            MethodArgumentTypeMismatchException methodArgumentTypeMismatchException =
                    (MethodArgumentTypeMismatchException)e;
            jsonResult.setCode("REQUEST_PARAMETER_IS_NECESSARY");
            jsonResult.setMessage("参数" + methodArgumentTypeMismatchException.getName() + "类型转换失败");
            methodArgumentTypeMismatchException.getParameter();
        } else if (e instanceof MethodArgumentNotValidException) {
            /**
             * 如果是body校验出错，会走这个分支
             */
            MethodArgumentNotValidException methodArgumentNotValidException =
                    (MethodArgumentNotValidException)e;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            jsonResult.setCode("PARAMETER_VALIDATE_ERROR");
            jsonResult.setMessage(dealBindingResult(bindingResult));
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            /**
             * get post 请求方法错误
             */
            HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException =
                    (HttpRequestMethodNotSupportedException)e;
            jsonResult.setCode("UNSUPPORTED_REQUEST_METHOD");
            jsonResult.setMessage("当前URI不支持" + httpRequestMethodNotSupportedException.getMethod());
        } else if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException)e;
            jsonResult.setCode(businessException.getCode());
            jsonResult.setMessage(businessException.getMessage());
        } else {
            log.error("出现其他类型的异常:{}",e.getMessage());
            jsonResult.setCode(JsonResult.FAIL);
            jsonResult.setMessage(JsonResult.FAIL_MESSAGE);
        }
        return jsonResult;
    }

    /**
     *
     * @param bindingResult
     * @return
     */
    private static String dealBindingResult(BindingResult bindingResult) {
        Class<?> clazz = bindingResult.getTarget().getClass();

        String meaning = null;

        if (!CollectionUtils.isEmpty(bindingResult.getFieldErrors())) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            String key = packageFieldMapKey(clazz,fieldError.getField());

            meaning = Optional.ofNullable(FIELD_MEANING_MAP.get(key)).orElseGet(() -> {
                Field field = Optional.ofNullable(BeanUtil.getDeclaredFieldQuietly(clazz,fieldError.getField()))
                        .orElseThrow(() -> new BusinessException("REFLECTION_GET_FIELD_FAIL","反射获取类的字段失败",false));
                String text = parseValidateMessage(field);
                FIELD_MEANING_MAP.put(key,text);
                return text;
            });
        } else {
            meaning = OPERATE_FAIL_STRING;
        }
        return meaning;
    }

    /**
     *
     * @param field
     * @return
     */
    private static String parseValidateMessage(Field field) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(Optional.ofNullable(field.getAnnotation(ApiField.class))
                .map(ApiField::desc).orElse(field.getName()));
        stringBuilder.append(StringConstant.ONE_BLANK_SPACE);

        Optional.ofNullable(field.getAnnotation(NotBlank.class)).ifPresent(notBlank -> {
            stringBuilder.append("不能为null或空;");
        });
        Optional.ofNullable(field.getAnnotation(NotNull.class)).ifPresent(notNull -> {
            stringBuilder.append("不能为null;");
        });
        Optional.ofNullable(field.getAnnotation(NotEmpty.class)).ifPresent(notEmpty -> {
            stringBuilder.append("集合不能为null或空;");
        });
        Optional.ofNullable(field.getAnnotation(Email.class)).ifPresent(email -> {
            stringBuilder.append("不是邮箱地址;");
        });
        Optional.ofNullable(field.getAnnotation(URL.class)).ifPresent(url -> {
            stringBuilder.append("不是URL;");
        });
        Optional.ofNullable(field.getAnnotation(Min.class)).ifPresent(min -> {
            stringBuilder.append("不满足最小值");
            stringBuilder.append(min.value());
            stringBuilder.append(StringConstant.SEMICOLON);
        });
        Optional.ofNullable(field.getAnnotation(Pattern.class)).ifPresent(pattern -> {
            stringBuilder.append("不满足正则表达式");
            stringBuilder.append(pattern.regexp());
            stringBuilder.append(StringConstant.SEMICOLON);
        });
        Optional.ofNullable(field.getAnnotation(Max.class)).ifPresent(max -> {
            stringBuilder.append("不满足最大值");
            stringBuilder.append(max.value());
            stringBuilder.append(StringConstant.SEMICOLON);
        });
        Optional.ofNullable(field.getAnnotation(Length.class)).ifPresent(length -> {
            stringBuilder.append("长度不满足[");
            stringBuilder.append(length.min());
            stringBuilder.append(StringConstant.GENERAL_COMMA_SPLIT);
            stringBuilder.append(length.max());
            stringBuilder.append(StringConstant.BRACKET_RIGHT);
            stringBuilder.append(StringConstant.SEMICOLON);
        });
        Optional.ofNullable(field.getAnnotation(Size.class)).ifPresent(size -> {
            stringBuilder.append("容量不满足[");
            stringBuilder.append(size.min());
            stringBuilder.append(StringConstant.GENERAL_COMMA_SPLIT);
            stringBuilder.append(size.max());
            stringBuilder.append(StringConstant.BRACKET_RIGHT);
            stringBuilder.append(StringConstant.SEMICOLON);
        });
        Optional.ofNullable(field.getAnnotation(Range.class)).ifPresent(range -> {
            stringBuilder.append("范围不满足[");
            stringBuilder.append(range.min());
            stringBuilder.append(StringConstant.GENERAL_COMMA_SPLIT);
            stringBuilder.append(range.max());
            stringBuilder.append(StringConstant.BRACKET_RIGHT);
            stringBuilder.append(StringConstant.SEMICOLON);
        });
        Optional.ofNullable(field.getAnnotation(MobileNo.class)).ifPresent(range -> {
            stringBuilder.append("不是合法的手机号码;");
        });
        Optional.ofNullable(field.getAnnotation(PhoneNo.class)).ifPresent(range -> {
            stringBuilder.append("不是合法的座机号码;");
        });
        Optional.ofNullable(field.getAnnotation(CertNo.class)).ifPresent(range -> {
            stringBuilder.append("不是合法的身份证号码;");
        });
        Optional.ofNullable(field.getAnnotation(Chinese.class)).ifPresent(range -> {
            stringBuilder.append("不满足汉字限制;");
        });

        return stringBuilder.toString();
    }
}
