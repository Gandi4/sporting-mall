package com.example.sporting.util;

import com.example.sporting.entity.Banner;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 统一返回结果
 */
@Data
@Accessors(chain = true)
public class R {
    private Integer code;
    private boolean success;
    private String msg;
    private Object data;

    // 成功 - 无数据
    public static R ok() {
        R r = new R();
        r.setCode(ResponseCode.SUCCESS.getCode());
        r.setSuccess(true);
        r.setMsg(ResponseCode.SUCCESS.getDesc());
        return r;
    }

    // 成功 - 自定义消息
    public static R ok(String msg) {
        R r = new R();
        r.setCode(ResponseCode.SUCCESS.getCode());
        r.setSuccess(true);
        r.setMsg(msg);
        return r;
    }

    // 成功 - 带数据
    public static R ok(Object data) {
        R r = new R();
        r.setCode(ResponseCode.SUCCESS.getCode());
        r.setSuccess(true);
        r.setMsg(ResponseCode.SUCCESS.getDesc());
        r.setData(data);
        return r;
    }

    // 成功 - 自定义消息 + 数据
    public static R ok(String msg, Object data) {
        R r = new R();
        r.setCode(ResponseCode.SUCCESS.getCode());
        r.setSuccess(true);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    // 业务失败（400）
    public static R fail() {
        R r = new R();
        r.setCode(ResponseCode.FAIL.getCode());
        r.setSuccess(false);
        r.setMsg(ResponseCode.FAIL.getDesc());
        return r;
    }

    public static R fail(String msg) {
        R r = new R();
        r.setCode(ResponseCode.FAIL.getCode());
        r.setSuccess(false);
        r.setMsg(msg);
        return r;
    }

    // 服务器错误（500）
    public static R error() {
        R r = new R();
        r.setCode(ResponseCode.ERROR.getCode());
        r.setSuccess(false);
        r.setMsg(ResponseCode.ERROR.getDesc());
        return r;
    }

    public static R error(String msg) {
        R r = new R();
        r.setCode(ResponseCode.ERROR.getCode());
        r.setSuccess(false);
        r.setMsg(msg);
        return r;
    }


}