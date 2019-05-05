package com.zwt.framework.utils.util.serializer;

import java.io.IOException;

/**
 * 对象序列化接口
 * @author zwt
 */
public interface Serializer {
	/**
	 * 序列化名称
	 * @return
	 */
	String name();
	/**
	 * 序列化
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	byte[] serialize(Object obj) throws IOException ;
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	Object deserialize(byte[] bytes) throws IOException ;
}
