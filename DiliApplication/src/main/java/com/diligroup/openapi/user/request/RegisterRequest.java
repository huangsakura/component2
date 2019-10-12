package com.diligroup.openapi.user.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.yui.openapi.bean.AbstractOpenApiRequest;

/**
 * @author huangjinlong
 * @time 2019-10-12 13:01
 * @description
 */
@Setter
@Getter
public class RegisterRequest extends AbstractOpenApiRequest {

    @NotBlank
    @Email(regexp = "^.+@.*\\.com$")
    private String email;
}
