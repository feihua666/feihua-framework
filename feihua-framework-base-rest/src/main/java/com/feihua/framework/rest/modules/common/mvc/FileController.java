package com.feihua.framework.rest.modules.common.mvc;

import com.feihua.framework.base.modules.file.po.BaseFilePo;
import com.feihua.framework.base.modules.oss.cloud.api.ApiCloudStorageService;
import com.feihua.framework.base.modules.oss.cloud.dto.CloudStorageConfig;
import com.feihua.framework.base.modules.oss.cloud.impl.LocalStorageServiceImpl;
import com.feihua.framework.rest.ResponseJsonRender;
import com.feihua.framework.utils.FileHelper;
import com.feihua.utils.graphic.ImageUtils;
import com.feihua.utils.http.httpServletResponse.ResponseUtils;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.io.FileUtils;
import com.feihua.utils.io.StreamUtils;
import com.feihua.utils.pdf.PdfUtils;
import com.feihua.utils.properties.PropertiesUtils;
import feihua.jdbc.api.pojo.BasePo;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传下载等功能
 * Created by yangwei
 * Created at 2017/8/2 14:52
 */
@RestController
public class FileController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(FileController.class);


    @Autowired
    private ApiCloudStorageService apiCloudStorageService;

    /**
     * @param file
     * @param path
     * @param fileType
     *
     * @return
     *
     * @throws IOException
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    public ResponseEntity fileUpload(MultipartFile file, String path, String fileType, boolean convertImage) throws IOException {
        logger.info("文件上传开始");
        logger.info("当前登录用户id:{}", getLoginUser().getId());
        ResponseJsonRender resultData = new ResponseJsonRender();
        if (!file.isEmpty()) {
            try {
                // 文件扩展名
                String originalFileExtention = FileHelper.getExtention(file.getOriginalFilename());
                if (StringUtils.isEmpty(originalFileExtention)) {
                    resultData.setCode("E404");
                    resultData.setMsg("extention not found");
                    logger.info("上传文件的扩展名不存在");
                    logger.info("文件上传结束，失败");
                    return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
                } else if (StringUtils.endsWithAny(originalFileExtention, "exe", "sh", "bat")) {
                    resultData.setCode("E404");
                    resultData.setMsg("extention is invalid");
                    logger.info("上传文件的扩展名不合法extention:{}", originalFileExtention);
                    logger.info("文件上传结束，失败");
                    return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
                }
                String resultPath;
                InputStream inputStream = file.getInputStream();
                CloudStorageConfig config = apiCloudStorageService.getConfig();
                // 如果启用压缩
                if (BasePo.YesNo.Y.equals(config.getCompress())) {
                    //
                    if (StringUtils.containsAny(originalFileExtention,
                            ImageUtils.IMAGE_TYPE_JPEG,
                            ImageUtils.IMAGE_TYPE_BMP,
                            ImageUtils.IMAGE_TYPE_PNG,
                            ImageUtils.IMAGE_TYPE_JPG)) {
                        BufferedImage image = ImageIO.read(inputStream);
                        inputStream = ImageUtils.compressImage(image, ImageUtils.IMAGE_TYPE_JPEG, Float.parseFloat(PropertiesUtils.getProperty("file.image.quality")));
                    } else {
                        logger.debug("image compress can only support extention jpg,bmp,png,jpeg");
                    }
                }

                List<String> imageUrls = new ArrayList<>();
                    // 如果需要转为图片，则转图片，目前只支持pdf转图片
                    if (convertImage) {
                        ByteArrayOutputStream byteArrayOutputStream = StreamUtils.inputStreamToByteArrayOutputStream(inputStream);
                        PDDocument pdDocument = null;
                        try {
                            pdDocument = PDDocument.load(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        }
                        List<BufferedImage> images = PdfUtils.pdf2Image(pdDocument, "png", 300);

                        for (int i = 0; i < images.size(); i++) {
                            BufferedImage image = images.get(i);
                            String imageUrl = apiCloudStorageService.uploadSuffix(ImageUtils.bufferedImageToInputStream(image, "png"),path, ".png");
                            imageUrls.add(imageUrl);

                        }
                        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    }
                    String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    resultPath = apiCloudStorageService.uploadSuffix(inputStream, path,suffix);

                //保存到数据库
                BaseFilePo baseFilePo = new BaseFilePo();
                baseFilePo.setName(file.getOriginalFilename());
                baseFilePo.setFilename(file.getOriginalFilename());
                baseFilePo.setFilePath(resultPath);
                baseFilePo.setDownloadNum(0);
                baseFilePo.setType(fileType);
                baseFilePo.setDuration("0");
                baseFilePo.setDataUserId(getLoginUser().getId());
                baseFilePo = baseFilePo.service().preInsert(baseFilePo, getLoginUser().getId());
                baseFilePo.service().insertSimple(baseFilePo);

                Map<String, Object> map = new HashMap<>();
                map.put("path", resultPath);
                map.put("ext", originalFileExtention);
                map.put("filename", file.getOriginalFilename());
                map.put("type", fileType);
                map.put("size", file.getSize() + "bytes");
                if (!imageUrls.isEmpty()) {
                    map.put("imageUrls", imageUrls);
                }
                resultData.setData(map);

                logger.info("上传文件的路径path:{}", resultPath);
                logger.info("文件上传结束，成功");
                return new ResponseEntity(resultData, HttpStatus.CREATED);
            } catch (IOException e) {
                throw e;
            }
        }
        resultData.setCode("E404");
        resultData.setMsg("resources not found");
        logger.info("可能没有上传文件数据");
        logger.info("文件上传结束，失败");
        return new ResponseEntity(resultData, HttpStatus.NOT_FOUND);
    }

    /**
     * 文件访问，如果是图片支持图片切割，切割参数请在原始请求后添加，如：原始访问路径是/file/test/sdfsds.jpg,那么切割图片地址为/file/test/sdfsds.jpg_22x44.jpg
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping(value = {"/file/**"}, method = RequestMethod.GET)
    public void fileget(HttpServletRequest request, HttpServletResponse response) {
        ResponseJsonRender resultData = new ResponseJsonRender();
        String path = request.getServletPath().substring(5);

        String contentType = null;
        ApiCloudStorageService localStrorageService = new LocalStorageServiceImpl(apiCloudStorageService.getConfig().getLocal());
        byte [] data = localStrorageService.download(path);
        try {
            //判断是否为图片访问
            if (StringUtils.endsWithAny(path.toLowerCase(), ImageUtils.IMAGE_TYPE_JPEG,
                    ImageUtils.IMAGE_TYPE_BMP,
                    ImageUtils.IMAGE_TYPE_PNG,
                    ImageUtils.IMAGE_TYPE_JPG)) {
                    // 如果切割文件不存在，切割
                    if (data == null || data.length == 0) {
                        // 提取切割参数
                        // 扩展名
                        String originalFileExtention = FileHelper.getExtention(path);
                        // _22x44.jpg
                        String requestParam = path.substring(path.lastIndexOf("_"));
                        // 22x44
                        String simpleParam = requestParam.substring(1, requestParam.lastIndexOf("."));
                        String size[] = simpleParam.split("x");
                        // 宽度
                        int width = Integer.parseInt(size[0]);
                        // 高度
                        int height = Integer.parseInt(size[1]);

                        String originRealPath = path.substring(0, path.lastIndexOf(requestParam));
                        //切割图片
                        byte originData[] = localStrorageService.download(originRealPath);
                        BufferedImage image = ImageUtils.createImage(new ByteArrayInputStream(originData));

                        //是图片
                        if (image != null) {
                            image = ImageUtils.zoomImage(image, width);
                            if (image.getHeight() > height)
                                image = ImageUtils.cutImage(image, 0, (image.getHeight() - height) / 2, width, height);
//                            ImageUtils.outPutImage(image, ImageUtils.IMAGE_TYPE_JPEG, path);
                            String resultPath = localStrorageService.upload(ImageUtils.bufferedImageToInputStream(image,ImageUtils.IMAGE_TYPE_JPEG),path);
                            data = localStrorageService.download(path);
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        }
                    }

            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        try {

            ResponseUtils.renderinputStream(response, new ByteArrayInputStream(data), contentType);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value = {"/proxy"}, method = RequestMethod.GET)
    public String proxyGet(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        return HttpClientUtils.httpGet(url);
    }
}
