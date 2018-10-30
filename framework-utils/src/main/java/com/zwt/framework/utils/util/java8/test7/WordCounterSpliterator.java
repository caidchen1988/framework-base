package com.zwt.framework.utils.util.java8.test7;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author zwt
 * @detail
 * @date 2018/10/10
 * @since 1.0
 */
public class WordCounterSpliterator implements Spliterator<Character> {

    private final String string;
    private int currentChar=0;

    public WordCounterSpliterator(String string) {
        this.string = string;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        //处理当前字符
        action.accept(string.charAt(currentChar++));
        //如果还有字符要处理，返回true
        return currentChar<string.length();
    }

    @Override
    public Spliterator<Character> trySplit() {
        int currentSize=string.length()-currentChar;
        //返回null表示要处理的String已经足够小
        if(currentSize<10){
            return null;
        }
        //将试探拆分位置设定到要解析的文字中间
        for(int splitPos=currentSize/2+currentChar;splitPos<string.length();splitPos++){
            //如果是空格就开始拆分，不是空格将拆分位置前进直到下一个空格
            if(Character.isWhitespace(string.charAt(splitPos))){
                //创建一个新的WordCounterSpliterator来解析String从开始到拆分位置的部分
                Spliterator<Character> spliterator=
                        new WordCounterSpliterator(string.substring(currentChar,splitPos));
                //将这个WordCounterSpliterator的起始位置设为拆分位置
                currentChar=splitPos;
                return spliterator;
            }
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return string.length()-currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED+SIZED+SUBSIZED+NONNULL+IMMUTABLE;
    }
}
