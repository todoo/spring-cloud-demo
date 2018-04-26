package com.easy.ms.router.filter.pre;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.easy.ms.router.service.AppService;
import com.easy.ms.service.base.constants.SignTypeEnum;
import com.easy.ms.service.base.utils.RSAUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 参数签名校验过滤器
 * 
 * @author tkx
 *
 */
@Slf4j
@Component
public class RestApiSignValidateFilter extends ZuulFilter {
    private final static String BODY_PARAM_NAME = "body";

    @Autowired
    private AppService appService;

    @Override
    public boolean shouldFilter() {
        // 非debug模式调用
        RequestContext ctx = RequestContext.getCurrentContext();
        return !ctx.debugRequest();
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        // 从头部信息获取appid相关信息
        String appId = request.getHeader("appId");
        String sign = request.getHeader("sign");
        String signType = request.getHeader("signType");
        String timestamp = request.getHeader("timestamp");

        if (StringUtils.isBlank(appId) || StringUtils.isBlank(sign) || StringUtils.isBlank(signType)
                || StringUtils.isBlank(timestamp)) {
            signValidateFail(ctx);
            return null;
        }

        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> signParamMap = new TreeMap<>();
        signParamMap.put("appId", appId);
        signParamMap.put("signType", signType);
        signParamMap.put("timestamp", timestamp);

        // url参数
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) == null || params.get(key).length == 0) {
                    continue;
                }
                if (StringUtils.isBlank(params.get(key)[0])) {
                    // 空值不参与签名
                    continue;
                }
                signParamMap.put(key, params.get(key)[0]);
            }
        }

        // body参数
        try {
            if (request.getInputStream() != null) {
                String bodyParam = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));

                if (StringUtils.isNotBlank(bodyParam)) {
                    signParamMap.put(BODY_PARAM_NAME, bodyParam);
                }
            }
        } catch (IOException e) {
            log.info("get body param error:", e);
        }

        // 按字段字典排序生成待签名字符串
        String signString = "";
        for (String key : signParamMap.keySet()) {
            signString = signString + "&" + key + "=" + signParamMap.get(key);
        }
        signString = signString.replaceFirst("&", "");

        if (!this.signVerify(appId, signString, signType, sign)) {
            // 签名不相同
            signValidateFail(ctx);
        }
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // DebugFilter后执行
        return FilterConstants.DEBUG_FILTER_ORDER + 1;
    }

    private void signValidateFail(RequestContext ctx) {
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(401);
        ctx.setResponseBody("{\"result\":\"验签失败\"}");
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
    }

    private boolean signVerify(String appId, String signStr, String signType, String sign) {
        if (SignTypeEnum.RSA.getCode().equalsIgnoreCase(signType)) {
            // 根据appId获取对应公钥
            String publicKey = appService.getAppRsaPublicKey(appId);

            try {
                boolean verify = RSAUtils.verify(signStr.getBytes("UTF-8"), publicKey, sign);
                return verify;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

}
