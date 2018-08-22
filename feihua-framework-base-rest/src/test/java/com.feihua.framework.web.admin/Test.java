package com.feihua.framework.web.admin;

import com.feihua.framework.utils.FileHelper;
import com.feihua.utils.graphic.ImageUtils;
import com.feihua.utils.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by yangwei
 * Created at 2017/12/26 14:09
 */
public class Test {
    public static void main(String[] args) throws IOException {
        String realPath = "d://test.jpg";
        String str = realPath.substring(realPath.lastIndexOf("_"));
        if(StringUtils.endsWithAny(realPath.toLowerCase(),"jpg","bmp","png")){
            // 如果切割文件不存在，切割
            if(!FileUtils.exists(realPath)){
                // 提取切割参数
                // 扩展名
                String originalFileExtention = FileHelper.getExtention(realPath);
                // _22x44.jpg
                String requestParam = realPath.substring(realPath.lastIndexOf("_"));
                // 22x44
                String simpleParam = requestParam.substring(1,requestParam.lastIndexOf("."));
                String size[] = simpleParam.split("x");
                // 宽度
                int width = Integer.parseInt(size[0]);
                // 高度
                int height = Integer.parseInt(size[1]);

                String originRealPath = realPath.substring(0,realPath.lastIndexOf(requestParam));
                //切割图片
                BufferedImage image = ImageUtils.createImage(originRealPath);
                //是图片
                if (image != null) {
                    image = ImageUtils.zoomImage(image,width,height);
                    ImageUtils.outPutImage(image,realPath);

                }else{
                }
            }

        }
    }
}
