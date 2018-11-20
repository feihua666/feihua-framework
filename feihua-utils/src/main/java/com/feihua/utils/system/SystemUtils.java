package com.feihua.utils.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * java系统工具类，解决系统的输入输出流
 * 飞华
 *	2014年2月24日 14:24:10
 */
public class SystemUtils {

	Logger logger = LoggerFactory.getLogger(SystemUtils.class);
	/**
	 * Scanner 方式读取所有输入
	 * @param message
	 * @return
	 */
	public static String readInputScanner(String message){
		return readInputScanner();
	}
	/**
	 * Scanner 方式读取所有输入
	 * @return
	 */
	public static String readInputScanner(){
		Scanner scanner = new Scanner(System.in); 
		return scanner.next();
	}
	/**
	 * Scanner 方式读取所有匹配输入
	 * @return
	 */
	public static String readInputScanner(Pattern pattern){
		Scanner scanner = new Scanner(System.in); 
		return scanner.next(pattern);
	}
	/**
	 * Scanner 方式读取所有匹配输入
	 * @param message
	 * @return
	 */
	public static String readInputScanner(String message,Pattern pattern){
		return readInputScanner(pattern);
	}
	/**
	 * buffer 方式读取所有输入
	 * @return
	 * @throws IOException
	 */
	public static String readInputBuffer() throws IOException{
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
	    String s = br.readLine();
	    return s;
	}
	/**
	 * buffer 方式读取所有输入
	 * @return
	 * @throws IOException
	 */
	public static String readInputBuffer(String message) throws IOException{
	    return readInputBuffer();
	}
	/**
	 * 通过文件名（相对路径）得到文件名（绝对路径）
	 * @return
	 */
	public static String getClassPath() {
		return SystemUtils.class.getClassLoader().getResource("").getPath();
	}

	/**
	 * 执行命令
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static Process execCmd(String cmd,boolean outputLog) throws IOException {
		Process process = Runtime.getRuntime().exec(cmd);
		if (outputLog) {
			new DoProcessOutput(process.getInputStream(),false);
			new DoProcessOutput(process.getErrorStream(),true);
		}
		return process;
	}
}
