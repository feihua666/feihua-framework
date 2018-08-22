import com.feihua.utils.json.JSONUtils;
import com.feihua.wechat.common.dto.WeixinUserDto;
import com.feihua.wechat.miniprogram.dto.RequestTextMessage;
import com.feihua.wechat.publicplatform.dto.Menu;
import com.feihua.wechat.publicplatform.dto.MenuItem;
import com.feihua.wechat.publicplatform.dto.MenuItemClick;
import com.feihua.wechat.publicplatform.dto.WeixinMenuDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/3/14 18:02
 */
public class Test {
    public static void main(String[] args) throws Exception {
        getSetMethod(new WeixinMenuDto(),"weixinMenuDto","po");

    }

    /**
     * po 转dto
     * @param obj
     * @param setPrefix
     * @param getPrefix
     */
    public static void getSetMethod(Object obj,String setPrefix,String getPrefix){
        Method[]methods = obj.getClass().getMethods();
        for (Method method : methods) {
            String name = method.getName();
            String parentIdXPrefix = "setParentId";
            if(name.startsWith(parentIdXPrefix) && name.length() > parentIdXPrefix.length()){
                continue;
            }
            if(name.startsWith("setDelFlag")
                    || name.startsWith("setCreateBy")
                    || name.startsWith("setCreateAt")
                    ||name.startsWith("setUpdateBy")){
                continue;
            }

            if(name.startsWith("set")){

                System.out.println(setPrefix + "." + name + "(" + getPrefix + ".get" + name.substring(3) + "());");
            }
        }
    }

}
