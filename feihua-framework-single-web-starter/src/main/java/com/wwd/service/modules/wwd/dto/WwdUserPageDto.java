package com.wwd.service.modules.wwd.dto;

import feihua.jdbc.api.pojo.BaseDto;

import java.util.List;

/**
 * @Auther: wzn
 * @Date: 2018/5/18 11:55
 * @Description:
 */
public class WwdUserPageDto extends BaseDto {
    private WwdUserDto wwdUserDto;
    private WwdUserAreaDto wwdUserAreaDto;
    private List<WwdUserPicDto> wwdUserPicDtos;

    public WwdUserDto getWwdUserDto() {
        return wwdUserDto;
    }

    public void setWwdUserDto(WwdUserDto wwdUserDto) {
        this.wwdUserDto = wwdUserDto;
    }

    public WwdUserAreaDto getWwdUserAreaDto() {
        return wwdUserAreaDto;
    }

    public void setWwdUserAreaDto(WwdUserAreaDto wwdUserAreaDto) {
        this.wwdUserAreaDto = wwdUserAreaDto;
    }

    public List<WwdUserPicDto> getWwdUserPicDtos() {
        return wwdUserPicDtos;
    }

    public void setWwdUserPicDtos(List<WwdUserPicDto> wwdUserPicDtos) {
        this.wwdUserPicDtos = wwdUserPicDtos;
    }
}
