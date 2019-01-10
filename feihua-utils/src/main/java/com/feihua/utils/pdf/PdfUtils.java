package com.feihua.utils.pdf;

import com.feihua.utils.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * pdf操作工具类
 * Created by yangwei
 * Created at 2018/10/30 15:38
 */
public class PdfUtils {

    private final static Logger logger = LoggerFactory.getLogger(PdfUtils.class);
    /***
     * PDF文件转图片，全部页数
     *
     * @param pdfFilePath pdf完整路径
     * @param dstImgFolder 图片存放的文件夹
     * @param formatName 图片格式
     * @param dpi dpi越大转换后越清晰，相对转换速度越慢，可以先试试300
     * @return 返回图片路径
     */
    public static List<String> pdf2Image(String pdfFilePath, String dstImgFolder,String formatName, int dpi) {
        File file = FileUtils.getFile(pdfFilePath);
        if (!file.exists()) {
            logger.warn("file not exist by path {}",pdfFilePath);
            return null;
        }
        if (!FileUtils.exists(dstImgFolder)) {
            FileUtils.createFolder(dstImgFolder);
        }
        PDDocument pdDocument = null;
        List<String> r = new ArrayList<>();

        try {
            pdDocument = PDDocument.load(file);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        List<BufferedImage> images = pdf2Image(pdDocument,formatName,dpi);
        StringBuffer imgFilePath = null;
        for (int i = 0; i < images.size(); i++) {
            BufferedImage image = images.get(i);
            String name = file.getName();
            String imgFilePathPrefix = dstImgFolder + File.separator + name;
            imgFilePath = new StringBuffer();
            imgFilePath.append(imgFilePathPrefix);
            imgFilePath.append("_");
            imgFilePath.append(String.valueOf(i + 1));
            imgFilePath.append(".");
            imgFilePath.append(formatName);
            File dstFile = new File(imgFilePath.toString());
            try {
                ImageIO.write(image, formatName, dstFile);
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
            r.add(imgFilePath.toString());
        }
        return r;
    }
    public static List<BufferedImage> pdf2Image(PDDocument pdDocument,String formatName, int dpi) {
        List<BufferedImage> result = new ArrayList<>();
        if (pdDocument != null) {
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            int pages = pdDocument .getNumberOfPages();
            StringBuffer imgFilePath = null;
            for (int i = 0; i < pages; i++) {
                BufferedImage image = null;
                try {
                    image = renderer.renderImageWithDPI(i, dpi);
                    result.add(image);
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
            try {
                pdDocument.close();
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
        return result;
    }
}
