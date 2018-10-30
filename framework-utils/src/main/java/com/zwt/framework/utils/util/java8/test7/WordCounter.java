package com.zwt.framework.utils.util.java8.test7;

/**
 * @author zwt
 * @detail
 * @date 2018/10/10
 * @since 1.0
 */
public class WordCounter {
    private final int counter;
    private final boolean lastSpace;

    public WordCounter(int counter, boolean lastSpace) {
        this.counter = counter;
        this.lastSpace = lastSpace;
    }

    //遍历一个个的Character
    public WordCounter accumulate(Character c){
        if(Character.isWhitespace(c)){
            return lastSpace ? this : new WordCounter(counter,true);
        }else{
            //上一个字符是空格，而当前遍历的字符不是空格时，将单词计数器加一
            return lastSpace ? new WordCounter(counter+1,false):this;
        }
    }
    //合并两个WordCounter，将其计数器加起来
    public WordCounter combine(WordCounter wordCounter){
        return new WordCounter(counter+wordCounter.counter,wordCounter.lastSpace);
    }

    public int getCounter(){
        return counter;
    }
}
