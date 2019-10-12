package org.yui.spring.util;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.yui.base.enums.Env;

/**
 * @author huangjinlong
 * @time 2019-10-11 16:58
 * @description
 */
public abstract class EnvUtil {

    /**
     * 是否是线上环境
     * @param environment
     * @return
     */
    public static boolean isOnline(Environment environment) {
        return checkEnv(environment, Env.ONLINE);
    }

    /**
     *
     * @param environment
     * @return
     */
    public static boolean isShow(Environment environment) {
        return checkEnv(environment,Env.SHOW);
    }


    /**
     *
     * @param environment
     * @return
     */
    public static boolean isShowOrOnline(Environment environment) {
        return checkEnv(environment,Env.SHOW,Env.ONLINE);
    }

    /**
     *
     * @param environment
     * @return
     */
    public static boolean isDevOrTest(Environment environment) {
        return checkEnv(environment,Env.DEV,Env.TEST);
    }


    /**
     *
     * @param environment
     * @param envs
     * @return
     */
    public static boolean checkEnv(Environment environment,Env... envs) {

        for (String x : environment.getActiveProfiles()) {
            for (Env env : envs) {
                if (x.equalsIgnoreCase(env.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     */
    public static class DevOrTestCondition implements Condition {
        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

            Environment environment = conditionContext.getEnvironment();

            String[] activeProfiles = environment.getActiveProfiles();
            for (String x : activeProfiles) {
                if (Env.DEV.getLowerCode().equals(x) || Env.TEST.getLowerCode().equals(x)) {
                    return true;
                }
            }
            return false;
        }
    }
}
