package com.feihua.framework.image.test;

import com.feihua.framework.mybatis.orm.mapper.NativeSqlMapper;
import com.feihua.framework.utils.FastdfsHelper;
import com.feihua.utils.http.httpclient.HttpClientUtils;
import com.feihua.utils.io.FileUtils;
import com.feihua.utils.string.RegularExpression;
import com.feihua.utils.string.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.csource.common.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangwei
 * Created at 2018/6/29 13:01
 */
public class TestFor268 {

    private static Logger logger = LoggerFactory.getLogger(TestFor268.class);
    private static final String prefix = "http://static.268xue.com";
    static NativeSqlMapper nativeSqlMapper;
    static FastdfsHelper fastdfsHelper;
    static String demo_web = "demo_web_v2.6_1";
    public static void main(String[] args) throws IOException, MyException {
        //初始化ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-mybatis-orm-use.xml");
         nativeSqlMapper = applicationContext.getBean(NativeSqlMapper.class);
         fastdfsHelper = applicationContext.getBean(FastdfsHelper.class);
        demo_web = "demo_web_v2.6_1";
        demoWeb();
        demo_web = "demo_exam_v2_6_1";
        demoExam();
        demo_web = "demo_sns_v2_6_1";
        demoSns();
        //
    }

    public static void demoWeb() throws IOException, MyException {
        //demoWeb_app_website_images();
        /*demoWeb_edu_article();
        demoWeb_edu_book();
        demoWeb_edu_course();
        demoWeb_edu_course_kpoint();
        demoWeb_edu_course_kpoint_atlas();
        demoWeb_edu_library();*/
        //demoWeb_edu_sug_suggest();
        demoWeb_edu_teacher();
        demoWeb_edu_teacher_article();
        demoWeb_edu_teacher_style();
        demoWeb_edu_user_integral_gift();
        demoWeb_edu_website_images();
        demoWeb_edu_website_profile();
        demoWeb_edu_weixin_reply();
        demoWeb_user_expand();
    }
    public static void demoExam() throws IOException, MyException {
        demoExam_exam_paper_type();
        demoExam_exam_question();
    }
    public static void demoSns() throws IOException, MyException {
        demoSns_gro_group();
        demoSns_gro_topic();
    }
    /**
     * web 库
     */
    public static void demoWeb_app_website_images() throws IOException, MyException {
        //app_website_images
        String tableName = "app_website_images";
        String imageColumnName = "IMAGE_URL";
        String keyColumnName = "ID";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add(imageColumnName);
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }
    public static void demoWeb_edu_article() throws IOException, MyException {
        String tableName = "edu_article";
        String imageColumnName = "picture";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add(imageColumnName);
        List<String> textColumnNames = new ArrayList<>();
        textColumnNames.add("content");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,textColumnNames);
    }

    public static void demoWeb_edu_book() throws IOException, MyException {
        String tableName = "edu_book";
        String imageColumnName = "book_img";
        String imageColumnName1 = "book_smallimg";
        String keyColumnName = "book_id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add(imageColumnName);
        imageColumnNames.add(imageColumnName1);
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }
    public static void demoWeb_edu_course() throws IOException, MyException {
        String tableName = "edu_course";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("logo");
        imageColumnNames.add("mobile_logo");
        imageColumnNames.add("package_logo");

        List<String> textColumnNames = new ArrayList<>();
        textColumnNames.add("context");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,textColumnNames);
    }

    public static void demoWeb_edu_course_kpoint() throws IOException, MyException {
        String tableName = "edu_course_kpoint";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("courseware");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }
    public static void demoWeb_edu_course_kpoint_atlas() throws IOException, MyException {
        String tableName = "edu_course_kpoint_atlas";
        String keyColumnName = "Id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("url");
        imageColumnNames.add("url_thumbnail");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }
    public static void demoWeb_edu_library() throws IOException, MyException {
        String tableName = "edu_library";
        String keyColumnName = "ID";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("pdf_url");
        imageColumnNames.add("img_url");
        imageColumnNames.add("png_url_str_tb");
        imageColumnNames.add("png_url_str");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }
    public static void demoWeb_edu_sug_suggest() throws IOException, MyException {
        String tableName = "edu_sug_suggest";
        String keyColumnName = "Id";
        List<String> testColumnNames = new ArrayList<>();
        testColumnNames.add("content");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,null,testColumnNames);
    }
    public static void demoWeb_edu_teacher() throws IOException, MyException {
        String tableName = "edu_teacher";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("PIC_PATH");
        imageColumnNames.add("honor_atlas");
        imageColumnNames.add("style_atlas");
        imageColumnNames.add("style_thumbnail_atlas");
        List<String> testColumnNames = new ArrayList<>();
        testColumnNames.add("style_thumbnail_atlas");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,testColumnNames);
    }
    public static void demoWeb_edu_teacher_article() throws IOException, MyException {
        String tableName = "edu_teacher_article";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("picture");
        List<String> textColumnNames = new ArrayList<>();
        textColumnNames.add("content");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,textColumnNames);
    }
    public static void demoWeb_edu_teacher_style() throws IOException, MyException {
        String tableName = "edu_teacher_style";
        String keyColumnName = "Id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("banner_img");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }

    public static void demoWeb_edu_user_integral_gift() throws IOException, MyException {
        String tableName = "edu_user_integral_gift";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("logo");
        List<String> textColumnNames = new ArrayList<>();
        textColumnNames.add("content");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,textColumnNames);
    }

    public static void demoWeb_edu_website_images() throws IOException, MyException {
        String tableName = "edu_website_images";
        String keyColumnName = "ID";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("IMAGE_URL");
        imageColumnNames.add("PREVIEW_URL");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }
    public static void demoWeb_edu_website_profile() throws IOException, MyException {
        String tableName = "edu_website_profile";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("desciption");

        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }

    public static void demoWeb_edu_weixin_reply() throws IOException, MyException {
        String tableName = "edu_weixin_reply";
        String keyColumnName = "reply_id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("image_url");
        List<String> textColumnNames = new ArrayList<>();
        textColumnNames.add("content");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,textColumnNames);
    }
    public static void demoWeb_user_expand() throws IOException, MyException {
        String tableName = "user_expand";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("avatar");
        imageColumnNames.add("banner_url");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }

    public static void demoExam_exam_paper_type() throws IOException, MyException {
        String tableName = "exam_paper_type";
        String keyColumnName = "ID";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("img_url");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }

    public static void demoExam_exam_question() throws IOException, MyException {
        String tableName = "exam_question";
        String keyColumnName = "id";
        List<String> textColumnNames = new ArrayList<>();
        textColumnNames.add("QST_CONTENT");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,null,textColumnNames);
    }
    public static void demoSns_gro_group() throws IOException, MyException {
        String tableName = "gro_group";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("image_url");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,null);
    }

    public static void demoSns_gro_topic() throws IOException, MyException {
        String tableName = "gro_topic";
        String keyColumnName = "id";
        List<String> imageColumnNames = new ArrayList<>();
        imageColumnNames.add("activity_image");
        List<String> textColumnNames = new ArrayList<>();
        textColumnNames.add("content");
        textColumnNames.add("html_images");
        demoWeb_table_simple(demo_web,tableName,keyColumnName,imageColumnNames,textColumnNames);
    }

        public static void demoWeb_table_simple(String schma,String tableName,String keyColumnName,List<String> imageColumnNames,List<String> textColumnNames) throws IOException, MyException {
        //app_website_images
            String _tableName = "`"+schma+"`.`"+tableName+"` ";
            long start = System.currentTimeMillis();
            logger.info("表{}*********************开始",_tableName);
            List<Map<String,Object>> list = nativeSqlMapper.selectByNativeSqlForList("SELECT * FROM " + _tableName);
            logger.info("表{}*********************总数={}",_tableName,list.size());

            int ii = 0;
            for (Map<String, Object> stringObjectMap : list) {
                ii++;
                logger.info("表{}*********************当前数={}/{}",_tableName,ii,list.size());

                String premaryKey = stringObjectMap.get(keyColumnName).toString();
            if (imageColumnNames != null && !imageColumnNames.isEmpty()) {
                logger.info("表{}*********************imageColumns总数={}",_tableName,imageColumnNames.size());
                int j = 0;
                for (String imageColumnName : imageColumnNames) {
                    j++;
                    String path = (String) stringObjectMap.get(imageColumnName);
                    logger.info("表{}*********************imageColumn={},当前数={}/{}",_tableName,imageColumnName,j,imageColumnNames.size());

                    if (path != null && check(path)) {
                        String uploadFilePath = null;
                        try {
                             uploadFilePath = downloadAndUpload(path);
                        }catch (Exception e){
                            logger.error(e.getMessage()+" 上传下载失败",e);
                        }
                        if (uploadFilePath != null) {
                            nativeSqlMapper.updateByNativeSql("update "+ _tableName +" set "+imageColumnName+" = '"+ uploadFilePath +"' where "+keyColumnName+" = " + premaryKey);
                        }else {
                            logger.info("表{}*********************imageColumn={},当前数={}/{}上传路径为空",_tableName,imageColumnName,j,imageColumnNames.size());

                        }
                        logger.info("表{}*********************imageColumn={},当前数={}/{}已完成",_tableName,imageColumnName,j,imageColumnNames.size());

                    }else{
                        logger.info("表{}*********************imageColumn={},当前数={}/{}检查不合适",_tableName,imageColumnName,j,imageColumnNames.size());
                    }
                }
            }else{
                logger.info("表{}*********************无imageColumn",_tableName);
            }

            if (textColumnNames != null && !textColumnNames.isEmpty()) {
                logger.info("表{}*********************textColumnNames总数={}",_tableName,textColumnNames.size());

                int m=0;
                for (String textColumnName : textColumnNames) {
                    m++;
                    String text = (String) stringObjectMap.get(textColumnName);

                    logger.info("表{}*********************textColumn={},当前数={}/{}",_tableName,textColumnName,m,textColumnNames.size());
                    if(text != null){

                        List<String> textImagesList = findImages(text);
                        if (textImagesList != null && !textImagesList.isEmpty()) {
                            List<String> uploadFilePaths = new ArrayList<>();
                            for (String path : textImagesList) {
                                String uploadFilePath = null;
                                try {
                                    uploadFilePath = downloadAndUpload(path);
                                }catch (Exception e){
                                    logger.info("表{}*********************textColumn={},当前数={}/{}上传下载失败",_tableName,textColumnName,m,textColumnNames.size());
                                    logger.error(e.getMessage(),e);
                                }
                                if(uploadFilePath != null && !StringUtils.isEmpty(uploadFilePath)){
                                    uploadFilePaths.add(uploadFilePath);
                                }
                            }// end for
                            if(uploadFilePaths.size() == textImagesList.size()){
                                //
                                for (int i = 0; i < textImagesList.size(); i++) {
                                    String path = textImagesList.get(i);
                                    text = text.replace(prefix + path,fastdfsHelper.getTracker_ngnix_addr() + uploadFilePaths.get(0));
                                }
                                //完美
                                Map map = new HashMap();
                                map.put("text",text);
                                nativeSqlMapper.updateByNativeSqlWithParam("update "+ _tableName +" set "+textColumnName+" = "+ "#{p.text}" +" where "+keyColumnName+" = " + premaryKey,map);
                                logger.info("表{}*********************textColumn={},当前数={}/{}已完成",_tableName,textColumnName,m,textColumnNames.size());

                            }

                        }// end if
                    }else{
                        logger.info("表{}*********************textColumn={},当前数={}/{}内容为空",_tableName,textColumnName,m,textColumnNames.size());

                    }
                }
            }

        }
            logger.info("表{}*********************结束,耗时{}毫秒",_tableName,System.currentTimeMillis() - start);
    }

    public static String downloadAndUpload(String path) throws IOException, MyException {
        String oriPath = prefix + path;
        HttpClient client = HttpClientUtils.getClient();
        HttpGet get = new HttpGet(oriPath);

        HttpResponse httpResponse =  client.execute(get);
        InputStream inputStream = httpResponse.getEntity().getContent();

        String uploadFilePath = fastdfsHelper.uploadFile(FileUtils.inputStreamtoByteArray(inputStream),getExt(path));
        get.releaseConnection();
        return uploadFilePath;
    }


        public static boolean check(String path){
        return (path.startsWith("/upload") || path.startsWith("//upload") );
    }

    public static List<String> findImages(String text){
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(RegularExpression.url.getReg());

        // 现在创建 matcher 对象
        Matcher m = r.matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()){
            String str = m.group();
            if (str != null) {
                String _str = str.replace(prefix,"");
                if(str.length() > _str.length()){
                    if(_str.endsWith("\"")){
                        _str = _str.substring(0,_str.length() - 1);
                    }
                    list.add(_str);
                }
            }
        }
        return list;
    }
    public static String getExt(String path){

        if(path != null){
            int index = path.lastIndexOf(".");
            if(index > 0){
                return path.substring(index + 1);
            }

        }
        return "";
    }
}
