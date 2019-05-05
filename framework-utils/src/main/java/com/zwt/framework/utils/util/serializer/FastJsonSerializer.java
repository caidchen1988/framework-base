package com.zwt.framework.utils.util.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;

/**
 * @author zwt
 * @detail 使用FastJson实现序列化
 * @date 2019/5/5
 * @since 1.0
 */
public class FastJsonSerializer implements Serializer{
    @Override
    public String name() {
        return "FastJson";
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        return JSON.toJSONString(obj, SerializerFeature.WriteClassName).getBytes();
    }

    @Override
    public Object deserialize(byte[] bytes) throws IOException{
        return JSON.parse(new String(bytes), Feature.SupportAutoType);
    }
}
