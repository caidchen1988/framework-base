package com.zwt.framework.utils.util.validation;

import com.zwt.framework.utils.util.validation.annotation.DataValid;
import com.zwt.framework.utils.util.validation.constant.RegexType;
import com.zwt.framework.utils.util.validation.constant.ReturnCodeEnum;
import com.zwt.framework.utils.util.validation.exception.ValidationException;
import com.zwt.framework.utils.util.validation.util.RegexUtils;
import com.zwt.framework.utils.util.validation.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 注解解析工具类
 */
public class ValidateUtils {

	private ValidateUtils() {
	}

	/**
	 * 解析入口
	 * @param object
	 * @throws Exception
	 */
	public static void valid(Object object) throws  Exception{
		//获取object的类型
		Class<? extends Object> clazz=object.getClass();
		//获取该类型声明的成员
		Field[] fields=clazz.getDeclaredFields();
		//遍历属性
		for(Field field:fields){
			//对于private私有化的成员变量，通过setAccessible来修改器访问权限
			field.setAccessible(true);
			validate(field,object);
			//重新设置会私有权限
			field.setAccessible(false);
		}
	}

    /**
     * 校验方法
	 * @param field
     * @param object
     * @throws Exception
	 */
	public static void validate(Field field,Object object) throws Exception{

		String  description;
		Object        value;
		DataValid dataValid;

		//获取对象的成员的注解信息
		dataValid=field.getAnnotation(DataValid.class);
		value=field.get(object);
		
		if(dataValid == null){
			return;
		}
		
		description=dataValid.description().equals("")?field.getName():dataValid.description();

		//1.为空校验逻辑
		if(!dataValid.nullable()){
			if(value == null|| StringUtils.isBlank(value.toString())){
				throw new ValidationException(description + "不能为空");
			}
		}

		//2.长度校验逻辑
		int minLength = dataValid.minLength();
		if(minLength >= 0){
			if(value instanceof String){
				if (value.toString().length() < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof Collection){
				if(((Collection) value).size() < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof Map){
				if(((Map)value).size() < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof Object[]){
				if(((Object[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof int[]){
				if(((int[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof double[]){
				if(((double[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof float[]){
				if(((float[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof byte[]){
				if(((byte[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof char[]){
				if(((char[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof long[]){
				if(((long[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof short[]){
				if(((short[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}else if(value instanceof boolean[]){
				if(((boolean[])value).length < minLength){
					throw new ValidationException(description +"长度不足");
				}
			}
		}
		int maxLength = dataValid.maxLength();
		if(maxLength >= 0){
			if(value instanceof String){
				if (value.toString().length() > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof Collection){
				if(((Collection) value).size() > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof Map){
				if(((Map) value).size() > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof Object[]){
				if(((Object[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof int[]){
				if(((int[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof double[]){
				if(((double[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof float[]){
				if(((float[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof byte[]){
				if(((byte[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof char[]){
				if(((char[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof long[]){
				if(((long[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof short[]){
				if(((short[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}else if(value instanceof boolean[]){
				if(((boolean[])value).length > maxLength){
					throw new ValidationException(description +"长度超限");
				}
			}
		}

		//3.正则表达式校验逻辑
		ReturnCodeEnum returnCodeEnum = dataValid.throwMsg();
		boolean flag = false;

		RegexType[] regexTypes = dataValid.regexType();
		for (RegexType regexType:regexTypes) {
			//value允许为null或者""的情况下，value正好为null或者""，则不触发正则表达式校验
			if(regexType != RegexType.NONE && value != null && StringUtils.isNotBlank(value.toString())){
				switch (regexType) {
					case CHINESE:
						if(!RegexUtils.isChinese(value.toString())){
							flag = true;
						}
						break;
					case EMAIL:
						if(!RegexUtils.isEmail(value.toString())){
							flag = true;
						}
						break;
					case NUMBER:
						if(!RegexUtils.isNumber(value.toString())){
							flag = true;
						}
						break;
					case PHONENUMBER:
						if(!RegexUtils.isPhoneNumber(value.toString())){
							flag = true;
						}
						break;
					case EMOJI_REPLACE:
						//1.先校验一下有没有emoji
						if(RegexUtils.checkEmoji(value.toString())){
							//2.如果有emoji,尝试去掉它
							String noEmoji = RegexUtils.replaceEmoji(value.toString());
							//3.全部是emoji就校验不通过
							if(StringUtils.isBlank(noEmoji)){
								flag = true;
							}else{
								field.set(object,noEmoji);
							}
						}
						break;
					case EMOJI_CHECK:
						if(RegexUtils.checkEmoji(value.toString())){
							flag = true;
						}
						break;
					default:
						break;
				}
			}
		}

		if(flag){
			if(returnCodeEnum != ReturnCodeEnum.SUCCESS){
				throw new ValidationException(returnCodeEnum);
			}else{
				throw new ValidationException(description + "校验不通过");
			}
		}

		//4.自定义正则表达式校验逻辑
		String regex = dataValid.regexExpression();
		//value允许为null或者""的情况下，value正好为null或者""，则不触发正则表达式校验
		if (!"".equals(regex) && value != null && StringUtils.isNotBlank(value.toString())) {
			if (!value.toString().matches(dataValid.regexExpression())) {
				throw new ValidationException(description + "格式不正确");
			}
		}

		//5.value为对象，需要对对象内部数据进行校验
		if(value instanceof Collection){
			for (Object obj:(Collection)value) {
				valid(obj);
			}
		}else if(value instanceof Map){
			for(Object obj:((Map) value).values()){
				valid(obj);
			}
		}else if(value instanceof Object[]){
			for(Object obj:(Object[]) value){
				valid(obj);
			}
		}
	}
}
