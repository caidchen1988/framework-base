/**
 *
 */
package com.zwt.framework.utils.util.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用 Kryo 实现序列化
 *
 * @author zwt
 */
public class KryoSerializer implements Serializer {
    private final static Kryo kryo = new Kryo();
    @Override
    public String name() {
        return "Kryo";
    }
    @Override
    public byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Output output = new Output(baos)) {
            kryo.register(obj.getClass());
            kryo.writeClassAndObject(output, obj);
            output.flush();
            return baos.toByteArray();

        }
    }
    @Override
    public Object deserialize(byte[] bits) throws IOException {
        if (bits == null || bits.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bits);
             Input ois = new Input(bais)
        ) {
            return kryo.readClassAndObject(ois);
        }
    }
}
