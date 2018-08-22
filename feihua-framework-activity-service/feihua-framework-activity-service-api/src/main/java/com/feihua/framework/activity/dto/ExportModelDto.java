package com.feihua.framework.activity.dto;

import java.io.ByteArrayInputStream;

/**
 * Created by yangwei
 * Created at 2018/4/9 13:03
 */
public class ExportModelDto {
    private String fileName;
    private ByteArrayInputStream inputStream;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
    }
}
