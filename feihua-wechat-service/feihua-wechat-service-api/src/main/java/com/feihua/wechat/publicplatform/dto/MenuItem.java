package com.feihua.wechat.publicplatform.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/8/16 17:58
 */
public class MenuItem implements Serializable {

    public MenuItem(){

    }
    public MenuItem(String type){
        this.type = type;
    }
    private String name;
    private String type;
    private List<MenuItem> sub_button;

    public void addSubButton(MenuItem menuItem){
        if (sub_button == null) {
            sub_button = new ArrayList<>();
        }
        sub_button.add(menuItem);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<MenuItem> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<MenuItem> sub_button) {
        this.sub_button = sub_button;
    }
}
