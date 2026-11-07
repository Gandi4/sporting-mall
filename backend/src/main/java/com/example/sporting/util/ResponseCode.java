package com.example.sporting.util;

public enum ResponseCode {
    SUCCESS(200, "操作成功!"),
    FAIL(400, "操作失败!"),              // 新增：用于账号密码错、参数错误等业务失败
    ERROR(500, "服务器内部错误!"),
    NEED_LOGIN(401, "请先登录!"),
    TOKEN_FAILED(403, "token失效,请重新登录!");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}