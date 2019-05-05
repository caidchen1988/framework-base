package com.zwt.framework.utils.util.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author zwt
 * @detail 使用Jackson实现序列化
 * @date 2019/4/26
 * @since 1.0
 */
public class JacksonSerializer implements Serializer{

    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    public String name() {
        return "Jackson";
    }

    @Override
    public byte[] serialize(Object obj) throws IOException {
        return mapper.writeValueAsBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes) throws IOException {
        return mapper.readValue(bytes,Object.class);
    }
}
