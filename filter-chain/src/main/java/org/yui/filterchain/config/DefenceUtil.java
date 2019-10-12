package org.yui.filterchain.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.yui.base.bean.constant.StringConstant;
import org.yui.filterchain.bean.filter.RequestInfoHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huangjinlong
 * @time 2019-09-26 15:12
 * @description
 */
@Log4j2
public abstract class DefenceUtil {

    /**
     *
     * @param value
     * @return
     */
    public static String cleanXss(String value) {

        String newValue = value;

        if (StringUtils.isNotBlank(value)) {
            for (Pattern pattern : FILTER_PATTERNS) {
                Matcher matcher = pattern.matcher(newValue);
                if (matcher.find()) {
                    String newValue1 = newValue;
                    newValue = matcher.replaceAll(StringConstant.BLANK);
                    log.warn("命中XSS防火墙。旧值:{}；新值:{}。ip地址:{}"
                            ,newValue1,newValue, RequestInfoHolder.getIp());
                }
            }
        }
        return newValue;
    }

    /**
     * 根据正则表达式判断是否受到xss攻击
     */
    private static final List<Pattern> FILTER_PATTERNS = Collections.unmodifiableList(Arrays.asList(

            // Avoid common html tags
            Pattern.compile("(<input(.*?)></input>|<input(.*)/>)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<span(.*?)</span>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<div(.*)</div>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<style>(.*?)</style>", Pattern.CASE_INSENSITIVE),
            //Avoid onload= expressions
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid anything between script tags
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // Avoid javascript:... expressions
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // Remove any lonesome </script> tag
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid anything in a src='...' type of expression
            Pattern.compile("src[\r\n]*=[\r\n]*\'(.*?)\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid eval(...) expressions
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid vbscript:... expressions
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE)
    ));
}
