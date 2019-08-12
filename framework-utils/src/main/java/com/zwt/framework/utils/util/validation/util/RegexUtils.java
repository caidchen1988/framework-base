package com.zwt.framework.utils.util.validation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用正则表达式
 */
public class RegexUtils {

	private RegexUtils(){

	}

	/**
	 * emoji正则表达式
	 * 1.杂项符号及图形："[\\uD83C\\uDF00-\\uD83D\\uDDFF]"
	 * 2.增补符号及图形："[\\uD83E\\uDD00-\\uD83E\\uDDFF]"
	 * 3.表情符号："[\\uD83D\\uDE00-\\uD83D\\uDE4F]"
	 * 4.交通及地图符号："[\\uD83D\\uDE80-\\uD83D\\uDEFF]"
	 * 5.杂项符号："[\\u2600-\\u26FF]\\FE0F?"
	 * 6.装饰符号："[\\u2700-\\u27BF]\\FE0F?"
	 * 7.封闭式字母数字符号："\\u24C2\\uFE0F?"
	 * 8.封闭式字母数字补充符号
	 * 	 8.1.区域指示符号："[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}"
	 * 	 8.2. 其他封闭式字母数字补充 emoji 符号："[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?"
	 * 9.键帽符号(#⃣, *️⃣ and 0⃣-9⃣)："[\\u0023\\u002A[\\u0030-\\u0039]]\\uFE0F?\\u20E3"
	 * 10.箭头符号："[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?"
	 * 11.杂项符号及箭头："[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?"
	 * 12.补充箭头符号："[\\u2934\\u2935]\\uFE0F?"
	 * 13.CJK 符号和标点："[\\u3030\\u303D]\\uFE0F?"
	 * 14.封闭式 CJK 字母和月份符号："[\\u3297\\u3299]\\uFE0F?"
	 * 15.封闭式表意文字补充符号："[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?"
	 * 16.一般标点："[\\u203C\\u2049]\\uFE0F?"
	 * 17.几何图形："[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?"
	 * 18.拉丁文补充符号："[\\u00A9\\u00AE]\\uFE0F?"
	 * 19.字母符号："[\\u2122\\u2139]\\uFE0F?"
	 * 20.麻将牌："\\uD83C\\uDC04\\uFE0F?"
	 * 21.扑克牌："\\uD83C\\uDCCF\\uFE0F?"
	 * 22.杂项技术符号："[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?"
	 */
	private static final String EMOJI_REGEX = "(?:[\\uD83C\\uDF00-\\uD83D\\uDDFF]|[\\uD83E\\uDD00-\\uD83E\\uDDFF]|[\\uD83D\\uDE00-\\uD83D\\uDE4F]|[\\uD83D\\uDE80-\\uD83D\\uDEFF]|[\\u2600-\\u26FF]\\uFE0F?|[\\u2700-\\u27BF]\\uFE0F?|\\u24C2\\uFE0F?|[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}|[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?|[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3|[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?|[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?|[\\u2934\\u2935]\\uFE0F?|[\\u3030\\u303D]\\uFE0F?|[\\u3297\\u3299]\\uFE0F?|[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?|[\\u203C\\u2049]\\uFE0F?|[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?|[\\u00A9\\u00AE]\\uFE0F?|[\\u2122\\u2139]\\uFE0F?|\\uD83C\\uDC04\\uFE0F?|\\uD83C\\uDCCF\\uFE0F?|[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?)";

	/**
	 * 邮箱正则表达式
	 */
	private static final String EMAIL_REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

	/**
	 * 中文字符正则
	 */
	private static final String CHINESE_REGEX = "[\u4e00-\u9fa5]";

	/**
	 * 数字正则
	 */
	private static final String NUMBER_REGEX = "[0-9]*";

	/**
	 * 手机号正则
	 */
	private static final String PHONE_REGEX = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$";


	/**
     * 判断是否是正确的邮箱地址
	 * @param email
     * @return
     */
	public static boolean isEmail(String email) {
		if (null == email || "".equals(email)) {
			return false;
		}
		return email.matches(EMAIL_REGEX);
	}

	/**
     * 判断是否含有中文，仅适合中国汉字，不包括标点
	 * @param text
     * @return
     */
	public static boolean isChinese(String text) {
		if (null == text || "".equals(text)){
			return false;
		}
		Pattern p = Pattern.compile(CHINESE_REGEX);
		Matcher m = p.matcher(text);
		return m.find();
	}

	/**
     * 判断是否正整数
	 * @param number
     * @return
     */
	public static boolean isNumber(String number) {
		if (null == number || "".equals(number)) {
			return false;
		}
		return number.matches(NUMBER_REGEX);
	}

    /**
     *  判断是否是手机号码
	 * @param phoneNumber
     * @return
     */
	public static boolean isPhoneNumber(String phoneNumber) {
		if (null == phoneNumber || "".equals(phoneNumber)) {
			return false;
		}
		return phoneNumber.matches(PHONE_REGEX);
	}

	/**
	 * 移除指定字符串的emoji
	 * @param str
	 * @return
	 */
	public static String replaceEmoji(String str){
		if(StringUtils.isBlank(str)){
			return str;
		}
		Pattern pattern = Pattern.compile(EMOJI_REGEX);
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll("");
	}

	/**
	 * 校验str字符是否有emoji
	 * @param str
	 * @return
	 */
	public static boolean checkEmoji(String str){
		if(StringUtils.isBlank(str)){
			return false;
		}
		Pattern pattern = Pattern.compile(EMOJI_REGEX);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
}
