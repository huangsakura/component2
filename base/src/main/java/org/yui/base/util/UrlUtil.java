package org.yui.base.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.yui.base.constant.StringConstant;
import org.yui.base.exception.BusinessException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author huangjinlong
 * url
 */
@Log4j2
public abstract class UrlUtil {

    /**
     * url解码
     * @param data
     * @return
     */
    public static String decode(String data) {
        if (StringUtils.isBlank(data)) {
            return data;
        }
        if (!data.contains(StringConstant.PERCENT)) {
            return data;
        }
        try {
            return URLDecoder.decode(data, StringConstant.UTF_8);
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
            log.error("URL解码出错:{}",e.getMessage());
            log.error("待解码的字符串为:{}",data);
            throw new BusinessException("URL_DECODE_ERROR","URL解码失败",false);
        }
    }

    /**
     * url编码
     * @param data
     * @return
     */
    public static String encode(String data) {
        if (null == data) {
            return null;
        }
        try {
            return URLEncoder.encode(data,StringConstant.UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.error("URL编码出错:{}",e);
            throw new BusinessException("URL_ENCODE_ERROR","URL编码失败",false);
        }
    }

    public static void main(String[] args) {
        System.out.println(encode("黄金龙%"));
    }
}
