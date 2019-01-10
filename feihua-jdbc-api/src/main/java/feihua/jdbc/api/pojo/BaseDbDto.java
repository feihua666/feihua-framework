package feihua.jdbc.api.pojo;

/**
 * 和数据库对应字段的dto
 * base db dto
 * Created by yangwei
 * Created at 2017/6/6 17:13
 */
public class BaseDbDto<PK> extends BaseDto {

    private PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }
}
