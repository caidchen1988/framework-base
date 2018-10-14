package com.zwt.framework.utils.util.java8.test1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zwt
 * @detail
 * @date 2018/10/8
 * @since 1.0
 */
public class AppleFilter {

    //判断是不是绿苹果
    public static boolean isGreenApple(Apple apple){
        return "green".equals(apple.getColor());
    }
    //判断苹果够不够重
    public static boolean isHeavyApple(Apple apple){
        return apple.getWeight()>150;
    }
    //苹果过滤器
    static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p){
        List<Apple> result =new ArrayList<>();
        for(Apple apple:inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples =new ArrayList<>();
        Apple apple1=new Apple("green",160);
        Apple apple2=new Apple("red",140);
        Apple apple3=new Apple("red",150);
        apples.add(apple1);
        apples.add(apple2);
        apples.add(apple3);

        filterApples(apples,AppleFilter::isGreenApple);
        filterApples(apples,AppleFilter::isHeavyApple);

        //顺序处理
        List<Apple> heavyApples=apples.stream().filter(AppleFilter::isHeavyApple).collect(Collectors.toList());

        //并行处理
        List<Apple> testApples=apples.parallelStream().filter(AppleFilter::isGreenApple).filter(AppleFilter::isHeavyApple).collect(Collectors.toList());

    }
}
