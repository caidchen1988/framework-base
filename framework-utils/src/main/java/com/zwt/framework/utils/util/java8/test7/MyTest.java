package com.zwt.framework.utils.util.java8.test7;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author zwt
 * @detail
 * @date 2018/10/10
 * @since 1.0
 */
public class MyTest {
    static final String WORD="Hello World Happy EveryDay Good good study day day up let us study Spliterator";

    public static int countWords(Stream<Character> stream){
        WordCounter wordCounter=stream.reduce(new WordCounter(0,true),WordCounter::accumulate,WordCounter::combine);
        return wordCounter.getCounter();
    }

    public static void main(String[] args) {
        Spliterator<Character> spliterator=new WordCounterSpliterator(WORD);
        Stream<Character> stream= StreamSupport.stream(spliterator,true);
        System.out.println(countWords(stream));
//        Stream<Character> stream= IntStream.range(0,WORD.length()).mapToObj(WORD::charAt).parallel();
//        System.out.println(countWords(stream));
    }
}
