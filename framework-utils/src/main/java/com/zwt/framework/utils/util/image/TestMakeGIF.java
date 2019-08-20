package com.zwt.framework.utils.util.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class TestMakeGIF {
	static final String baseStr = "/Users/zhangwentong/Desktop/排序Sort/梳排序/原图/";
	public static void main(String[] args) {
		try {
			List<String> imgFileNames = new ArrayList<>(52);
			for(int i=0;i<52;i++){
				imgFileNames.add(baseStr+(i+1)+".jpg");
			}

			ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
			AnimatedGifEncoder e = new AnimatedGifEncoder();
			e.start(outputStream2);
			e.setDelay(1000);
			for (int i = 0; i < imgFileNames.size(); i++) {
				e.addFrame(ImageIO.read(new FileInputStream(imgFileNames.get(i))));
			}
			e.finish();
			System.out.println(outputStream2.size());
			byte[] bytearray = outputStream2.toByteArray();
			System.out.println(bytearray.length);
			File file = new File("/Users/zhangwentong/Desktop/1.gif");
			  FileOutputStream fileOutputStream = new FileOutputStream(file);
			    fileOutputStream.write(bytearray);
			    fileOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}