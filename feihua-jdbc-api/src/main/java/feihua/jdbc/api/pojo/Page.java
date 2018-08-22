package feihua.jdbc.api.pojo;

import java.io.Serializable;

/**
 * 分页对象
 * Created by yw on 2016/1/26.
 */
public class Page implements Serializable{

    public Page(){}

    public Page(int pageNo,int pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }


    /**
     * 分页，默认从第一页开始
     */
    private Integer pageNo = 1;

    /**
     * 每页数据量，默认10条
     */
    private Integer pageSize = 10;

    /**
     * 数据总数
     */
    private Integer dataNum = 0;

    /**
     * 分页总数
     */
    private Integer pageNum = 0;

    /**
     * 是否开启分页
     */
    private boolean pageable = false;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getDataNum() {

        return dataNum;
    }

    public void setDataNum(Integer dataNum) {
        this.dataNum = dataNum;
    }

    public Integer getPageNum() {

        //可以思考一下这种写法
        this.pageNum = (dataNum + pageSize - 1) / pageSize;
        return pageNum;
    }

    /**
     * 默认不提供set方法
     * @return
     */
   /* public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }*/

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(boolean pageable) {
        this.pageable = pageable;
    }
}
