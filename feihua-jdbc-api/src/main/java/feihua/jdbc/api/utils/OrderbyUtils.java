package feihua.jdbc.api.utils;


import com.feihua.utils.thread.ThreadContext;
import feihua.jdbc.api.pojo.Orderby;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by yw on 2016/1/26.
 */
public class OrderbyUtils {

    private static  final String THREAD_LOCAL_ORDERBY_KEY = "thread_local_orderby_key";
    private static String THREAD_LOCAL_ORDERBY_ENABLE_EKEY = "threadlocalOrderbyEnableKey";

    public static Orderby getOrderbyFromMap(Map map) {
        //orderby的格式应该是：name-desc,sex-asc,排序方式可以省略会有默认，建议显示加上
        Object _orderby = map.get("orderby");
        Object _orderable = map.get("orderable");

        String orderby = null;
        String orderable = null;
        if (_orderby != null) {
            orderby = _orderby.toString();
        }
        if (_orderable != null) {
            orderable = _orderable.toString();
        }
        return getOrderby(orderable,orderby);
    }
    /**
     * 从request获取page
     *
     * @param request
     * @return
     */
    public static Orderby getOrderbyFromRequest(HttpServletRequest request) {
        //orderby的格式应该是：name-desc,sex-asc,排序方式可以省略会有默认，建议显示加上
        String orderby = request.getParameter("orderby");
        String orderable = request.getParameter("orderable");

        return getOrderby(orderable,orderby);
    }
    private static Orderby getOrderby(String orderable,String orderby){
        Orderby orderby1 = new Orderby();

        orderby1.setOrderable(BooleanUtils.toBoolean(orderable));
        if(!StringUtils.isEmpty(orderby)){
            String o[] = orderby.split(",");
            for(int i=0;i<o.length;i++){
                String c[] = o[i].split("-");
                if(c.length>0){
                    Orderby.Statement statement = null;
                    //如果长度为1，则省略了排序方式
                    if(c.length == 1){
                        statement = new Orderby.Statement(i,c[0],"asc");
                    }else if(c.length == 2){
                        statement = new Orderby.Statement(i,c[0],c[1]);
                    }
                    orderby1.getStatements().add(statement);
                }
            }
            orderby1.setOrderable(true);
        }
        return orderby1;
    }

    /**
     * Orderby
     *
     * @param orderby
     */
    public static void putOrderbyToThreadLocal(Orderby orderby) {
        ThreadContext.put(THREAD_LOCAL_ORDERBY_KEY, orderby);
    }
    /**
     * Orderby
     */
    public static Orderby getOrderbyFromThreadLocal() {
        return (Orderby) ThreadContext.get(THREAD_LOCAL_ORDERBY_KEY);
    }

    /**
     * 从threadlocal中销毁
     */
    private static void destroyOrderbyFromThreadLocal() {
        ThreadContext.remove(THREAD_LOCAL_ORDERBY_KEY);
    }
    public static void orderbyStart() {
        ThreadContext.put(THREAD_LOCAL_ORDERBY_ENABLE_EKEY, true);
    }
    public static void orderbyEnd() {
        destroyOrderbyFromThreadLocal();
    }
    public static boolean isOrderbyStarted() {
        return  ThreadContext.get(THREAD_LOCAL_ORDERBY_ENABLE_EKEY) != null;
    }
}
