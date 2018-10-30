package com.zwt.framework.utils.util.java8.test11;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * @author zwt
 * @detail
 * @date 2018/10/15
 * @since 1.0
 */
public class ForkingStreamConsumer<T> implements Consumer<T>,Results {

    public static final Object END_OF_STREAM=new Object();

    private final List<BlockingQueue<T>> queues;
    private final Map<Object, Future<?>> actions;

    public ForkingStreamConsumer(List<BlockingQueue<T>> queues, Map<Object, Future<?>> actions) {
        this.queues = queues;
        this.actions = actions;
    }

    @Override
    public <R> R get(Object key) {
        try {
          return ((Future<R>)actions.get(key)).get();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(T t) {
        //将流中遍历的元素添加到所有的队列中
        queues.forEach(q->q.add(t));
    }

    void finish(){
        //将最后一个元素添加到队列中，表明该流已经结束
        accept((T)END_OF_STREAM);
    }
}
