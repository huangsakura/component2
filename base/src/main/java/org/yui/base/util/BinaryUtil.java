package org.yui.base.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.yui.base.bean.constant.StringConstant;
import org.yui.base.enums.BinaryEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 二进制工具类
 */
public abstract class BinaryUtil {

    /**
     * 这个方法的作用是 按二进制的规则拆分 一个十进制的数
     *
     * 比如 把 7 拆分为  1,2,4
     * 把 5 拆分为 1,4
     *
     * @return
     */
    public static String separateDecimal2BinaryString(int number) {

        List<Integer> integerList = separateDecimal2BinaryList(number);
        return StringUtils.join(integerList, StringConstant.GENERAL_COMMA_SPLIT);
    }

    /**
     * 
     * @param number
     * @return
     */
    public static List<Integer> separateDecimal2BinaryList(int number) {

        Validate.isTrue(number >= 0,"参数必须大于0");
        String binaryString = Integer.toBinaryString(number);
        int length = binaryString.length();

        List<Integer> integerList = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            int result = number & (0x01 << i);
            if (result != 0) {
                integerList.add(result);
            }
        }
        return integerList;
    }

    /**
     * 根据 BinaryEnumIFace 列表 计算他们的二进制之和
     * @param binaryEnumIFaceSet
     * @return
     */
    public static int calculateBinaryBySet(Set<BinaryEnum> binaryEnumIFaceSet) {

        int result = 0;
        if (null != binaryEnumIFaceSet) {
            for (BinaryEnum binaryEnum : binaryEnumIFaceSet) {
                result += binaryEnum.getBinaryNumber();
            }
        }
        return result;
    }

    public static void main(String[] a) {
        System.out.println(separateDecimal2BinaryString(0));
        //System.out.println(Integer.toBinaryString(7));
    }
}
