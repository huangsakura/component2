package org.yui.base.bean.api;

import org.yui.base.annotation.doc.ApiField;
import lombok.Getter;
import lombok.Setter;
import org.yui.base.enums.entity.ClientCategory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author huangjinlong
 */
@Getter
@Setter
public abstract class AbstractRequest implements Serializable {

    @NotNull
    @ApiField(desc = "客户端类型")
    private ClientCategory clientCategory;

    @ApiField(desc = "APP的版本号(仅APP端必传)")
    private String appVersion;
}
