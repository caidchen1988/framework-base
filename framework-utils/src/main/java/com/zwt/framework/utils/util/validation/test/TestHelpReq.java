package com.zwt.framework.utils.util.validation.test;


import com.zwt.framework.utils.util.validation.annotation.DataValid;
import com.zwt.framework.utils.util.validation.constant.RegexType;
import com.zwt.framework.utils.util.validation.constant.ReturnCodeEnum;

/**
 * @author zwt
 * @detail
 * @date 2019/8/5
 * @since 1.0
 */
public class TestHelpReq {

    @DataValid(nullable = false,regexExpression = "\\d{4}$",throwMsg = ReturnCodeEnum.SYSTEM_ERROR,description = "自定义正则测试")
    private String arg1;

    @DataValid(nullable = true,regexType = RegexType.CHINESE,description = "允许为null，如果不为null，应该满足指定正则")
    private String arg2;



    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }
}
