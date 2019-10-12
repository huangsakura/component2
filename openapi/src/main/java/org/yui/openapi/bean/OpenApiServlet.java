package org.yui.openapi.bean;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.yui.base.bean.api.JsonResult;
import org.yui.base.bean.constant.StringConstant;
import org.yui.base.exception.BusinessException;
import org.yui.base.util.ValidationUtil;
import org.yui.filterchain.bean.filter.RequestInfoHolder;
import org.yui.tomcat.util.ServletRequestUtil;
import org.yui.tomcat.util.ServletResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author huangjinlong
 * @time 2019-10-11 19:57
 * @description
 */
@Log4j2
public class OpenApiServlet extends HttpServlet {

    public static final String PATH = "/api/*";

    private Set<OpenApiInfo> openApiInfoSet;

    public OpenApiServlet(Set<OpenApiInfo> openApiInfoSet) {
        this.openApiInfoSet = openApiInfoSet;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonResult jsonResult = new JsonResult();
        try {
            String uri = RequestInfoHolder.getUri();
            if (StringUtils.isBlank(uri)) {
                throw new BusinessException(JsonResult.FAIL,"获取URI失败",false);
            }
            String[] uris = uri.split(StringConstant.SLASH);
            if (ArrayUtils.isEmpty(uris)) {
                throw new BusinessException(JsonResult.FAIL,"URI格式不满足要求",false);
            }
            String openApiName = uris[uris.length - 1];
            String body = ServletRequestUtil.getStringFromBody(req);
            if (StringUtils.isBlank(body)) {
                throw new BusinessException(JsonResult.FAIL,"获取参数失败",false);
            }

            AbstractOpenApiResponse abstractOpenApiResponse = null;
            boolean exist = false;
            try {
                for (OpenApiInfo openApiInfo : openApiInfoSet) {
                    if (openApiName.equals(openApiInfo.getName())) {
                        exist = true;
                        AbstractOpenApiRequest abstractOpenApiRequest = JSON.parseObject(body,openApiInfo.getRequest());
                        ValidationUtil.validate(abstractOpenApiRequest);
                        abstractOpenApiResponse = openApiInfo.getResponse().newInstance();
                        openApiInfo.getOpenApi().doService(abstractOpenApiRequest,abstractOpenApiResponse);
                        break;
                    }
                }
            } catch (IllegalAccessException | InstantiationException e) {
                log.error("创建openApi的response对象出错:{}",e.getMessage());
                throw new BusinessException(JsonResult.FAIL,JsonResult.FAIL_MESSAGE,false);
            }
            if (!exist) {
                throw new BusinessException("NOT_EXIST_OPEN_API","OPEN_API不存在",false);
            }
            jsonResult.setCode(JsonResult.SUCCESS);
            jsonResult.setMessage(JsonResult.SUCCESS_MESSAGE);
            jsonResult.setContent(abstractOpenApiResponse);
        } catch (BusinessException e) {
            jsonResult.setCode(e.getCode());
            jsonResult.setMessage(e.getMessage());
        }
        ServletResponseUtil.write(resp,jsonResult);
    }
}
