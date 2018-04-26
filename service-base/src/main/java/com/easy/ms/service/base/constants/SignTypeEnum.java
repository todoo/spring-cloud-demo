package com.easy.ms.service.base.constants;

public enum SignTypeEnum {
    RSA("RSA");

    String code;

    private SignTypeEnum(String code) {
        this.code = code;
    }

    /**
     * 根据code获取类型
     * 
     * @param code
     * @return
     */
    public static SignTypeEnum fromCode(String code) {
        for (SignTypeEnum rt : SignTypeEnum.values()) {
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
}
