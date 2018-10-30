package com.zwt.framework.utils.util.java8.test11;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author zwt
 * @detail
 * @date 2018/10/15
 * @since 1.0
 */
public class StreamForker<T> {
    private final Stream<T> stream;
    private final Map<Object, Function<Stream<T>,?>> forks=new HashMap<>();

    public StreamForker(Stream<T> stream) {
        this.stream = stream;
    }

    public StreamForker<T> fork(Object key,Function<Stream<T>,?> f){
        //使用一个键对流上的函数进行索引
        forks.put(key,f);
        //返回this从而保证多次顺畅的调用fork方法
        return this;
    }

    public Results getResults(){
        ForkingStreamConsumer<T> consumer=build();
        try{
            stream.sequential().forEach(consumer);
        }finally {
            consumer.finish();
        }
        return consumer;
    }

    private Future<?> getOperationResult(List<BlockingQueue<T>> queues,Function<Stream<T>,?> f){
        //创建一个队列，并将其添加到队列的列表中
        BlockingQueue<T> queue=new LinkedBlockingDeque<>();
        queues.add(queue);
        //创建一个Spliterator，遍历队列中的元素
        Spliterator<T> spliterator=new BlockingQueueSpliterator<>(queue);
        //创建一个流，将Spliterator作为数据源
        Stream<T> source= StreamSupport.stream(spliterator,false);
        //创建一个Future对象，以异步方式计算在流上执行特定函数的结果
        return CompletableFuture.supplyAsync(()->f.apply(source));
    }

    private ForkingStreamConsumer<T> build(){

        //创建由队列组成的列表，每一个队列对应一个操作
        List<BlockingQueue<T>> queues=new ArrayList<>();

        //建立用于标识操作的键与包含操作结果的Future之间的映射关系
        Map<Object,Future<?>> actions=
                forks.entrySet().stream().reduce(
                        new HashMap<Object,Future<?>>(),
                        (map,e)->{
                            map.put(e.getKey(),getOperationResult(queues,e.getValue()));
                            return map;
                        },
                        (m1,m2)->{
                            m1.putAll(m2);
                            return m1;
                        }
                );
        return new ForkingStreamConsumer<>(queues,actions);
    }
}
