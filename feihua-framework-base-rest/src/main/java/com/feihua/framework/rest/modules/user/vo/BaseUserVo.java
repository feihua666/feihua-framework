package com.feihua.framework.rest.modules.user.vo;


import com.feihua.framework.base.modules.user.dto.BaseUserDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户vo
 * Created by yangwei
 * Created at 2018/1/19 10:52
 */
public class BaseUserVo {

    private String id;
    private String account;
    private String locked;
    private String nickname;
    private String gender;
    private String serialNo;
    private String dataOfficeId;
    private String photo;


    public BaseUserVo() {}
    public BaseUserVo(BaseUserDto userDto) {
        this.id = userDto.getId();
        this.locked = userDto.getLocked();
        this.nickname = userDto.getNickname();
        this.gender = userDto.getGender();
        this.serialNo = userDto.getSerialNo();
        this.dataOfficeId = userDto.getDataOfficeId();
        this.photo = userDto.getPhoto();
    }

    public static List<BaseUserVo> toList(List<BaseUserDto> userDtoList){

        if(userDtoList != null) {
            List<BaseUserVo> result = new ArrayList<>(userDtoList.size());
            for (int i = 0; i < userDtoList.size(); i++) {
                result.add(new BaseUserVo(userDtoList.get(i)));
            }
            return result;
        }
        return null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getDataOfficeId() {
        return dataOfficeId;
    }

    public void setDataOfficeId(String dataOfficeId) {
        this.dataOfficeId = dataOfficeId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
