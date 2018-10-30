package com.zwt.framework.utils.util.java8.test11;

import com.zwt.framework.utils.util.java8.test2.Dish;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author zwt
 * @detail
 * @date 2018/10/15
 * @since 1.0
 */
public class MyTest {
    static final List<Dish> menu= Arrays.asList(
            new Dish("pork",false,800,Dish.Type.MEAT),
            new Dish("beef",false,700,Dish.Type.MEAT),
            new Dish("chicken",false,400,Dish.Type.MEAT),
            new Dish("french fries",true,530,Dish.Type.OTHER),
            new Dish("rice",true,350,Dish.Type.OTHER),
            new Dish("season fruit",true,120,Dish.Type.OTHER),
            new Dish("pizza",true,550,Dish.Type.OTHER),
            new Dish("prawns",false,300,Dish.Type.FISH),
            new Dish("salmon",false,450,Dish.Type.FISH));

    public static void main(String[] args) {
//        Stream<Dish> menuStream=menu.stream();
//        Results results=new StreamForker<Dish>(menuStream)
//                .fork("shortMenu",s->s.map(Dish::getName).collect(Collectors.joining(", ")))
//                .fork("totalCalories",s->s.mapToInt(Dish::getCalories).sum())
//                .fork("mostCaloricDish",s->s.collect(Collectors.reducing(
//                        (d1,d2)->d1.getCalories()>d2.getCalories()?d1:d2
//                )).get())
//                .fork("dishesByType",s->s.collect(Collectors.groupingBy(Dish::getType)))
//                .getResults();
//
//        String shortMenu=results.get("shortMenu");
//        int totalCalories=results.get("totalCalories");
//        Dish mostCaloricDish=results.get("mostCaloricDish");
//        Map<Dish.Type,List<Dish>> dishesByType=results.get("dishesByType");
//
//        System.out.println(shortMenu);
//        System.out.println(totalCalories);
//        System.out.println(mostCaloricDish);
//        System.out.println(dishesByType);

        //生成1到1000的数组
        List<Integer> list1=IntStream.rangeClosed(1,1000).filter(n->n%2==0).boxed().collect(Collectors.toList());
        List<Integer> list2=IntStream.rangeClosed(1,1000).filter(n->n%5==0).boxed().collect(Collectors.toList());
        //同时对list1数据求和，统计list1数据数量，统计list1和list2相同元素，统计list1和list2相同元素的最大值和最小值
        Results results=new StreamForker<Integer>(list1.stream())
                .fork("sum",s->s.mapToInt(Integer::intValue).sum())
                .fork("count",s->s.count())
                .fork("list3",s->s.flatMap(i->list2.stream().filter(j->i.equals(j))).collect(Collectors.toList()))
                .fork("max",s->s.flatMap(i->list2.stream().filter(j->i.equals(j))).max(Comparator.naturalOrder()))
                .fork("min",s->s.flatMap(i->list2.stream().filter(j->i.equals(j))).min(Comparator.naturalOrder()))
                .getResults();

        System.out.println("sum="+results.get("sum"));
        System.out.println("count="+results.get("count"));
        System.out.println("max="+((Optional) results.get("max")).get());
        System.out.println("min="+((Optional)results.get("min")).get());
        ((List<Integer>)results.get("list3")).stream().forEach(System.out::println);

    }
}
