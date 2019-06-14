package com.feihua.framework.constants;

/**
 * 跟字典管理配置一致，属于系统类型的
 * Created by yangwei
 * Created at 2018/3/14 17:14
 */
public class DictEnum {


    /**
     * 登录方式
     */
    public enum LoginType {
        //帐号登录
        ACCOUNT,
        //邮箱登录
        EMAIL,
        //QQ登录
        QQ,
        //微信公众平台
        WX_PLATFORM,
        // 微信小程序
        WX_MINIPROGRAM,
        //手机号登录
        MOBILE,
        // 二维码登录
        QRCODE
    }

    /**
     * 登录客户端类型
     */
    public enum LoginClientType {
        pc,
        h5,
        wx_miniprogram,
        wx_publicplatform,
        app_android,
        app_ios,
        virtual_wx_miniprogram,
        virtual_wx_publicplatform,
        virtual_sms,
        virtual_email
    }

    /**
     * 性别
     */
    public enum Gender {
        male,    // 男
        female,  // 女
        unknown,  // 未知
        secret  // 保密
    }

    /**
     * 机构数据范围
     */
    public enum OfficeDataScope {
        useroffice,     // 所在机构
        userofficedown, // 所在机构及以下机构
        roleoffice,     // 角色所在机构
        all,            // 所有机构
        roleofficedown, // 角色所在机构及以下机构
        postoffice,     // 岗位所在机构
        postofficedown, // 岗位所在机构及以下机构
        self,           // 自定义
        no              // 无权限
    }

    /**
     * 用户数据范围
     */
    public enum UserDataScope {
        userofficedown, // 所在机构及以下机构用户
        roleoffice,     // 角色所在机构用户
        roleofficedown, // 角色所在机构及以下机构用户
        postoffice,     // 岗位所在机构用户
        postofficedown, // 岗位所在机构及以下机构用户
        officedata,     // 机构数据范围下用户
        rolebind,       // 角色绑定的用户
        all,            // 所有用户
        personal,       // 本人
        useroffice,     // 所在机构下用户
        no              // 无权限

    }

    /**
     * 角色数据范围
     */
    public enum RoleDataScope {
        useroffice,      // 用户所在机构下的角色
        userofficedown,  // 用户所在机构及以下机构角色
        roleofficedown,  // 角色所在机构及以下机构角色
        roleoffice,      // 角色所在机构下角色
        postofficedown,  // 岗位所在机构及以下机构角色
        postoffice,      // 岗位所在机构下角色
        office,          // 机构数据范围下的角色
        assign,          // 分配的角色
        assigndown,      // 分配的角色及以下角色
        all,             // 所有角色
        self,            // 自定义
        no               // 无权限

        }

    /**
     * 数据资源
     */
    public enum DataResource {
        user,    // 用户数据资源
        role,    // 角色数据资源
        office,   // 机构数据资源
        dataScope, //数据范围资源
        dict      //字典数据资源
    }
    /**
     * 角色数据范围
     */
    public enum FunctionResourceDataScope {
        self,          // 自定义功能勾选
        all            // 所有功能
    }
    /**
     * 数据范围的数据范围
     */
    public enum DataScopeDataScope {
        useroffice,      // 用户所在机构下的数据范围
        userofficedown,  // 用户所在机构及以下机构数据范围
        roleofficedown,  // 角色所在机构及以下机构数据范围
        roleoffice,      // 角色所在机构下数据范围
        postofficedown,  // 岗位所在机构及以下机构数据范围
        postoffice,      // 岗位所在机构下数据范围
        office,          // 机构数据范围下的数据范围
        all,             // 所有数据范围
        self,            // 自定义
        no               // 无权限

    }
    /**
     * 数据范围类型
     */
    public enum DataScopeType {
        allData,          // 所有数据
        defaultType      // 默认类型
    }

    /**
     * 字典数据范围
     */
    public enum DictDataScope {
        all,                                // 所有
        publics ,                           // 公共
        publics_userofficecompany,          //公共 + 用户所在机构的公司
        userofficecompany,                  // 用户所在机构的公司
        publics_userroleofficecompany,      //公共 + 用户角色所在机构的公司
        userroleofficecompany,              // 用户角色所在机构的公司
        publics_userpostofficecompany,      //公共 + 用户岗位所在机构的公司
        userpostofficecompany,              // 用户岗位所在机构的公司
        no     // 无权限
    }
    /**
     * 岗位职务数据范围
     */
    public enum PostJobDataScope {
        all,                                // 所有
        publics ,                           // 公共
        publics_useroffice,          //公共 + 用户所在机构
        useroffice,                  // 用户所在机构
        publics_userroleoffice,      //公共 + 用户角色所在机构
        userroleoffice,              // 用户角色所在机构
        publics_userpostoffice,      //公共 + 用户岗位所在机构
        userpostoffice,              // 用户岗位所在机构
        no     // 无权限
    }
    /**
     * 岗位数据范围
     */
    public enum PostDataScope {
        all,                                // 所有
        publics ,                           // 公共
        publics_useroffice,          //公共 + 用户所在机构
        useroffice,                  // 用户所在机构
        publics_userroleoffice,      //公共 + 用户角色所在机构
        userroleoffice,              // 用户角色所在机构
        publics_userpostoffice,      //公共 + 用户岗位所在机构
        userpostoffice,              // 用户岗位所在机构
        no     // 无权限
    }
    public enum RoleType {
        superadmin,    //超级管理员
        normal,   //普通
        departmentmanager,     // 部门负责人
        companymanager, // 公司负责人
        hr// 人事
    }
    public enum OfficeType {
        company,    //公司
        department   //部门
    }
    /**
     * 文件上传与导出分类
     */
    public enum UploadDownloadType {
        export,    //导出
        upload   //上传
    }
    /**
     * 文件分类
     */
    public enum FileType {
        image,    //图片
        video,   //视频
        audio,   //音频
        pdf,      //pdf
        other      //其它
    }

    /**
     * 用户关注的渠道来源，微信用户怎么来的分类
     */
    public enum WeixinUserHowFrom {
        webAuthorize,   //网页授权
        // 下面的基本跟微信文档一致，注意ADD_SCENEPROFILE_LINK微信文档是ADD_SCENEPROFILE LINK 最后面一个下划线没有，需要处理
        ADD_SCENE_SEARCH,              // 公众号搜索
        ADD_SCENE_ACCOUNT_MIGRATION,   // 公众号迁移
        ADD_SCENE_PROFILE_CARD,       // 名片分享
        ADD_SCENE_QR_CODE,            // 扫描二维码
        ADD_SCENEPROFILE_LINK,        // 图文页内名称点击
        ADD_SCENE_PROFILE_ITEM,       // 图文页右上角菜单
        ADD_SCENE_PAID,               // 支付后关注
        ADD_SCENE_OTHERS,             // 其他
    }

    /**
     * 微信用户状态
     */
    public enum WeixinUserStatus {
        subscribe,       //关注中
        unsubscribe,   //取消关注
    }
    /**
     * 微信菜单类型
     */
    public enum WeixinMenuType {
        click,                //点击推事件
        view,                //跳转URL
        scancode_push,       //扫码推事件
        scancode_waitmsg,    //扫码推事件且弹出“消息接收中”
        pic_sysphoto,        //弹出系统拍照发图
        pic_photo_or_album,   //弹出拍照或者相册发图
        pic_weixin,         //弹出微信相册发图器
        location_select,    //弹出地理位置选择器
        media_id,           //下发消息（除文本消息）
        view_limited,        //跳转图文消息URL
        miniprogram        //小程序
    }
    /**
     * 微信账号类型
     */
    public enum WxAccountType {
        /**
         * 小程序
         */
        weixin_miniprogram,

        /**
         * 公众号
         */
        weixin_publicplatform
    }

    /**
     * 微信消息类型
     */
    public enum WxMsgType {
        /**
         * 文本
         */
        text,
        /**
         * 图文
         */
        news,

        /**
         * 图片
         */
        image
    }

    /**
     * 消息状态
     */
    public enum MessageState {
        sended,      //已发送
        to_be_sended,   //待发送
        sending   //发送中
    }

    /**
     * 客户端消息支持类型
     * 目前这么多
     */
    public enum MessageClientType{
        sms,                          // 短信
        email,                        // 邮件
        wx_publicplatform_template,   // 微信公众号模板消息
        wx_miniprogram_template,     // 微信小程序模板消息
        app_message_push             // app消息推送
    }
    /**
     * 消息发送目标类型
     */
    public enum MessageTargetType {
        all,            // 所有人
        multi_people,   //多个人
        multi_office,      //机构下的人
        multi_role,           // 角色绑定的人
        multi_area           // 区域下的人
    }
    public enum CmsContentType {
        article, // 文章/图文
        library, // 文库
        gallery, //图库
        download, //下载
        video, //视频
        audio, //音频
    }
    public enum XmlOrJsonType {
        xml,
        json
    }

    /**
     * 任务计划状态
     */
    public enum SchedulerStatus {
        offline, //脱机，不在线
        normal, //正常，任务计划中
        error, //错误
        blocked, //阻塞
        complete, //完成
        pause //暂停
    }
}
