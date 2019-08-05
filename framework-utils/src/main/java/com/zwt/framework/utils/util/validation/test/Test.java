package com.zwt.framework.utils.util.validation.test;

import com.zwt.framework.utils.util.validation.ValidateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zwt
 * @detail
 * @date 2019/8/5
 * @since 1.0
 */
public class Test {
    public static void main(String[] args) throws Exception{
        TestHelpReq testHelpReq1 = new TestHelpReq();
        testHelpReq1.setArg1("0123");
        testHelpReq1.setArg2("张三");
        TestHelpReq testHelpReq2 = new TestHelpReq();
        testHelpReq2.setArg1("2345");
        testHelpReq2.setArg2(null);

        TestHelpReq testHelpReq3 = new TestHelpReq();
        testHelpReq2.setArg1("2345");
        testHelpReq2.setArg2("zzz");


        List<TestHelpReq> list = new ArrayList<>();
        list.add(testHelpReq1);
        list.add(testHelpReq2);

        Map<String,TestHelpReq> map = new HashMap<>();
        map.put("1",testHelpReq1);
        map.put("2",testHelpReq2);

        TestReq testReq = new TestReq();
        testReq.setUserName("李四");
        testReq.setAge(25);
        testReq.setEmoji("Langga Quh\uD83D\uDE18\uD83D\uDE0D\uD83D\uDC9E");
        testReq.setTestHelpReqList(list);
        testReq.setTestHelpReqMap(map);
        testReq.setTestHelpReq(testHelpReq3);

        ValidateUtils.valid(testReq);
    }
}
