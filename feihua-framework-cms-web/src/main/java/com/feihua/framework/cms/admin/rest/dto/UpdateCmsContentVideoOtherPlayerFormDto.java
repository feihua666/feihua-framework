package com.feihua.framework.cms.admin.rest.dto;

import com.feihua.framework.rest.dto.UpdateFormDto;

/**
 * This class corresponds to the database table cms_content_video_other_player
 * 2018-12-07 09:19:31
*/
public class UpdateCmsContentVideoOtherPlayerFormDto extends UpdateFormDto {

    private String id;

    private String player;

    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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