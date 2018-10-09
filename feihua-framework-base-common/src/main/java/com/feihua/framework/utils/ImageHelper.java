package com.feihua.framework.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 图片需要帮助类，借用第三方库的图片压缩，但经测试也不是很理想
 * 如果不想借用第三方库，utils模块也有提供 @see com.feihua.utils.graphic.ImageUtils#compressImage(java.awt.image.BufferedImage, float)
 * Created by yangwei
 * Created at 2018/9/17 11:17
 */
public class ImageHelper {

    public static BufferedImage compressImage(BufferedImage image,float quality) throws IOException {

        return Thumbnails.of(image).scale(1f).outputQuality(quality).asBufferedImage();
    }
}
