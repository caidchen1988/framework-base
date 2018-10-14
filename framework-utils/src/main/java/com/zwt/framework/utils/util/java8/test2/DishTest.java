package com.zwt.framework.utils.util.java8.test2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author zwt
 * @detail
 * @date 2018/10/9
 * @since 1.0
 */
public class DishTest {
    public enum CaloricLevel{
        DIET,NORMAL,FAT
    }
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

    static final List<String> words=Arrays.asList("Hello","World","Java8");

    static final List<Integer> nums=Arrays.asList(1,2,3,4,5,6,7,8,9,10);

    public static void main(String[] args) {
        //找出三个高热量菜肴的名字
        List<String> threeHighCaloricDishNames=
                menu.stream().filter(dish -> dish.getCalories()>300).map(Dish::getName).limit(3).collect(Collectors.toList());
        System.out.println(threeHighCaloricDishNames);


        //找出除去前两个的高热量的菜肴
        List<Dish> dishes=menu.stream().filter(dish -> dish.getCalories()>300).skip(2).collect(Collectors.toList());


        //找出前两个荤菜
        List<Dish> dishes1=menu.stream().filter(dish -> dish.getType()== Dish.Type.MEAT).limit(2).collect(Collectors.toList());
        System.out.println(dishes1);

        //每道菜名字的长度
        List<Integer> dishNameLengths=menu.stream().map(Dish::getName).map(String::length).collect(Collectors.toList());

        //列出不相同的字符
        List<String> uniqueCharacters=words.stream().map(w->w.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
        System.out.println(uniqueCharacters);

        //返回一组数的平方列表
        List<Integer> squareNums=nums.stream().map(n->n*n).collect(Collectors.toList());
        System.out.println(squareNums);

        //给定两个数字列表，返回所有数对
        List<Integer> numbers1=Arrays.asList(1,2,3);
        List<Integer> numbers2=Arrays.asList(3,4);
        List<int[]> pairs=numbers1.stream().flatMap(i->numbers2.stream().map(j->new int[]{i,j})).collect(Collectors.toList());

        //返回总和能被3整除的数对
        List<int[]> pairs1=numbers1.
                stream().
                flatMap(i->numbers2.stream().
                        filter(j->(i+j)%3==0).
                        map(j->new int[]{i,j})).
                collect(Collectors.toList());


        //检查是否有素食可以选择
        if(menu.stream().anyMatch(Dish::isVegetarian)){
            System.out.println("有素食可以选择");
        }

        //检查菜品是否健康
        boolean isHealthy1=menu.stream().allMatch(dish -> dish.getCalories()<1000);
        boolean isHealthy2=menu.stream().noneMatch(dish -> dish.getCalories()>=1000);

        //随便找一道素食
        Optional<Dish> dish0=menu.stream().filter(Dish::isVegetarian).findAny();

        //查找第一道素食
        Optional<Dish> dish1=menu.stream().filter(Dish::isVegetarian).findFirst();

        //计算菜单菜谱的总卡路里
        int allCalories=menu.stream().map(dish -> dish.getCalories()).reduce(0,Integer::sum);
        System.out.println(allCalories);

        //查找热量最高的食物
        Optional<Dish> highestCalories=menu.stream().reduce(
                (d1,d2)->{
                    if(d1.getCalories()>=d2.getCalories()){
                        return d1;
                    }
                    return d2;
                });
        highestCalories.ifPresent(System.out::println);

        //计算有多少到不重复的菜肴
        long dishCount=menu.stream().distinct().count();

        //计算卡路里之和
        int calories=menu.stream().mapToInt(Dish::getCalories).sum();

        //获取最大卡路里菜肴
        OptionalInt maxCalories=menu.stream().mapToInt(Dish::getCalories).max();
        maxCalories.orElse(0);

        //数值范围 生成一个从1到100的偶数流
        IntStream evenNumbers=IntStream.rangeClosed(1,100).filter(n->n%2==0);
        System.out.println(evenNumbers.count());

        //生成勾股数流
        Stream<int[]> pythagoreanTriples=IntStream.rangeClosed(1,100).boxed().
                flatMap(a->IntStream.rangeClosed(a,100).
                        filter(b->Math.sqrt(a*a+b*b)%1==0).
                        mapToObj(b->new int[]{a,b,(int)Math.sqrt(a*a+b*b)}));
        pythagoreanTriples.forEach(i->System.out.println("["+i[0]+","+i[1]+","+i[2]+"]"));

        Stream<double[]> pythagoreanTriples2=IntStream.rangeClosed(1,100).boxed().
                flatMap(a->IntStream.rangeClosed(a,100).
                        mapToObj(b->new double[]{a,b,Math.sqrt(a*a+b*b)}).
                        filter(t->t[2]%1==0));


        //由值创建流
        Stream<String> stringStream=Stream.of("Hello","World","Java8");
        stringStream.map(String::toUpperCase).forEach(System.out::println);
        //创建一个空流
        Stream<String> emptyStream=Stream.empty();
        //由数组创建流
        int [] numbers={2,3,5,7,11,13};
        int sum=Arrays.stream(numbers).sum();

        //由文件生成流
        long uniqueWords=0;
        try(Stream<String> lines= Files.lines(Paths.get("data.txt"), Charset.defaultCharset())){
            uniqueWords=lines.flatMap(line->Arrays.stream(line.split(" "))).distinct().count();
        }catch (IOException e){
            System.out.println(e);
        }

        //创建无限流
        Stream.iterate(0,n->n+2).limit(10).forEach(System.out::println);

        //斐波那契数列
        Stream.iterate(new int[]{0,1},t->new int[]{t[1],t[0]+t[1]}).limit(20).map(t->t[0]).forEach(t->System.out.print(t+","));

        //生成若干个随机数流
        Stream.generate(Math::random).limit(5).forEach(System.out::println);

        //收集器
        //有多少菜肴
        long howManyDishes=menu.stream().collect(Collectors.counting());
        //最大的卡路里
        Optional<Dish> mostCalorieDish=menu.stream().collect(Collectors.maxBy(Comparator.comparing(Dish::getCalories)));
        //卡路里之和
        int totalCarlories=menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        //平均卡路里
        double avgCalories=menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
        //菜肴卡路里分析
        IntSummaryStatistics menuStatistics=menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println(menuStatistics);
        //打印菜肴名称
        String shortMenu=menu.stream().map(Dish::getName).collect(Collectors.joining(","));
        System.out.println(shortMenu);

        //卡路里之和
        int totalCarlories2=menu.stream().collect(Collectors.reducing(0,Dish::getCalories,(i,j)->i+j));

        //把菜肴按照类型进行分类
        Map<Dish.Type,List<Dish>> dishesByType=menu.stream().collect(Collectors.groupingBy(Dish::getType));

        //把菜肴按照热量进行分类（低热量<400 普通400-700 高热量>700）
        Map<DishTest.CaloricLevel,List<Dish>> dishesByCaloric=menu.stream().collect(Collectors.groupingBy(dish->{
            if(dish.getCalories()<400) return CaloricLevel.DIET;
            else if(dish.getCalories()>700) return CaloricLevel.FAT;
            return CaloricLevel.NORMAL;
        }));

        //多级分组
        Map<Dish.Type,Map<CaloricLevel,List<Dish>>> dishesByTypeCaloric=menu.stream().
                collect(Collectors.groupingBy(Dish::getType,
                        Collectors.groupingBy(dish->{
                            if(dish.getCalories()<400) return CaloricLevel.DIET;
                            else if(dish.getCalories()>700) return CaloricLevel.FAT;
                            return CaloricLevel.NORMAL;
                        })));

        //数一数菜单中每类菜有多少个
        Map<Dish.Type,Long> typesCount=menu.stream().collect(Collectors.groupingBy(Dish::getType,Collectors.counting()));
        //按菜单类型查找菜单中热量最高的菜肴
        Map<Dish.Type,Optional<Dish>> mostCaloricByType=menu.stream().
                collect(Collectors.groupingBy(Dish::getType,Collectors.maxBy(Comparator.comparing(Dish::getCalories))));

        //查找每个子组中热量最高的Dish
        Map<Dish.Type,Dish> mostCaloricByType2=menu.stream().collect(Collectors.groupingBy(Dish::getType,Collectors.collectingAndThen(
                Collectors.maxBy(Comparator.comparing(Dish::getCalories)),Optional::get
        )));

        Map<Dish.Type,Set<CaloricLevel>> caloricLevelsByType=menu.stream().collect(
                Collectors.groupingBy(Dish::getType,Collectors.mapping(
                        dish->{
                            if(dish.getCalories()<400) return CaloricLevel.DIET;
                            else if(dish.getCalories()>700) return CaloricLevel.FAT;
                            return CaloricLevel.NORMAL;
                        },Collectors.toCollection(HashSet::new)
                ))
        );

        //分区区分是不是素食
        Map<Boolean,List<Dish>> partitionedMenu=menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian));

        //找到素食和非素食中热量最高的菜肴
        Map<Boolean,Dish> mostCaloricPartitionedByVegetarian=
                menu.stream().collect(Collectors.partitioningBy(
                        Dish::isVegetarian,Collectors.collectingAndThen(
                                Collectors.maxBy(
                                        Comparator.comparingInt(Dish::getCalories)
                                ),Optional::get)));



    }

}
