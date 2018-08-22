package com.feihua.framework.activity;

import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.utils.PageUtils;
import org.activiti.engine.query.NativeQuery;
import org.activiti.engine.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程工具
 */
public class ActUtils {

	public static String VARIABLE_KEY_USERID = "userId";
	public static String VARIABLE_KEY_USERIDS = "userIds";
	public static String VARIABLE_KEY_ROLEIDS = "roleIds";
	public static String VARIABLE_VALUE_NG = "notgive";

	public static Map<String,Object> createVariableMap(){
		Map<String,Object> vars = new HashMap<>();
		vars.put(VARIABLE_KEY_ROLEIDS,VARIABLE_VALUE_NG);
		vars.put(VARIABLE_KEY_USERID,VARIABLE_VALUE_NG);
		vars.put(VARIABLE_KEY_USERIDS,VARIABLE_VALUE_NG);
		return vars;
	}
	public static void putVariableUserId(Map<String,Object> vars,Object value){
		vars.put(VARIABLE_KEY_USERID,value);
	}
	public static void putVariableUserIds(Map<String,Object> vars,Object value){
		vars.put(VARIABLE_KEY_USERIDS,value);
	}
	public static void putVariableRoleIds(Map<String,Object> vars,Object value){
		vars.put(VARIABLE_KEY_ROLEIDS,value);
	}


	/**
	 * activity query for page
	 * @param query
	 * @param page
	 * @return
	 */
	public static List queryListPage(Query query, Page page){
		if(page == null || !page.isPageable()){
			return  query.list();
		}else {
			page.setDataNum((int) query.count());
			return  query.listPage(PageUtils.getFirstOffset(page), page.getPageSize());
		}
	}
	/**
	 * activity query for page
	 * @param query
	 * @param page
	 * @return
	 */
	public static List queryListPage(NativeQuery query, Page page, NativeQuery queryCount){

		if(page == null || !page.isPageable()){
			return  query.list();
		}else {
			page.setDataNum((int)queryCount.count());
			return  query.listPage(PageUtils.getFirstOffset(page), page.getPageSize());
		}
	}
}
