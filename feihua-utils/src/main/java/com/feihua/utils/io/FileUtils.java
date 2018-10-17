package com.feihua.utils.io;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * java文件操作封装
 * 杨威
 * 2013年8月22日 17:48:46
 *
 */
public class FileUtils {

	/**
	 * 创建文件夹,如果存在则返回已存在的，不存在则创建，
	 * 可能的问题，如果目录很长中间没有的目录也会被创建，一旦创建失败，中间的目录可能已经生成
	 * @param folderPath 要创建的目录
	 * @return
	 */
	public static File createFolder(String folderPath) {
		File file = null;

		file = getFile(folderPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		return file;
	}
	/**
	 * 判断文件是否存在,不是判断文件夹
	 * @param filePath
	 * @return 文件存在返回true 不存在返回false
	 */
	public static boolean exists(String filePath){
		boolean flag = false;
		File file = getFile(filePath);
		if(file!=null && file.exists() && file.isFile()){
			flag = true;
		}
		return flag;
	}
	/**
	 * 删除文件夹
	 * @param folderPath 文件夹路径
	 * @return
	 */
	public static boolean deleteFolder(String folderPath){
		boolean flag = false;
		folderPath = folderPath.endsWith(File.separator)?folderPath:folderPath+File.separator;
		File file = getFile(folderPath);
		//如果不存在
		if(!file.exists()){
			return false;
		}
		//如果不是目录
		else if(!file.isDirectory()){
			return false;
		}
		File[] subFiles = file.listFiles();
			for (int i = 0; i < subFiles.length; i++) {  
		        //删除子文件  
		        if (subFiles[i].isFile()) {  
		            flag = deleteFile(subFiles[i].getAbsolutePath());  
		            if (!flag) return flag;  
		        } //删除子目录  
		        else {  
		            flag = deleteFolder(subFiles[i].getAbsolutePath());  
		            if (!flag) return flag;  
		        }  
		    }
			//删除当前目录
			flag = file.delete();
		return flag;
		
	}
	/**
	 * 删除一个文件夹下的所有文件夹不包括文件
	 * @param folderPath 文件夹目录
	 * @return 删除的文件夹的名称
	 */
	public static List<String> deleteAllFolder(String folderPath){
		List<String> list = new ArrayList<String>();
		File folder = getFile(folderPath);
		//如果folder是一个目录
		if(folder.exists()&&folder.isDirectory()){
			File[]subFolder = folder.listFiles();
			for(int i=0;i<subFolder.length;i++){
				if(subFolder[i].isDirectory()){
					deleteFolder(subFolder[i].getAbsolutePath());
					list.add(subFolder[i].getName());
				}
			}
		}
		//如果是一个文件
		else{
			//无操作
		}
		
		return list;
	}
	/**
	 * 删除一个文件夹下的所有文件不包括文件夹
	 * @param folderPath 文件夹目录
	 * @return 删除的文件的名称
	 */
	public static List<String> deleteAllFile(String folderPath){
		List<String> list = new ArrayList<String>();
		File folder = getFile(folderPath);
		//如果folder是一个目录
		if(folder.exists()&&folder.isDirectory()){
			File[]subFolder = folder.listFiles();
			for(int i=0;i<subFolder.length;i++){
				if(subFolder[i].isFile()){
					subFolder[i].delete();
					list.add(subFolder[i].getName());
				}
			}
		}
		//如果是一个文件
		else{
			//无操作
		}
		
		return list;
	}
	/**
	 * 清空文件夹
	 * @param folderPath 文件夹目录
	 * @return 删除的文件夹或文件的名称
	 */
	public static List<String> emptyFolder(String folderPath){
		List<String> list = new ArrayList<String>();
		File folder = getFile(folderPath);
		//如果folder是一个目录
		if(folder.exists()&&folder.isDirectory()){
			File[]subFolder = folder.listFiles();
			for(int i=0;i<subFolder.length;i++){
				subFolder[i].delete();
				if(subFolder[i].isDirectory()){
					list.add("folder:"+subFolder[i].getName());
				}else{
					list.add("file:"+subFolder[i].getName());
				}
			}
		}
		//如果是一个文件
		else{
			//无操作
		}
		
		return list;
	}
	/**
	 * 创建空文件
	 * @param filePath 文件目录及文件名
	 * flag 如果有重名的是否删除创建，true则删除，
	 * @return 如果有重名的，且没有删除，则返回已在文件，如果没有重名的则返回创建的文件
	 * @throws IOException 
	 */
	public static File createFile(String filePath,boolean flag) throws IOException{
		File file = null;
		file = getFile(filePath);
			if(!file.exists()){
				file.createNewFile();
			}else{
				//如果存在重名文件
				if(flag){
					//如果要删除重名文件
					file.delete();
					file.createNewFile();
				}
			}
		return file;
	}
	/**
	 * 创建空文件，如果存在则返回存在的文件
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static File createFile(String filePath) throws IOException{
		
		return createFile(filePath,false);
	}
	/**
	 * 根据输入流创建文件，返回创建的文件
	 * @param outFilePath
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static File createFile(String outFilePath,InputStream in) throws IOException{
		File targetFile = getFile(outFilePath);
		FileOutputStream out = new FileOutputStream(targetFile);
        byte[] buffer = new byte[1024];
        int byteread = 0;
        while((byteread = in.read(buffer)) !=-1){
        	out.write(buffer,0,byteread);
        }
        return targetFile;
	}
	/**
	 * 取得filePath路径下的文件
	 * @param filePath
	 * @return
	 */
	public static File getFile(String filePath){
		File file = null;
		file = new File(filePath);
		return file;
	}
	/**
	 * 删除文件
	 * @param filePath 文件夹路径
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		boolean flag = false;
		File file = getFile(filePath);

		if (file.exists()) {
			// 判断给定目录不是文件
			if (!file.isFile())
				return false;
		} else {
			return false;
		}
		file.delete();
		flag = true;

		return flag;
	}
	/**
	 * 复制文件夹 将sourceFolerPath下的所有文件和文件夹复制到targetFolderPath
	 * @param sourceFolerPath 要复制的文件夹路径
	 * @param targetFolderPath 要复制到的文件夹路径
	 * @return
	 */
	public static boolean copyFolderContents(String sourceFolerPath,String targetFolderPath,boolean overlay){
		boolean flag = false;
		
		// 判断源目录是否存在  
        File srcDir = getFile(sourceFolerPath);  
        if (!srcDir.exists()) {  
            return false;  
        } else if (!srcDir.isDirectory()) {  
            return false;  
        }
        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符  
        if (!targetFolderPath.endsWith(File.separator)) {  
        	//targetFolderPath = targetFolderPath + File.separator+srcDir.getName()+File.separator;  
        	targetFolderPath = targetFolderPath +File.separator;
        }
		   // 新建目标目录
        File targetFolder = getFile(targetFolderPath);
     // 如果目标文件夹存在  
        if (targetFolder.exists()) {  
            // 如果允许覆盖则删除已存在的目标目录  
            if (overlay) {  
                deleteFolder(targetFolderPath); 
             // 创建目的目录  
                if (!targetFolder.mkdirs()) {  
                    return false;  
                } 
            } else {  
                return false;  
            }  
        } else {  
            // 创建目的目录  
            if (!targetFolder.mkdirs()) {  
                return false;  
            }  
        } 

        // 获取源文件夹当前下的文件或目录
        File[] file = srcDir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = getFile(getFile(targetFolderPath).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile,overlay);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceFolerPath + File.separator + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetFolderPath  + file[i].getName();
                copyFolderContents(dir1, dir2,overlay);
            }
        }
		return flag;
	}
	
	/**
	 * 复制文件夹 将sourceFolerPath文件夹复制到targetFolderPath下面
	 * @param sourceFolderPath 要复制的文件夹路径
	 * @param targetFolderPath 要复制到的文件夹路径
	 * @return
	 */
	public static boolean copyFolder(String sourceFolderPath,String targetFolderPath,boolean overlay){
		if(targetFolderPath.endsWith(File.separator)){
			targetFolderPath = targetFolderPath+getFile(sourceFolderPath).getName();
		}else{
			targetFolderPath = targetFolderPath+File.separator+getFile(sourceFolderPath).getName();
		}
		return copyFolderContents(sourceFolderPath,targetFolderPath,overlay);
	}
    /**
     * 复制文件
     * @param sourceFilePath 源文件
     * @param targetFilePath 目标文件路径，或目录
     * @param overlay
     */
    public static boolean copyFile(String sourceFilePath, String targetFilePath,boolean overlay){
    	
        return copyFile(getFile(sourceFilePath), getFile(targetFilePath), overlay);
    }
    /**
     * 复制文件
     * @param sourceFile 源文件
     * @param targetFile 目标文件或目录
     * @param overlay boolean overlay 真为覆盖
     */
    public static boolean copyFile(File sourceFile, File targetFile,boolean overlay){
    	boolean flag = false;
    	FileInputStream in = null;
    	FileOutputStream out = null;
    	//源文件不存在
    	if(!sourceFile.exists()){
    		return false;
    	}
    	//源文件不是文件
    	else if(!sourceFile.isFile()){
    		return false;
    	}
    	
    	 // 判断目标文件是否存在  
        if (targetFile.exists()&&targetFile.isFile()) {  
            // 如果目标文件存在并允许覆盖  
            if (overlay) {  
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
            	targetFile.delete();  
            }  
        } else {  
        	if (!targetFile.exists()&&!targetFile.getParentFile().mkdirs())
            if(!targetFile.getParentFile().exists()&&!targetFile.getParentFile().mkdirs()){
            	return false;
            }
            else{
            	targetFile = getFile(targetFile.getParentFile().getPath()+File.separator+sourceFile.getName());
            }
        }
        try {
        	
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int byteread = 0;
            while((byteread = in.read(buffer)) !=-1){
            	out.write(buffer,0,byteread);
            }
            flag = true;
        }catch(IOException e){
        	flag = false;
        }
        finally {
            if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
            if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
        }
        return flag;
    }
    /**
     * 取得一个文件夹下所有文件，不包括文件夹
     * @param folderPath 文件夹路径
     * @return 所有文件
     */
    public static List<File> getAllFile(String folderPath){
    	List<File> list = new ArrayList<File>();
    	File file = getFile(folderPath);
    	if(file.exists()&&file.isDirectory()){
    		File filesTemp[] = file.listFiles();
    		for(int i=0;i<filesTemp.length;i++){
    			if(filesTemp[i].isFile()){
    				list.add(filesTemp[i]);
    			}
    		}
    	}
    	return list;
    }
    /**
     * 取得一个文件夹下所有文件或文件夹
     * @param folderPath 文件夹路径
     * @return 所有文件
     */
    public static List<File> getAllFiles(String folderPath){
    	List<File> list = new ArrayList<File>();
    	File file = getFile(folderPath);
    	if(file.exists()&&file.isDirectory()){
    		File filesTemp[] = file.listFiles();
    		for(int i=0;i<filesTemp.length;i++){
    			list.add(filesTemp[i]);
    		}
    	}
    	return list;
    }
    /**
     * 取得一个文件夹下所有文件夹,不包括文件
     * @param folderPath 文件夹路径
     * @return 所有文件
     */
    public static List<File> getAllFolder(String folderPath){
    	List<File> list = new ArrayList<File>();
    	File file = getFile(folderPath);
    	if(file.exists()&&file.isDirectory()){
    		File filesTemp[] = file.listFiles();
    		for(int i=0;i<filesTemp.length;i++){
    			if(filesTemp[i].isDirectory()){
    				list.add(filesTemp[i]);
    			}
    		}
    	}
    	return list;
    }
	/**
	 * 一次读取文件所有内容
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileContent(File file) throws IOException {
		Long fileLengthLong = file.length();
		byte[] fileContent = new byte[fileLengthLong.intValue()];
		FileInputStream inputStream = new FileInputStream(file);
		inputStream.read(fileContent);
		inputStream.close();
		String string = new String(fileContent);
		return string;
	}
    /**
     * 显示输入流中还剩的字节数
     * @throws IOException 
     */
    public static int getAvailableBytes(InputStream in) throws IOException {
        return in.available();
    }
    /**
     * java 读取filePath路径下一般properties配置文件的操作
     * @throws IOException 
     */
	public static Properties getProperties(String filePath) throws IOException {
		Properties p = new Properties();
		InputStream in = null;
		in = new FileInputStream(filePath);
		p.load(in);
		return p;
	}
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     * @throws IOException 
     */
    public List<String> readFileByLines(File file) throws IOException {
        BufferedReader reader = null;
        List<String> list = new ArrayList<String>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {

                list.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;
    }

	/**
	 * 判断路径是否为绝对路径
	 * @param path
	 * @return
	 */
	public static boolean isAboslutePath(String path){
		return new File(path).isAbsolute();
	}

	/**
	 * 获取项目的绝对目录
	 * @return
	 */
	public static String getProjectPath(){
		return getCanonicalPath("");
	}

	/**
	 * 将路径获取为标准路径
	 * 如果参数为绝对路径：E:/test/../text.txt 返回 E:/test/text.txt
	 * 如果参数不为绝对路径：../text.txt 返回 项目的绝对路径的上一层/text.txt
	 * @param path
	 * @return
	 */
	public static String getCanonicalPath(String path){
		String result = null;
		try {
			result =  new File(path).getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("get Canonical path exception",e);
		}
		return result;
	}
    /**
     * 向文件中写入字符串
     * @param file
     * @param string
     * @throws IOException 
     */
    public static void writeString(File file,String string) throws IOException{
		writeString(file,string,"UTF-8");
    }
	/**
	 * 向文件中写入字符串
	 * @param file
	 * @param string
	 * @throws IOException
	 */
	public static void writeString(File file,String string,String encode){
		OutputStreamWriter write = null;
		BufferedWriter writer = null;
		try {
			write = new OutputStreamWriter(new FileOutputStream(file), encode);
			writer = new BufferedWriter(write);
			writer.write(string);
			writer.close();
		} catch (Exception e) {
			throw new RuntimeException("writeString to file " + file.getPath(),e);
		}finally {
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
			if(write != null){
				try {
					write.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] inputStreamtoByteArray(InputStream in) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while ((n = in.read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}
}
