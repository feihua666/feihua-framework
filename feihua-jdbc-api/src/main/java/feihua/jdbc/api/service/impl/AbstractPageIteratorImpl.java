package feihua.jdbc.api.service.impl;

import feihua.jdbc.api.pojo.Page;
import feihua.jdbc.api.service.ApiPageIterator;

import java.util.List;

/**
 * 如果继承该类，请不要设置为单例，否则数据会乱
 * Created by yangwei
 * Created at 2018/11/1 20:38
 */
public abstract class AbstractPageIteratorImpl<T> implements ApiPageIterator<T> {

    private Page page;

    private List<T> currentPageData;

    public AbstractPageIteratorImpl(int pageNo, int pageSize){
        Page page = new Page();
        page.setPageable(true);
        page.setPageSize(pageSize);
        page.setPageNo(pageNo);
        this.page = page;
    }

    public void pageNoPlus(Page page){
        if (page == null) {
            getPage().setPageNo(getPage().getPageNo() + 1);
        }else {
            getPage().setPageNo(page.getPageNo() + 1);
        }
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<T> getCurrentPageData() {
        return currentPageData;
    }

    public void setCurrentPageData(List<T> currentPageData) {
        this.currentPageData = currentPageData;
    }
}
