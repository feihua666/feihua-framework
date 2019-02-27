import com.feihua.framework.base.modules.user.po.BaseUserPo;
import feihua.jdbc.api.pojo.BasePo;

import java.io.*;

/**
 * Created by yangwei
 * Created at 2018/7/27 20:36
 */
public class Test {
    public static void main(String[] args) {
        /*pdf2swf(new File("d:/test/6f1293d8-022a-4414-b4ad-7729cd58ecbc-pdf.pdf"),
                "d:/test/6f1293d8-022a-4414-b4ad-7729cd58ecbc-swf.swf");*/

    }

    public static boolean pdf2swf(File sourceFile, String targetFileAbsolutePath) {
        try {
            /**
             * SWFTools_HOME在系统中的安装目录
             * 1：window需要指定到 pdf2swf.exe 文件
             * 2：linux则xxx/xxx/xxx/pdf2swf即可
             */
            String SWFTools_HOME = "C:\\Program Files (x86)\\SWFTools\\pdf2swf.exe";
            String[] cmd = new String[6];
            cmd[0] = SWFTools_HOME;
            cmd[1] = "";
            cmd[2] = sourceFile.getAbsolutePath();
            cmd[3] = "-o";
            cmd[4] = targetFileAbsolutePath;
            cmd[5] = "-T 9";
            Process pro =Runtime.getRuntime().exec(cmd);
//			 如果不读取流则targetFile.exists() 文件不存在，但是程序没有问题
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
//			while (bufferedReader.readLine() != null);

            new DoOutput(pro.getInputStream()).start();
            new DoOutput(pro.getErrorStream()).start();
            pro.getOutputStream().flush();
            pro.waitFor();
            pro.exitValue();
            System.out.println("PDF文档转swf成功！");
        } catch (Exception e) {
            System.out.println("PDF文档转swf失败！");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private static    class DoOutput extends Thread {
        public InputStream is;

        //构造方法
        public DoOutput(InputStream is) {
            this.is = is;
        }

        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
            String str = null;
            try {
                //这里并没有对流的内容进行处理，只是读了一遍
                while ((str = br.readLine()) != null){
                    System.out.println(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
