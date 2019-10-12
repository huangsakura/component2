package org.yui.expression.util;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author huangjinlong
 * @time 2019-03-07 19:09
 * @description
 */
public abstract class SpelUtil {

    /**
     * 解析
     * @param spel
     * @param method
     * @param args
     * @return
     */
    public static String parse(String spel,Method method, Object[] args) {
        /**
         * 创建解析器
         */
        ExpressionParser expressionParser = new SpelExpressionParser();
        StandardEvaluationContext standardEvaluationContext = packageEvaluationContext(method,args);
        return expressionParser.parseExpression(spel).getValue(standardEvaluationContext, String.class);
    }

    /**
     *
     * @param spel
     * @param standardEvaluationContext
     * @return
     */
    public static String parse(String spel,StandardEvaluationContext standardEvaluationContext) {
        /**
         * 创建解析器
         */
        ExpressionParser expressionParser = new SpelExpressionParser();
        return expressionParser.parseExpression(spel).getValue(standardEvaluationContext, String.class);
    }


    /**
     *
     * @param method
     * @param args
     * @return
     */
    public static StandardEvaluationContext packageEvaluationContext(Method method,Object[] args) {

        /**
         * 获取被拦截方法的参数名列表
         */
        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer =
                new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);

        /**
         * spel的上下文
         */
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        /**
         * 放入参数
         */
        if ((null != paramNames) && (null != args)) {
            for (int i = 0; i < paramNames.length; i++) {
                standardEvaluationContext.setVariable(paramNames[i], args[i]);
            }
        }

        return standardEvaluationContext;
    }
}
