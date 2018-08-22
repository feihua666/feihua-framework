package com.feihua.utils.http.httpServletResponse;

import com.feihua.utils.io.FileUtils;
import com.feihua.utils.json.JSONUtils;
import com.feihua.utils.string.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by yw on 2017/3/13.
 */
public class ResponseUtils {

    /**
     * 客户端返回JSON字符串
     * @param response
     * @param object
     * @return
     */
    public static void renderString(HttpServletResponse response, Object object) throws Exception {
        renderString(response, JSONUtils.obj2json(object), "application/json");
    }

    /**
     * 客户端返回字符串
     * @param response
     * @param string
     * @return
     */
    public static void renderString(HttpServletResponse response, String string, String contentType) throws IOException {
            //response.reset();
            response.setContentType(contentType);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
    }
    public static void renderFile(HttpServletResponse response, String filePath, String contentType) throws IOException {
            if(!StringUtils.isEmpty(contentType)){
                response.setContentType(contentType);
            }
            response.setCharacterEncoding("utf-8");
            OutputStream stream = response.getOutputStream();
            File file = FileUtils.getFile(filePath);
            if(file == null || !file.exists()){
                throw new FileNotFoundException();
            }
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[(int)file.length()];
            int length = inputStream.read(data);
            inputStream.close();
            stream.write(data);
            stream.flush();
            stream.close();
    }
}
