package com.easy.ms.router.model.mongo;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "gateway_application_info")
public class ApplicationInfo implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -5954048558834753161L;
    private String appId;
    private String rsaPublicKey;
    private String serverPrivateKey;
}
