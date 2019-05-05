package com.zwt.framework.utils.util.serializer;


import java.io.*;

/**
 * 标准的 Java 序列化
 * @author zwt
 */
public class JavaSerializer implements Serializer {
	@Override
	public String name() {
		return "Java";
	}
	@Override
	public byte[] serialize(Object obj) throws IOException {
		try(
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
		){
			oos.writeObject(obj);
			return baos.toByteArray();
		}
	}
	@Override
	public Object deserialize(byte[] bits) throws IOException{
		if(bits == null || bits.length == 0) {
			return null;
		}
		try (
			ByteArrayInputStream bais = new ByteArrayInputStream(bits);
			ObjectInputStream ois = new ObjectInputStream(bais);
		){
			return ois.readObject();
		}catch (ClassNotFoundException e){
			throw new RuntimeException(e);
		}
	}
}
