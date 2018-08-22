package com.feihua.wechat.publicplatform.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwei
 * Created at 2018/8/16 17:56
 */
public class Menu implements Serializable {
    private List<MenuItem> button;


    public void addMenuItem(MenuItem menuItem){
        if (button == null) {
            button = new ArrayList<>();
        }
        button.add(menuItem);
    }

    public List<MenuItem> getButton() {
        return button;
    }

    public void setButton(List<MenuItem> button) {
        this.button = button;
    }
}
