package com.feihua.utils.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yangwei
 * Created at 2018/10/30 13:40
 */
public class DoProcessOutput extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(DoProcessOutput.class);

    private InputStream is;
    private boolean errorInputStream = false;

    public DoProcessOutput(InputStream is,boolean errorInputStream) {
        this.is = is;
        this.errorInputStream = errorInputStream;
    }

    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
        String str = null;
        try {
            //这里并没有对流的内容进行处理，只是读了一遍
            while ((str = br.readLine()) != null){
                if(this.errorInputStream){
                    logger.error(str);
                }else {
                    logger.info(str);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
    }
}
