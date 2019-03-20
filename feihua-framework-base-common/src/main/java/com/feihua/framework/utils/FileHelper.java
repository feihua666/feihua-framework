package com.feihua.framework.utils;

import com.feihua.exception.BaseException;
import com.feihua.utils.http.httpServletResponse.ResponseCode;
import com.feihua.utils.io.FileUtils;
import com.feihua.utils.properties.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by yangwei
 * Created at 2018/4/9 13:27
 */
public class FileHelper {


    /**
     * 把相对路径拼接根路径以获取真实路径
     * @param relativePath
     * @return
     */
    public static String getRealPath(String rootPath,String relativePath){
        return rootPath + wrapPath(relativePath);
    }

    /**
     * 整理path
     * @param path
     * @return 如：/a/b
     */
    public static String wrapPath(String path){

        if(StringUtils.isNotEmpty(path)){
            // 不充许路径回退符
            if(StringUtils.contains(path,"..")){
                throw new BaseException("path can not include ..", ResponseCode.E400_100000.getCode(), HttpServletResponse.SC_BAD_REQUEST);
            }
            path = path.replace("/", File.separator);
            if(!path.startsWith(File.separator)){
                path = File.separator + path;
            }
            if(path.endsWith(File.separator)){
                path = path.substring(0,path.length() - 1);
            }
            return path;
        }
        return "";
    }

    /**
     * 根据文件名，获取扩展名
     * @param originalFilename
     * @return
     */
    public static String getExtention(String originalFilename){
        if(StringUtils.isNotEmpty(originalFilename)){
            // 文件扩展名
            String originalFileExtention = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            return originalFileExtention;
        }

        return "";
    }
    /**
     * 保存输入流到硬盘
     * @param in
     * @param originalFilename
     * @return
     */
    public static String saveToDisk(InputStream in,String originalFilename,String rootPath,String relativePath) throws IOException {
        // 新文件名
        String newFileName = UUID.randomUUID().toString();
        // 新文件名全名
        String newOriginalFilename = newFileName;
        String originalFileExtention = getExtention(originalFilename);
        if(StringUtils.isNotEmpty(originalFileExtention)){
            newOriginalFilename += "." + originalFileExtention;
        }
        String realPath = getRealPath(rootPath,relativePath);
        if(!FileUtils.exists(realPath)){
            FileUtils.createFolder(realPath);
        }
        // 组装成带文件名的全路径
        realPath = realPath + File.separator + newOriginalFilename;
        File createFile = FileUtils.createFile(realPath,in);

        String resultPath = wrapPath(relativePath) + File.separator + newOriginalFilename;
        return resultPath;
    }

    /**
     * 删除文件
     * @param fileRelativePath
     */
    public static void deleteDiskFile(String rootPath,String fileRelativePath){
        String realPath = FileHelper.getRealPath(rootPath,fileRelativePath);
        if(FileUtils.exists(realPath)){
            FileUtils.deleteFile(realPath);
        }
    }
}
