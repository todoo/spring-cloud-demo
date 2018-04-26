package com.easy.ms.service.constant;

public enum ResultCodeEnum {
    SUCCESS("0", "success"), 
    REQUEST_PARAM_ERROR("000001", "request param error");

    String code, description;

    private ResultCodeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据code获取类型
     * 
     * @param code
     * @return
     */
    public static ResultCodeEnum fromCode(String code) {
        for (ResultCodeEnum rt : ResultCodeEnum.values()) {
            if (rt.getCode().equals(code)) {
                return rt;
            }
        }
        return null;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
