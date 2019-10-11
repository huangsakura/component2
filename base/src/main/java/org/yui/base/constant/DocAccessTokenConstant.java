package org.yui.base.constant;

/**
 * @author huangjinlong
 * doc模块的header名和对应的header值.
 *
 * 是这样的，虽然 swagger文档发出的请求会进行url编码，但是和
 * 实际工作中前端同事的url编码的方式不一样。（我个人始终怀疑是前端同事的编码有问题）。
 * swagger发出的请求全部都带上了这个header，这样web模块拿到swagger的请求就会执行另外的逻辑。
 *
 * 相关代码 com.yunhuakeji.component.doc.config.DocConfig#globalOperationParameters()
 * com.yunhuakeji.component.web.bean.resolver.UrlDecodeHandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
 */
public interface DocAccessTokenConstant {

    /**
     * doc 调用接口的时候，添加到header的name
     */
    String DOC_ACCESS_TOKEN_HEADER_NAME = "_doc_access_token";

    /**
     * doc 调用接口的时候，添加到header的默认值
     */
    String DOC_ACCESS_TOKEN_HEADER_DEFAULT_VALUE = "_doc_access_token_value";
}
