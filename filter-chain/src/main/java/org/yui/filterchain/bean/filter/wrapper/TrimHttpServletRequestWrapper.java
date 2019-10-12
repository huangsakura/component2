package org.yui.filterchain.bean.filter.wrapper;

import org.yui.base.bean.constant.StringConstant;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangjinlong
 * @time 2019-04-12 14:59
 * @description
 */
public final class TrimHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public TrimHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return StringUtils.trim(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {

        String[] values = super.getParameterValues(name);
        if (null == values) {
            return null;
        }

        /**
         * 如果不包含空格，则不进行trim
         */
        boolean needTrim = false;
        for (String value : values) {
            if ((null != value) && (value.contains(StringConstant.ONE_BLANK_SPACE))) {
                needTrim = true;
                break;
            }
        }

        if (needTrim) {
            int length = values.length;
            String[] result = new String[length];

            for (int i=0;i<length;i++) {
                result[i] = StringUtils.trim(values[i]);
            }
            return result;
        } else {
            return values;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> oldValue = super.getParameterMap();
        if (null == oldValue) {
            return null;
        }

        /**
         * 如果不包含空格，则不进行trim
         */
        boolean needTrim= false;
        for (Map.Entry<String,String[]> entry : oldValue.entrySet()) {
            if (null != entry.getValue()) {
                for (String value : entry.getValue()) {
                    if ((null != value) && (value.contains(StringConstant.ONE_BLANK_SPACE))) {
                        needTrim = true;
                        break;
                    }
                }
                if (needTrim) {
                    break;
                }
            }
        }

        if (needTrim) {
            Map<String, String[]> newValue = new HashMap<>(32);
            oldValue.forEach((x,y) -> {
                if (null != y) {
                    int length = y.length;
                    String[] strings = new String[length];

                    for (int i = 0;i < length; i++) {
                        strings[i] = StringUtils.trim(y[i]);
                    }
                    newValue.put(x,strings);
                } else {
                    newValue.put(x,null);
                }
            });
            return newValue;
        } else {
            return oldValue;
        }
    }
}
