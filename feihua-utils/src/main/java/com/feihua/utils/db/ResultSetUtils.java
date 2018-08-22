package com.feihua.utils.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库查询结果集原始操作工具类
 * @author feihua
 * 2014年5月22日 11:18:24
 *
 */
public class ResultSetUtils {

	/**
	 * 获取结果集元数据
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetMetaData getMetaData(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd=rs.getMetaData();
		return rsmd;
	}
	/**
	 * 获取结果集总列数
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	public static int getColumnCount(ResultSetMetaData rsmd) throws SQLException {
		//总列数
		int size = rsmd.getColumnCount();
		return size;
	}
	/**
	 * 获取列名
	 * @param col 从1开始
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	public static String getColumnName(int col,ResultSetMetaData rsmd) throws SQLException{
		String columnName = rsmd.getColumnName(col);
		return columnName;
	}
	/**
	 * 获取表名
	 * @param col
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	public static String getTableName(int col,ResultSetMetaData rsmd) throws SQLException{
		String tableName = rsmd.getTableName(col);
		return tableName;
	}
	/**
	 * 获取列名数据类型
	 * @param col 从1开始
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	public static String getColumnTypeName(int col,ResultSetMetaData rsmd) throws SQLException{
		String columnTypeName = rsmd.getColumnTypeName(col);
		return columnTypeName;
	}
	/**
	 * 获取列类型大小
	 * @param col
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	public static int getColumnDisplaySize(int col,ResultSetMetaData rsmd) throws SQLException{
		int columnSize = rsmd.getColumnDisplaySize(col);
		return columnSize;
	}
	/**
	 * 获取列别名
	 * @param col
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	public static String getColumnAlias(int col,ResultSetMetaData rsmd) throws SQLException{
		String alias = rsmd.getColumnLabel(col);
		return alias;
	}
	/**
	 * 集中数据
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	public List<Object[]> getResultData(ResultSet rs) throws SQLException{
		List<Object[]> list = new ArrayList<Object[]>();
		int size = getColumnCount(getMetaData(rs));
		while(rs.next()){
			Object[] obj = new Object[size];
			for(int i=1;i<=size;i++)
		    {
		     obj[i-1] = rs.getObject(i);
		    }
		}
		return list;
	}
}
