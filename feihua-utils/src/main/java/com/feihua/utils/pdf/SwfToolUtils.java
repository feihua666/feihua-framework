package com.feihua.utils.pdf;

import com.feihua.utils.io.FileUtils;
import com.feihua.utils.system.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SwfTool 工具跟pdf相关操作,swftool是一个软件，需要安装到系统中
 * Created by yangwei
 * Created at 2018/10/30 16:28
 */
public class SwfToolUtils {
    private final static Logger logger = LoggerFactory.getLogger(SwfToolUtils.class);

    /**
     * pgn图片转swf
     * @param png2SwfCmdPath png2swf命令安装路径
     * @param pngPaths
     * @param swfPath
     * @return
     */
    public static File pngToSwf(String png2SwfCmdPath,List<String> pngPaths,String swfPath){

        if (pngPaths == null && pngPaths.isEmpty()) {
            return null;
        }
        StringBuffer cmd = new StringBuffer(png2SwfCmdPath);
        cmd.append(" -o ").append(swfPath);
        for (String pngPath : pngPaths) {
            cmd.append(" ").append(pngPath);
        }
        Process process = null;
        try {
            process = SystemUtils.execCmd(cmd.toString(),true);
            process.waitFor();
            process.exitValue();
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(),e);
        }
        return FileUtils.exists(swfPath) ? FileUtils.getFile(swfPath) : null;
    }
    /**
     * pgn图片转swf
     * @param png2SwfCmdPath
     * @param pngFolderPath png的文件夹路径
     * @param swfPath
     * @return
     */
    public static File pngToSwf(String png2SwfCmdPath,String pngFolderPath,String swfPath){

        if (!FileUtils.existsFolder(pngFolderPath)) {
            return null;
        }
        List<File> files = FileUtils.getAllFile(pngFolderPath);
        if (files == null || files.isEmpty()) {
            return null;
        }
        List<String> pngPaths = new ArrayList<>(files.size());
        for (File file : files) {
            pngPaths.add(file.getAbsolutePath());
        }
        return pngToSwf(png2SwfCmdPath,pngPaths,swfPath);
    }
}
