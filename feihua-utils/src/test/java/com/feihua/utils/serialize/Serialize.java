package com.feihua.utils.serialize;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yangwei
 * Created at 2017/7/27 10:36
 */
public class Serialize {

    @Test
    public void Ser(){

        String s = "1";
        ;
        Assert.assertEquals(s,SerializationUtils.deserialize(SerializationUtils.serialize(s)));



    }
}
