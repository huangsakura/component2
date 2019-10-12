package org.yui.openapi.bean;

/**
 * @author huangjinlong
 * @time 2019-10-11 19:53
 * @description
 */
public abstract class AbstractOpenApi<T extends AbstractOpenApiRequest,E extends AbstractOpenApiResponse> {

    public abstract void doService(T t,E e);
}
