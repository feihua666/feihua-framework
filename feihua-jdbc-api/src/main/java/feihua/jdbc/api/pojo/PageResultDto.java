package feihua.jdbc.api.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果dto
 * Created by yangwei
 * Created at 2017/9/25 17:07
 */
public class PageResultDto<T> extends BaseDto{
    private List<T> data = new ArrayList<>();
    private Page page;

    public PageResultDto(){}

    public PageResultDto(List<T> data,Page page){
        this.data = data;
        this.page = page;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
