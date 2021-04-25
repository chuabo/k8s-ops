package com.github.puhaiyang.k8sops.bean;

import com.github.puhaiyang.k8sops.constants.ResultCode;

import java.io.Serializable;

/**
 * @author puhaiyang
 * @date 2021/4/25 20:46
 * Result
 */
public class Result<T> implements Serializable {
    private String code;
    private String msg;
    private T data;

    public Result() {
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        return result;
    }

    public static <T> Result<T> fail(ResultCode commonErrorCode) {
        return fail(commonErrorCode.getCode(), commonErrorCode.getErrorMsg());
    }

    public static <T> Result<T> fail(Result<T> result) {
        return fail(result.getCode(), result.getMsg());
    }

    public static <T> Result<T> fail(String code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> build(String code, String msg, T data) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> build(String code, T data) {
        return build(code, (String) null, data);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
