package com.feihua.utils.EmailUtils;

import java.io.File;

/**
 * Created by yw on 2017/1/10.
 */
public class MailAttach {

    private String cid;
    private File file;
    private String fileName;

    public MailAttach() {

    }

    public MailAttach(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
    }
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
