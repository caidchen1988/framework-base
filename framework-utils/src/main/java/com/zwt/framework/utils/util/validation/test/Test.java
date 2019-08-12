package com.zwt.framework.utils.util.validation.test;

import com.zwt.framework.utils.util.validation.ValidateUtils;

import java.util.*;

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
        testHelpReq3.setArg1("1234");
        testHelpReq3.setArg2(null);

        List<TestHelpReq> list = new ArrayList<>();
        list.add(testHelpReq1);
        list.add(testHelpReq2);

        Map<String,TestHelpReq> map = new HashMap<>();
        map.put("1",testHelpReq1);
        map.put("2",testHelpReq2);

        Set<TestHelpReq> set = new HashSet<>();
        set.add(testHelpReq1);
        set.add(testHelpReq2);

        String [] strings = new String[]{"1","2"};
        int[] ints = new int[]{1,2,3};
        Integer[] integers = new Integer[]{1,2};
        TestHelpReq[] testHelpReqs = new TestHelpReq[]{testHelpReq1,testHelpReq3};

        TestReq testReq = new TestReq();
        testReq.setUserName("李四");
        testReq.setAge(25);
        testReq.setEmoji("Langga Quh\uD83D\uDE18\uD83D\uDE0D\uD83D\uDC9E");
        testReq.setTestHelpReqList(list);
        testReq.setTestHelpReqMap(map);
        testReq.setTestHelpReq(testHelpReq1);
        testReq.setTestHelpReqSet(set);
        testReq.setStrings(strings);
        testReq.setIntegers(integers);
        testReq.setInts(ints);
        testReq.setTestHelpReqs(testHelpReqs);

        ValidateUtils.valid(testReq);
    }
}
