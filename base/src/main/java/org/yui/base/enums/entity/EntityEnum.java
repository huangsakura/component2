package org.yui.base.enums.entity;

import org.yui.base.enums.Enums;

/**
 * 如果自定义了一个枚举，这个枚举是用来映射数据库的码值，
 * 则这个枚举，必须实现这个接口
 *
 * 如果一个枚举不被用到实体类里面去，则一定不要实现这个接口，
 * 请实现 com.yunhuakeji.component.base.enums.iface.EnumIFace 接口
 */
public interface EntityEnum extends Enums {

    String getDbCode();
}
