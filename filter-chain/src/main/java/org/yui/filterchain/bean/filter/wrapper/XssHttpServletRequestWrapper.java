package org.yui.filterchain.bean.filter.wrapper;

import org.yui.filterchain.config.DefenceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangjinlong
 */
public final class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return DefenceUtil.cleanXss(value);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return DefenceUtil.cleanXss(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = DefenceUtil.cleanXss(values[i]);
        }
        return encodedValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {

        Map<String, String[]> values = super.getParameterMap();
        if (values == null) {
            return null;
        }

        Map<String, String[]> newValues = new HashMap<>(values.size());
        values.forEach((kye0,value0) -> {
            if (null != value0) {
                String[] newValue0 = new String[value0.length];
                for (int i = 0;i < value0.length;i ++) {
                    newValue0[i] = DefenceUtil.cleanXss(value0[i]);;
                }
                newValues.put(kye0,newValue0);
            } else {
                newValues.put(kye0,null);
            }
        });
        return newValues;
    }
}
