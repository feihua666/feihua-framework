package com.feihua.utils.string;

import java.text.MessageFormat;

public class StringUtils {

	/**
	 * 判断字符串是否为无意义的字符串，或空串
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		return (null == str || "".equals(str))?true:false;
	}

	/**
	 *
	 * @param sb
	 * @param str
	 */
	public static void appendIfNotEmpty(StringBuffer sb,String str){
		if (!isEmpty(str)) {
			sb.append(str);
		}
	}

	/**4
	 * +
	 * 判断是否所以的都这空
	 * @param str
	 * @return
	 */
	public static boolean isAllEmpty(String ...str){
		if(null == str) return true;
		for (String s : str) {
			if(!isEmpty(s)){
				return false;
			}
		}
		return true;
	}
	/**
	 * 取文本字符串的长度，每个中文字符长度为2
	 * @param text
	 * @return
	 */
	public final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length;
	}

	/**
	 *
	 * @param str "这是{0}条信息{1}"
	 * @param param
     * @return
     */
	public static String messageFormat(String str,Object ...param) {
		return MessageFormat.format(str, param);
	}

	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstLetter(String str){
		if(!isEmpty(str)){
			return str.substring(0,1).toUpperCase() + str.substring(1);
		}
		return str;
	}

	/**
	 * 属性名转为set方法名
	 * @param propertyName
	 * @return
	 */
	public static String propertyToSetMethodName(String propertyName){
		return "set" + upperCaseFirstLetter(propertyName);
	}
	/**
	 * 属性名转为get方法名
	 * @param propertyName
	 * @return
	 */
	public static String propertyToGetMethodName(String propertyName){
		return "get" + upperCaseFirstLetter(propertyName);
	}
}
