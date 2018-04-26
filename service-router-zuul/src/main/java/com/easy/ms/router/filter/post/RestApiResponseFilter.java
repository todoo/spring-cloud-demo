package com.easy.ms.router.filter.post;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.netflix.zuul.filters.support.ResettableServletInputStreamWrapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.easy.ms.router.service.AppService;
import com.easy.ms.service.base.constants.SignTypeEnum;
import com.easy.ms.service.base.utils.DateUtils;
import com.easy.ms.service.base.utils.RSAUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 对返回结果进行签名
 * @author tkx
 *
 */
@Slf4j
@Component
public class RestApiResponseFilter extends ZuulFilter {
    @Autowired
    private AppService appService;

    @Override
    public boolean shouldFilter() {

        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        String appId = request.getHeader("appId");
        String signType = SignTypeEnum.RSA.getCode();
        String timestamp = DateUtils.formatDateTime(new Date());
        response.setHeader("appId", appId);

        response.setHeader("signType", signType);
        response.setHeader("timestamp", timestamp);

        Map<String, String> signParamMap = new TreeMap<>();
        signParamMap.put("appId", appId);
        signParamMap.put("signType", signType);
        signParamMap.put("timestamp", timestamp);
        String body = null;
        if ((body = this.getResponseBody(ctx)) != null) {
            signParamMap.put("body", body);
        }

        // 按字段字典排序生成待签名字符串
        String signString = "";
        for (String key : signParamMap.keySet()) {
            signString = signString + "&" + key + "=" + signParamMap.get(key);
        }
        signString = signString.replaceFirst("&", "");
        response.setHeader("sign", this.sign(appId, signString, signType));

        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 2;
    }

    private String sign(String appId, String signStr, String signType) {
        if (SignTypeEnum.RSA.getCode().equalsIgnoreCase(signType)) {
            // 加密私钥
            String privateKey = appService.getServerRsaPrivateKey(appId);
            
            try {
                RSAUtils.sign(signStr.getBytes("UTF-8"), privateKey);
            } catch (Exception e) {
                log.error("rsa sign error:", e);
                return null;
            }
        }

        return null;
    }

    private String getResponseBody(RequestContext ctx) {
        if (ctx.getResponseBody() != null) {
            return ctx.getResponseBody();
        }

        try {
            ResettableServletInputStreamWrapper resettableServletInputStreamWrapper = new ResettableServletInputStreamWrapper(
                    StreamUtils.copyToByteArray(ctx.getResponseDataStream()));
            ctx.setResponseDataStream(resettableServletInputStreamWrapper);
            String response = StreamUtils.copyToString(resettableServletInputStreamWrapper, Charset.forName("UTF-8"));
            resettableServletInputStreamWrapper.reset();
            return response;
        } catch (IOException e) {
            log.error("get response error:", e);
        }

        return null;

    }

}
