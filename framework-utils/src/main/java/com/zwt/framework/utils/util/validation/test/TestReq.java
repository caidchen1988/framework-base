package com.zwt.framework.utils.util.validation.test;


import com.zwt.framework.utils.util.validation.annotation.DataValid;
import com.zwt.framework.utils.util.validation.constant.RegexType;
import com.zwt.framework.utils.util.validation.constant.ReturnCodeEnum;

import java.util.List;
import java.util.Map;

/**
 * @author zwt
 * @detail
 * @date 2019/8/5
 * @since 1.0
 */
public class TestReq {

    @DataValid(nullable = false,regexType = RegexType.CHINESE,throwMsg = ReturnCodeEnum.PARAMER_ERROR,maxLength = 100,description = "用户名")
    private String userName;

    @DataValid(nullable = false,regexType = RegexType.NUMBER,throwMsg = ReturnCodeEnum.SYSTEM_ERROR,description = "年龄")
    private int age;

    @DataValid(nullable = false,regexType = RegexType.EMOJI_REPLACE,description = "表情符号")
    private String emoji;

    @DataValid(nullable = false,throwMsg = ReturnCodeEnum.PARAMER_ERROR,minLength = 2,maxLength = 16,description = "测试数组")
    private List<TestHelpReq> testHelpReqList;

    @DataValid(nullable = false,throwMsg = ReturnCodeEnum.PARAMER_ERROR,minLength = 2,maxLength = 16,description = "测试集合")
    private Map<String,TestHelpReq> testHelpReqMap;

    @DataValid(nullable = false,throwMsg = ReturnCodeEnum.PARAMER_ERROR,description = "测试对象")
    private TestHelpReq testHelpReq;


    public TestHelpReq getTestHelpReq() {
        return testHelpReq;
    }

    public void setTestHelpReq(TestHelpReq testHelpReq) {
        this.testHelpReq = testHelpReq;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public List<TestHelpReq> getTestHelpReqList() {
        return testHelpReqList;
    }

    public void setTestHelpReqList(List<TestHelpReq> testHelpReqList) {
        this.testHelpReqList = testHelpReqList;
    }

    public Map<String, TestHelpReq> getTestHelpReqMap() {
        return testHelpReqMap;
    }

    public void setTestHelpReqMap(Map<String, TestHelpReq> testHelpReqMap) {
        this.testHelpReqMap = testHelpReqMap;
    }
}
