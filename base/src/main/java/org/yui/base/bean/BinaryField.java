package org.yui.base.bean;

import org.yui.base.enums.BinaryEnum;

import java.util.Set;

/**
 * @author huangjinlong
 * @time 2018-12-20 09:53
 * @description
 */
public interface BinaryField<T extends Enum<T> & BinaryEnum> {

    void setBinaryEnum(Set<T> binaryEnum);

    Set<T> getBinaryEnum();

    String GET_BINARY_ENUM = "getBinaryEnum";
}
