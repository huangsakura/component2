package org.yui.base.bean.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.yui.base.bean.entity.Entitys;
import org.yui.base.enums.entity.StateEnum;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huangjinlong
 * 创建实体类时，都应该有这五个字段，都应该继承自这个类
 */
@Getter
@Setter
public abstract class AbstractBaseEntity implements Serializable, Entitys {

    /**
     * 创建时间
     */
    @NotNull
    @Column(name = "create_time",nullable = false)
    private LocalDateTime createTime;

    /**
     * 状态
     */
    @NotNull
    @Column(name = "state",nullable = false,length = 16)
    private StateEnum state;

    /**
     * 最后一次更新时间
     */
    @Nullable
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Length(max = 255)
    @Nullable
    @Column(name = "memo")
    private String memo;
}
