package org.yui.base.util;

import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.hibernate.validator.constraints.NotBlank;
import org.yui.base.bean.constant.StringConstant;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjinlong
 * http工具类，使用okhttp3
 */
@Log4j2
public abstract class HttpUtil {

    /**
     * 创建okhhtp的客户端
     * @return
     */
    private static OkHttpClient generateClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 发出请求
     * @param request
     * @return
     */
    @Nullable
    private static String sendRequestQuietly(@NotNull Request request) {

        Response response = null;
        try {
            response = generateClient().newCall(request).execute();
        } catch (IOException e) {
            log.error("okhttp3请求抛出io异常1:{}",e.getMessage());
            return null;
        }
        ResponseBody responseBody = response.body();
        if (null == responseBody) {
            log.error("okhttp3请求的responseBody为空");
            return null;
        }
        try {
            return responseBody.string();
        } catch (IOException e) {
            log.error("okhttp3请求抛出io异常2:{}",e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param request
     * @return
     * @throws IOException
     */
    private static String sendRequest(@NotNull Request request) throws IOException {

        Response response = generateClient().newCall(request).execute();
        Optional<ResponseBody> responseBodyOptional = Optional.ofNullable(response.body());
        ResponseBody responseBody = responseBodyOptional.orElseThrow(() -> new IOException("post请求失败"));
        return responseBody.string();
    }

    /**
     * 使用okhttp3 以表单形式发出post请求
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    @Nullable
    public static String postFormQuietly(@NotBlank String url, Map<String,Object> paramMap, Map<String,Object> headerMap) {

        Request.Builder builder = packagePost(url,paramMap,headerMap);

        return sendRequestQuietly(builder.build());
    }

    /**
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     */
    @Nullable
    public static String putFormQuietly(@NotBlank String url, Map<String,Object> paramMap, Map<String,Object> headerMap) {
        Request.Builder builder = packagePut(url,paramMap,headerMap);
        return sendRequestQuietly(builder.build());
    }

    /**
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     */
    @Nullable
    public static String deleteFormQuietly(@NotBlank String url, Map<String,Object> paramMap, Map<String,Object> headerMap) {
        Request.Builder builder = packageDelete(url,paramMap,headerMap);
        return sendRequestQuietly(builder.build());
    }

    /**
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static String postForm(@NotBlank String url, Map<String,Object> paramMap, Map<String,Object> headerMap) throws IOException {
        Request.Builder builder = packagePost(url,paramMap,headerMap);
        return sendRequest(builder.build());
    }

    /**
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     */
    private static Request.Builder packagePut(@NotBlank String url,Map<String,Object> paramMap,Map<String,Object> headerMap) {
        /**
         * 组装表单数据
         */
        FormBody formBody = packageFormBody(paramMap);
        /**
         * 配置 url，
         */
        Request.Builder requestBuilder = new Request.Builder().url(url).put(formBody);
        /**
         * 组装header
         */
        addHeader(requestBuilder,headerMap);
        return requestBuilder;
    }

    /**
     *
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     */
    private static Request.Builder packageDelete(@NotBlank String url,Map<String,Object> paramMap,Map<String,Object> headerMap) {
        /**
         * 组装表单数据
         */
        FormBody formBody = packageFormBody(paramMap);
        /**
         * 配置 url，
         */
        Request.Builder requestBuilder = new Request.Builder().url(url).delete(formBody);
        /**
         * 组装header
         */
        addHeader(requestBuilder,headerMap);
        return requestBuilder;
    }

    /**
     *
     * @param paramMap
     * @param headerMap
     * @return
     */
    private static Request.Builder packagePost(@NotBlank String url,Map<String,Object> paramMap,Map<String,Object> headerMap) {
        /**
         * 组装表单数据
         */
        FormBody formBody = packageFormBody(paramMap);
        /**
         * 配置 url，
         */
        Request.Builder requestBuilder = new Request.Builder().url(url).post(formBody);
        /**
         * 组装header
         */
        addHeader(requestBuilder,headerMap);
        return requestBuilder;
    }

    /**
     *
     * @param paramMap
     * @return
     */
    private static FormBody packageFormBody(Map<String,Object> paramMap) {
        /**
         * 组装表单数据
         */
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (null != paramMap) {
            paramMap.forEach((x,y) -> {
                formBodyBuilder.add(x,Optional.ofNullable(y).map(Object::toString).orElse(StringConstant.BLANK));
            });
        }
        return formBodyBuilder.build();
    }

    /**
     *
     * @param requestBuilder
     * @param headerMap
     * @return
     */
    private static void addHeader(@NotNull Request.Builder requestBuilder, Map<String,Object> headerMap) {
        if (null != headerMap) {
            headerMap.forEach((x,y) -> {
                requestBuilder.header(x,Optional.ofNullable(y).map(Object::toString).orElse(StringConstant.BLANK));
            });
        }
    }

    /**
     * 同步 GET 请求
     * @param url
     * @return
     */
    @Nullable
    public static String get(@NotBlank String url,Map<String,String> headerMap) {

        Request.Builder requestBuilder = new Request.Builder().url(url).get();
        /**
         * 配置header
         */
        if (null != headerMap) {
            headerMap.forEach(requestBuilder::header);
        }

        return sendRequestQuietly(requestBuilder.build());
    }

    /**
     *
     * @param url
     * @param object
     * @param headerMap
     * @return
     */
    @Nullable
    public static String postJson(String url,Object object,Map<String,String> headerMap) {

        RequestBody requestBody = FormBody.create(
                MediaType.parse("application/json; charset=utf-8"), JsonUtil.toJsonString(object));

        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        /**
         * 配置header
         */
        if (null != headerMap) {
            headerMap.forEach(requestBuilder::header);
        }

        return sendRequestQuietly(requestBuilder.build());
    }

    public static void main(String[] a) {
        System.out.println(get("https://www.baidu.com",null));
    }
}
