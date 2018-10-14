package com.zwt.framework.utils.util.java8.test3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zwt
 * @detail
 * @date 2018/10/9
 * @since 1.0
 */
public class MyTest {
    static Trader raoul=new Trader("Raoul","Cambridge");
    static Trader mario=new Trader("Mario","Milan");
    static Trader alan=new Trader("Alan","Cambridge");
    static Trader brian=new Trader("Brian","Cambridge");

    static final List<Transaction> transactions= Arrays.asList(
            new Transaction(brian,2011,300),
            new Transaction(raoul,2012,1000),
            new Transaction(raoul,2011,400),
            new Transaction(mario,2012,710),
            new Transaction(mario,2012,700),
            new Transaction(alan,2012,950)
    );

    public static void main(String[] args) {
        //(1)找出2011年发生的所有交易，并按交易额排序（从低到高）。
        List<Transaction> list1=transactions.stream().
                filter(transaction -> transaction.getYear()==2011).
                sorted(Comparator.comparing(transaction -> transaction.getValue())).
                collect(Collectors.toList());
        list1.stream().forEach(System.out::println);

        //(2)交易员都在哪些不同的城市工作过？
        List<String> list2=transactions.stream().
                map(transaction -> transaction.getTrader().getCity()).
                distinct().
                collect(Collectors.toList());
        System.out.println(list2);

        //(3)查找所有来自于剑桥的交易员，并按照姓名排序。
        List<Trader> list3=transactions.stream().
                map(transaction -> transaction.getTrader()).
                filter(trader -> "Cambridge".equals(trader.getCity())).
                sorted(Comparator.comparing(trader -> trader.getName())).
                distinct().
                collect(Collectors.toList());
        System.out.println(list3);

        //(4)返回所有交易员的姓名字符串，按字母顺序排序。
        String list4=transactions.stream().
                map(transaction -> transaction.getTrader().getName()).
                sorted().
                distinct().
                collect(Collectors.joining());
        System.out.println(list4);

        //(5)有没有交易员是在米兰工作的？
        boolean list5=transactions.stream().
                map(transaction -> transaction.getTrader().getCity()).
                anyMatch(s -> "Milan".equals(s));
        System.out.println(list5);

        //(6)打印生活在剑桥的交易员的所有交易额。
        transactions.stream().
                filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity())).
                forEach(transaction -> System.out.println(transaction.getValue()));

        //(7)所有交易中，最高的交易额是多少？
        Optional<Transaction> maxValue=transactions.stream().
                max(Comparator.comparing(transaction -> transaction.getValue()));
        maxValue.ifPresent(transaction -> System.out.println(transaction.getValue()));

        //(8)找到交易额最小的交易。
        Optional<Transaction> minTransaction=transactions.stream().min(Comparator.comparing(transaction -> transaction.getValue()));
        minTransaction.ifPresent(System.out::println);


    }
}
