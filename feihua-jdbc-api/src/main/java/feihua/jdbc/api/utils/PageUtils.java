package feihua.jdbc.api.utils;


import com.feihua.utils.thread.ThreadContext;
import feihua.jdbc.api.pojo.Page;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yw on 2016/1/26.
 */
public class PageUtils {

    private static  final String THREAD_LOCAL_PAGE_EKEY = "thread_local_page_ekey";
    private static String THREAD_LOCAL_PAGE_ENABLE_EKEY = "threadlocalPageEnableKey";

    /**
     * 从request获取page
     *
     * @param request
     * @return
     */
    public static Page getPageFromRequest(HttpServletRequest request) {
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        Page page = new Page();
        if(!StringUtils.isEmpty(pageNo)){
            page.setPageNo(Integer.parseInt(pageNo));
            page.setPageable(true);
        }
        if(!StringUtils.isEmpty(pageSize)){
            page.setPageSize(Integer.parseInt(pageSize));
        }
        return page;
    }


    /**
     * 将page放到threadlocal中
     *
     * @param page
     */
    public static void putPageToThreadLocal(Page page) {
        ThreadContext.put(THREAD_LOCAL_PAGE_EKEY, page);
    }

    /**
     * 将page放到threadlocal中
     */
    public static Page getPageFromThreadLocal() {
        return (Page) ThreadContext.get(THREAD_LOCAL_PAGE_EKEY);
    }

    /**
     * 将page放到threadlocal中,并返回
     */
    public static Page getPageFromThreadLocal(Page page) {
        putPageToThreadLocal( page);
        return getPageFromThreadLocal();
    }

    /**
     * 从threadlocal中销毁
     */
    private static void destroyPageFromThreadLocal() {
        ThreadContext.remove(THREAD_LOCAL_PAGE_EKEY);

    }
    public static void pageStart() {
        ThreadContext.put(THREAD_LOCAL_PAGE_ENABLE_EKEY, true);
    }
    public static void pageEnd() {
        destroyPageFromThreadLocal();
    }
    public static boolean isPageStarted() {
        return  ThreadContext.get(THREAD_LOCAL_PAGE_ENABLE_EKEY) != null;
    }

    public static int getFirstOffset(Page page){
        int offset = (page.getPageNo() - 1) * page.getPageSize();
        if (offset < 0) {
            offset = 0;
        }
        return offset;
    }
}
