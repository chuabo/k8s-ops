package com.github.puhaiyang.k8sops.constants;
/**
 * @author puhaiyang
 * @date 2021/4/25 20:45
 * ResultCode
 */
public enum ResultCode {
    /**
     * 成功-0000
     */
    SUCCESS("0000", "成功"),
    /**
     * 出错了-9999
     */
    FAIL("9999", "出错了");
    private String code;
    private String errorMsg;

    private ResultCode(String code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
