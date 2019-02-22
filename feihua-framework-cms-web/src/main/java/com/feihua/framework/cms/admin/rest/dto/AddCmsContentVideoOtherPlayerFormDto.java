package com.feihua.framework.cms.admin.rest.dto;

/**
 * This class corresponds to the database table cms_content_video_other_player
 * 2018-12-07 09:19:31
*/
public class AddCmsContentVideoOtherPlayerFormDto {

    private String player;

    private String url;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}