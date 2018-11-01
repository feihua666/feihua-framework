package com.feihua.utils.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangwei
 * Created at 2018/10/31 20:00
 */
public class StreamUtils {

    /**
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] inputStreamToByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream inputStreamToByteArrayOutputStream(InputStream in) throws IOException {

        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = in.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return  swapStream;
    }

    /**
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static ByteArrayInputStream inputStreamToByteArrayInputStream(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return new ByteArrayInputStream(inputStreamToByteArray(in));
    }
}
