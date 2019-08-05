package com.zwt.framework.utils.util.validation.annotation;

import com.zwt.framework.utils.util.validation.constant.RegexType;
import com.zwt.framework.utils.util.validation.constant.ReturnCodeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据验证注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
public @interface DataValid {
	
	//是否可以为空
	boolean nullable() default false;
	
	//提供几种常用的正则验证
	RegexType[] regexType() default RegexType.NONE;

	//自定义正则验证
	String regexExpression() default "";
	
	//参数或者字段描述
	String description() default "";

	//校验异常抛出指定信息
	ReturnCodeEnum throwMsg() default ReturnCodeEnum.SUCCESS;

	//最小长度
	int minLength() default -1;

	//最大长度
	int maxLength() default -1;
}
