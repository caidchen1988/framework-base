package com.zwt.framework.utils.util.java8.test10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zwt
 * @detail
 * @date 2018/10/12
 * @since 1.0
 */
public class MyTest {
    //给定一个列表List<Value>,比如{1,4,9}，构造一个List<List<Integer>>,它的成员都是类表{1,4,9}的子集。

    static List<List<Integer>> subSets(List<Integer> list){
        if(list.isEmpty()){
            List<List<Integer>> ans=new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }

        Integer first=list.get(0);
        List<Integer> rest=list.subList(1,list.size());

        List<List<Integer>> subans=subSets(rest);
        List<List<Integer>> subans2=insertAll(first,subans);
        return concat(subans,subans2);
    }

    static List<List<Integer>> insertAll(Integer first,List<List<Integer>> lists){
        List<List<Integer>> result=new ArrayList<>();
        for(List<Integer> list:lists){
            List<Integer> copyList=new ArrayList<>();
            copyList.add(first);
            copyList.addAll(list);
            result.add(copyList);
        }
        return result;
    }

    static List<List<Integer>> concat(List<List<Integer>> a,List<List<Integer>> b){
        List<List<Integer>> r=new ArrayList<>(a);
        r.addAll(b);
        return r;
    }
}
