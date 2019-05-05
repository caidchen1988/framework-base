package com.zwt.framework.utils.util.serializer;

import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 使用 FST 实现序列化
 * @author zwt
 */
public class FSTSerializer implements Serializer {
	@Override
	public String name() {
		return "FST";
	}
	@Override
	public byte[] serialize(Object obj) throws IOException {
		try (
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			FSTObjectOutput fout = new FSTObjectOutput(out);
		){
			fout.writeObject(obj);
			fout.flush();
			return out.toByteArray();
		}
	}
	@Override
	public Object deserialize(byte[] bytes) throws IOException {
		if(bytes == null || bytes.length == 0) {
			return null;
		}
		try (
			FSTObjectInput in = new FSTObjectInput(new ByteArrayInputStream(bytes));
		){
			return in.readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
