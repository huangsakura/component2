package org.yui.openapi.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * @author huangjinlong
 * @time 2019-10-11 20:04
 * @description
 */
@Setter
@Getter
public class OpenApiInfo {

    private String name;

    private AbstractOpenApi openApi;

    private Class<? extends AbstractOpenApiRequest> request;

    private Class<? extends AbstractOpenApiResponse> response;

    @Override
    public int hashCode() {
        return Optional.ofNullable(this.name)
                .map(String::hashCode).orElse(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OpenApiInfo)) {
            return false;
        }
        OpenApiInfo openApiInfo0 = (OpenApiInfo)obj;

        if (this.name == null) {
            return openApiInfo0.getName() == null;
        }
        return this.name.equals(openApiInfo0.getName());
    }
}
