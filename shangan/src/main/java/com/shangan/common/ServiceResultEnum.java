package com.shangan.common;

/**
 * @Author Alva
 * @CreateTime 2021/1/25 16:36
 */
public enum ServiceResultEnum {
    ERROR("error"),

    SUCCESS("success"),

    SAME_LOGIN_NAME_EXIST("用户名已存在！"),

    LOGIN_NAME_NULL("请输入登录名！"),

    LOGIN_NAME_IS_NOT_PHONE("请输入正确的手机号！"),

    LOGIN_PASSWORD_NULL("请输入密码！"),

    LOGIN_VERIFY_CODE_NULL("请输入验证码！"),

    LOGIN_VERIFY_CODE_ERROR("验证码错误！"),

    LOGIN_ERROR("登录失败！"),

    NOT_LOGIN_ERROR("未登录！"),

    TOKEN_EXPIRE_ERROR("无效认证！请重新登录！"),

    USER_NULL_ERROR("无效用户！请重新登录！"),

    LOGIN_USER_LOCKED_ERROR("用户已被禁止登录！"),

    ADMIN_NULL_ERROR("无效管理员账号！请重新登录！"),

    LOGIN_ADMIN_LOCKED_ERROR("该管理员已被禁止登录！"),

    DATA_NOT_EXIST("未查询到记录！"),

    DB_ERROR("database error");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
