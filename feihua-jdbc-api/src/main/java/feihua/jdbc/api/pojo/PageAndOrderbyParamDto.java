package feihua.jdbc.api.pojo;

/**
 * 分页和排序参数dto
 * Created by yangwei
 * Created at 2017/9/25 17:05
 */
public class PageAndOrderbyParamDto extends BaseDto{
    private Page page;

    private Orderby orderby;

    public PageAndOrderbyParamDto(){}
    public PageAndOrderbyParamDto(Page page){
        this.page = page;
    }
    public PageAndOrderbyParamDto(Page page,Orderby orderby){

        this.page = page;
        this.orderby = orderby;
    }


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Orderby getOrderby() {
        return orderby;
    }

    public void setOrderby(Orderby orderby) {
        this.orderby = orderby;
    }
}
