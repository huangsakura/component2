package com.diligroup.openapi.user;

import com.diligroup.openapi.user.request.RegisterRequest;
import com.diligroup.openapi.user.response.RegisterResponse;
import org.yui.openapi.annotation.OpenApi;
import org.yui.openapi.bean.AbstractOpenApi;

/**
 * @author huangjinlong
 * @time 2019-10-11 19:51
 * @description
 */
@OpenApi("register")
public class RegisterApi extends AbstractOpenApi<RegisterRequest, RegisterResponse> {
    @Override
    public void doService(RegisterRequest registerRequest, RegisterResponse registerResponse) {
    }
}
