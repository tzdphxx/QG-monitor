package com.qg.common.utils;

public class Constants {
    /**
     *  权限标识
     *  0:不可见
     *  1：可读
     *  2：可操作
     */
    public static final Integer PERMISSION_NOT_VISIBLE = 0;
    public static final Integer PERMISSION_READ = 1;
    public static final Integer PERMISSION_OP = 2;

    /**
     * 用户角色
     * 0：老板
     * 1：管理员
     * 2：成员
     */
    public static final Integer USER_ROLE_BOSS = 0;
    public static final Integer USER_ROLE_ADMIN = 1;
    public static final Integer USER_ROLE_MEMBER = 2;

    /**
     * 通知是否已读
     */
    public static final Integer IS_READ = 1;
    public static final Integer IS_NOT_READ = 0;

    /**
     * 通知发送者是否存在
     */
    public static final Integer IS_SENDER_EXIST = 1;
    public static final Integer IS_SENDER_NOT_EXIST = 0;

    /**
     * 错误是否处理
     */
    public static final Integer IS_HANDLE = 1;
    public static final Integer IS_NOT_HANDLE = 0;

    /**
     * 报警升级时间：
     * 40min未处理发给管理员
     * 80min未处理发给老板
     *
     */
    public static final Integer ALERT_UPGRADE_TIME = 5;
    public static final Integer ALERT_UPGRADE_TIME_BOSS = 10;

    /**
     * 信息内容
     * 1. 有新的委派信息！
     * 2. 有错误仍未被处理!
     * 3、你有新的错误需要处理！
     */
    public static final String ALERT_CONTENT_NEW = "有新的错误需要处理！";
    public static final String ALERT_CONTENT_HANDLE = "有错误仍未被处理!";
    public static final String ALERT_CONTENT_DELEGATE = "有新的委派信息！";

}
