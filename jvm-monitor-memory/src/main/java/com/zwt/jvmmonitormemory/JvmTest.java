package com.zwt.jvmmonitormemory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 产生对象测试类
 */
public class JvmTest {
	//是否停止产生对象
	private volatile boolean isStop = false;
	/**
	 * 停止产生对象
	 */
	public void stop(){
		this.isStop = true;
	}

	/**
	 * 垃圾回收测试
	 */
	public void test() {
		List<byte[]> list = new ArrayList<>();
		try{
			while (!isStop){
				list.add(new byte[1024*1024]);
				if(list.size()>1000){
					list = new ArrayList<>();
					TimeUnit.MILLISECONDS.sleep(100);
				}
			}
		}catch(InterruptedException e){
		    e.printStackTrace();
		}
	}

	/**
	 * oom方法测试
	 */
	public void oomTest(){
		List<JvmTest> list = new ArrayList<>();
		try{
			while (!isStop){
				list.add(new JvmTest());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * stackOver Test
	 * @param n
	 * @return
	 */
	public int stackOverTest(int n){
		if(n==1){
			return 8;
		}else{
			return stackOverTest(n-1)+2;
		}
	}

	/**
	 * MetaSpace Test
	 */
	public void MetaSpaceOOMTest() {
		while (true) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(JvmTest.class);
			enhancer.setUseCache(false);
			enhancer.setCallback(
					new MethodInterceptor() {
						@Override
						public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
							return methodProxy.invokeSuper(o,objects);
						}
					}
			);
			enhancer.create();
		}
	}


	public static void main(String[] args) {
		new JvmTest().oomTest();
	}
}
