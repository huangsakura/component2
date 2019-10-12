package com.diligroup.bean.entity;

import com.sun.javafx.beans.IDProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.yui.base.annotation.entity.Code;
import org.yui.base.bean.entity.AbstractBaseEntity;

import javax.annotation.Nullable;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author huangjinlong
 * @time 2019-10-12 13:21
 * @description
 */
@Setter
@Getter
@ToString
@Table(name = "b_user")
public class User extends AbstractBaseEntity {

    @Id
    private String id;

    @Code
    private String userCode;

    @NotBlank
    private String email;

    @Nullable
    private String mobileNumber;
}
