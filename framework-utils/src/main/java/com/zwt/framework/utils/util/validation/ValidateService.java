package com.zwt.framework.utils.util.validation;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * 注解解析Service
 */
public class ValidateService {
	
	/*private static DataValid dataValid;*/

	public ValidateService() {
		super();
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
	
	
	public static void validate(Field field,Object object) throws  Exception{

		String  description = null;
		Object        value = null;
		DataValid dataValid = null;

		//获取对象的成员的注解信息
		dataValid=field.getAnnotation(DataValid.class);
		value=field.get(object);
		
		if(dataValid==null)return;
		
		description=dataValid.description().equals("")?field.getName():dataValid.description();
		
		/*************注解解析工作开始******************/
		if(!dataValid.nullable() && dataValid.regexType() != RegexType.NUMBERORNIL){
			if(value==null|| StringUtils.isBlank(value.toString())){
				throw new Exception(description+"不能为空");
			}
		}
		
		if(dataValid.regexType()!=RegexType.NONE){
			switch (dataValid.regexType()) {
				case NONE:
					break;
				case SPECIALCHAR:
					if(RegexUtils.hasSpecialChar(value.toString())){
						throw new Exception(description+"不能含有特殊字符");
					}
					break;
				case CHINESE:
					if(RegexUtils.isChinese2(value.toString())){
						throw new Exception(description+"不能含有中文字符");
					}
					break;
				case EMAIL:
					if(!RegexUtils.isEmail(value.toString())){
						throw new Exception(description+"邮箱地址格式不正确");
					}
					break;
				case IP:
					if(!RegexUtils.isIp(value.toString())){
						throw new Exception(description+"IP地址格式不正确");
					}
					break;
				case NUMBER:
					if(!RegexUtils.isNumber(value.toString())){
						throw new Exception(description+"不是数字");
					}
					break;
				case NUMBERORNIL:
					if(value == null){
						break;
					}
					if(!RegexUtils.isNumberOrNil(value.toString())){
						throw new Exception(description+"格式不正确");
					}
					break;					
				case PHONENUMBER:
					if(!RegexUtils.isPhoneNumber(value.toString())){
						throw new Exception("手机号格式不正确");
					}
					break;
				case ID:
					if(!RegexUtils.isID(value.toString())){
						throw new Exception("身份证号格式不正确");
					}
					break;					
				default:
					break;
			}
		}
		
		if(!dataValid.regexExpression().equals("")){
			if(value.toString().matches(dataValid.regexExpression())){
				throw new Exception(description+"格式不正确");
			}
		}
		/*************注解解析工作结束******************/
	}
}
