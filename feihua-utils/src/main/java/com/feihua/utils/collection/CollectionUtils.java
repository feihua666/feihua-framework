package com.feihua.utils.collection;

import com.feihua.utils.string.StringUtils;

import java.util.*;

/**
 * 集合数组操作工具类
 * @author feihua
 * 2014年3月20日 13:32:09
 *
 */
public class CollectionUtils {

	/**
	 * 将一个不包含数据的数组，list对象转为null
	 * @param c
	 * @return
	 */
	public static Object emptyToNull(Object c){
		Object result = null;
		if(c == null) return result;
		if(c instanceof int[] && (((int[]) c).length > 0)){
			result = c;
		}else if(c instanceof String[] && (((String[]) c).length > 0)){
			result = c;
		}else if(c instanceof char[] && (((char[]) c).length > 0)){
			result = c;
		}
		else if(c instanceof Collection && ((Collection)c).size() > 0){
			result = c;
		}
		return result;
	}

	/**
	 * 判断是否为空
	 * @param c
	 * @return
	 */
	public static boolean isNullOrEmpty(List c){
		return c == null || c.isEmpty();
	}

	/**
	 * 判断是否为空
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(Object c){
		return null == emptyToNull(c)?true:false;
	}
	/**
	 * 将一个字符串追加到数据中
	 * @param array
	 * @param str
	 * @return
	 */
	public static String[] appendStrToArray(String array[],String str){
		int arrayLength = 1;
		if(!isEmpty(array)){
			arrayLength = array.length+1;
		}
		String resultArray[] = new String[arrayLength];
		if(arrayLength == 1) resultArray[0] = str;
		else{
			for(int i = 0;i<array.length;i++){
				resultArray[i] = array[i];
			}
			resultArray[resultArray.length-1] = str;
		}
		return resultArray;
	}
	/**
	 * 把array1+array2
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static String[] catArray(String[] array1,String [] array2){
		String resultArray[] = null;
		if(isEmpty(array1) && isEmpty(array2)){
			return null;
		}else if(!isEmpty(array1) && !isEmpty(array2)){
			resultArray  = new String[array1.length+array2.length];
			for(int i=0;i<resultArray.length;i++){
				if(i<array1.length){
					resultArray[i] = array1[i];
				}else{
					resultArray[i] = array2[i-array1.length];
				}
			}
		}else if(!isEmpty(array1)){
			resultArray  = new String[array1.length];
			for(int i=0;i<resultArray.length;i++){
				resultArray[i] = array1[i];
			}
		}else if(!isEmpty(array2)){
			resultArray  = new String[array2.length];
			for(int i=0;i<resultArray.length;i++){
				resultArray[i] = array2[i];
			}
		}
		
		return resultArray;
	}

	/**
	 * 集合过滤
	 * @param list
	 * @param hook
	 * @param <T>
	 * @return
	 */
	public static <T> Collection<T> filter(Collection<T> list, Filter<T> hook) {
		Iterator<T> iterator = list.iterator();
		while (iterator.hasNext()){
			if (hook.filter(iterator.next()) == false) {
				iterator.remove();
			}
		}
		return list;
	}

	/**
	 * 返回true表示过滤掉
	 * @param <T>
	 */
	public static interface Filter<T>{
		public boolean filter(T obj);
	}
	public static String listToString(List<String> list, String seperator) {
		if (list == null || list.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(seperator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}
	public static List<String> StringToList(String str,String seperator){
		if (StringUtils.isEmpty(str)) return null;
		String []a = str.split(seperator);
		return Arrays.asList(a);
	}
	public static <T> List<T> subList(int from, int to,List<T> list){
		if (isNullOrEmpty(list)) {
			return null;
		}
		int _from = from;
		int _to = to;
		int size = list.size();
		if (_from > size - 1) {
			return null;
		}
		if(_to >  size) {
			_to = size;
		}
		if (_from > _to){
			return null;
		}
		return list.subList(_from,_to);
	}
}
