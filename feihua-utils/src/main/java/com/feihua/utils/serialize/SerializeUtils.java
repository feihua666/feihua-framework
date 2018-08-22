package com.feihua.utils.serialize;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 序列化实例工具类
 * Created by lyy on 2015/9/7.
 */
public class SerializeUtils {

    private static Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

    /**
     * 将传入实例序列化为一个byte数族
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        if (object  instanceof  Serializable) {
            ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            try {
                //序列化
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                byte[] bytes = baos.toByteArray();
                return bytes;
            } catch (Exception e) {
                logger.error("serialize instance exception",e);
            }
        }else {
            return SerializationUtils.serialize((Serializable) object);
        }
        return null;
    }

    /**
     * 将传入byte数组反序列化为一个实例
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        if (null != bytes){
            ByteArrayInputStream bais = null;
            try {
                //反序列化
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                logger.error("unserialize instance exception",e);
            }
        }
        return null;
    }
}
