package feihua.jdbc.api.pojo.test;

import feihua.jdbc.api.pojo.BasePo;
import feihua.jdbc.api.pojo.Page;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by feihua on 2015/6/30.
 */
public class TestPage {


    @Test
    public void testPage(){
        //构造函数默认
        Page p = new Page();
        p.setDataNum(251);
        Assert.assertTrue(p.getPageNum() == 26);

        //构造函数只添加总记录条数
        Page p1 = new Page();
        p1.setDataNum(569);
        Assert.assertTrue(p1.getPageNum() == 57);


    }
    @Test
    public void testBasePo(){
        Assert.assertEquals("Y", BasePo.YesNo.Y.toString());
        Assert.assertEquals(BasePo.YesNo.Y.name(), BasePo.YesNo.Y.toString());
    }
}
