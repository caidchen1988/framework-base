package com.zwt.framework.utils.util.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.IOException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * KryoPool实现序列化
 */
public class KryoPoolSerializer implements Serializer{
    /**
     * Kryo 的包装
     */
    private static class KryoHolder {
        private Kryo kryo;
        static final int BUFFER_SIZE = 1024;
        /**
         * reuse
         */
        private Output output = new Output(BUFFER_SIZE, -1);
        private Input input = new Input();
        KryoHolder(Kryo kryo) {
            this.kryo = kryo;
        }
    }

    interface KryoPool {
        /**
         * get o kryo object
         * @return KryoHolder instance
         */
        KryoHolder get();

        /**
         * return object
         * @param kryo holder
         */
        void offer(KryoHolder kryo);
    }

    /**
     * 由于kryo创建的代价相对较高 ，这里使用空间换时间
     * 对KryoHolder对象进行重用
     */
    public static class KryoPoolImpl implements KryoPool {
        /**
         * default is 1500
         * online server limit 3K
         */
        /**
         * thread safe list
         */
        private final Deque<KryoHolder> kryoHolderDeque=new ConcurrentLinkedDeque<KryoHolder>();

        private KryoPoolImpl() {
        }

        /**
         * @return KryoPool instance
         */
        public static KryoPool getInstance() {
            return Singleton.pool;
        }

        /**
         * get o KryoHolder object
         *
         * @return KryoHolder instance
         */
        @Override
        public KryoHolder get() {
            // Retrieves and removes the head of the queue represented by this table
            KryoHolder kryoHolder = kryoHolderDeque.pollFirst();
            return kryoHolder == null ? creatInstnce() : kryoHolder;
        }

        /**
         * create a new kryo object to application use
         * @return KryoHolder instance
         */
        public KryoHolder creatInstnce() {
            Kryo kryo = new Kryo();
            kryo.setReferences(false);
            return new KryoHolder(kryo);
        }

        /**
         * return object
         * Inserts the specified element at the tail of this queue.
         *
         * @param kryoHolder ...
         */
        @Override
        public void offer(KryoHolder kryoHolder) {
            kryoHolderDeque.addLast(kryoHolder);
        }

        /**
         * creat a Singleton
         */
        private static class Singleton {
            private static final KryoPool pool = new KryoPoolImpl();
        }
    }

    @Override
    public String name() {
        return "Kryo_Pool";
    }

    /**
     * Serialize object
     * @param obj what to serialize
     * @return return serialize data
     */
    @Override
    public byte[] serialize(Object obj) throws IOException {
        KryoHolder kryoHolder = null;
        if (obj == null){
            throw new RuntimeException("obj can not be null");
        }
        try {
            kryoHolder = KryoPoolImpl.getInstance().get();
            kryoHolder.kryo.register(obj.getClass());
            //reset Output    -->每次调用的时候  重置
            kryoHolder.output.reset();
            kryoHolder.kryo.writeClassAndObject(kryoHolder.output, obj);
            // 无法避免拷贝  ~~~
            return kryoHolder.output.toBytes();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Serialize obj exception");
        } finally {
            KryoPoolImpl.getInstance().offer(kryoHolder);
            //GC
            obj = null;
        }
    }

    /**
     * Deserialize data
     * @param bytes what to deserialize
     * @return object
     */
    @Override
    public Object deserialize(byte[] bytes) throws IOException {
        KryoHolder kryoHolder = null;
        if (bytes == null){
            throw new RuntimeException("bytes can not be null");
        }
        try {
            kryoHolder = KryoPoolImpl.getInstance().get();
            //call it ,and then use input object  ,discard any array
            kryoHolder.input.setBuffer(bytes, 0, bytes.length);
            return kryoHolder.kryo.readClassAndObject(kryoHolder.input);
        } catch (RuntimeException e) {
            throw new RuntimeException("Deserialize bytes exception");
        } finally {
            KryoPoolImpl.getInstance().offer(kryoHolder);
            //  for gc
            bytes = null;
        }
    }
}
