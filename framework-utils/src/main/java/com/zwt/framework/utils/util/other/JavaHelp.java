package com.zwt.framework.utils.util.other;

/**
 * @author zwt
 * @detail
 * @date 2018/9/28
 * @since 1.0
 */
public class JavaHelp {
    public static void main(String[] args) {
        //1、获取jdk信息

        System.out.println(System.getProperty("java.version"));//当前使用jdk版本号
        System.out.println(System.getProperty("java.vendor"));//Java供应商
        System.out.println(System.getProperty("java.vendor.url"));//供应商网址
        System.out.println(System.getProperty("java.home"));//Java路径
        System.out.println(System.getProperty("java.vm.specification.version"));//JVM主版本
        System.out.println(System.getProperty("java.vm.specification.vendor"));//JVM供应商
        System.out.println(System.getProperty("java.vm.specification.name"));//JVM主名称
        System.out.println(System.getProperty("java.vm.version"));//JVM小版本号
        System.out.println(System.getProperty("java.vm.vendor"));//JVM供应商
        System.out.println(System.getProperty("java.vm.name"));//JVM名称
        System.out.println(System.getProperty("java.specification.version"));//JAVA主版本号
        System.out.println(System.getProperty("java.specification.vendor"));//JAVA供应商
        System.out.println(System.getProperty("java.specification.name"));//Java主名称
        System.out.println(System.getProperty("java.class.version"));//java class 版本号
        System.out.println(System.getProperty("java.class.path"));//classpath地址
        System.out.println(System.getProperty("java.library.path"));//lib path地址
        System.out.println(System.getProperty("java.io.tmpdir"));//临时输出路径
        System.out.println(System.getProperty("java.compiler"));//java编译器
        System.out.println(System.getProperty("java.ext.dirs"));//java ext地址

        //2、获取系统信息

        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("os.arch"));
        System.out.println(System.getProperty("os.version"));
        System.out.println(System.getProperty("file.separator"));
        System.out.println(System.getProperty("path.separator"));
        System.out.println(System.getProperty("line.separator"));
        System.out.println(System.getProperty("user.name"));
        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("user.dir"));
    }
}
