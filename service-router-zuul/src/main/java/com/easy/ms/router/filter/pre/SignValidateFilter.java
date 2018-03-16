package com.easy.ms.router.filter.pre;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.StreamUtils;

import com.easy.ms.router.util.MD5;
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
public class SignValidateFilter extends ZuulFilter {
    private final static String SIGN_PARAM_NAME = "_sign";
    private final static String BODY_PARAM_NAME = "_body";
    private final static String SECRET_PARAM_NAME = "_keySecret";
    private final static String KEY_SECRET = "asdjasdnn";

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
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> signParamMap = new TreeMap<>();
        String requestSign = "";
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) == null || params.get(key).length == 0) {
                    continue;
                }
                if (StringUtils.isBlank(params.get(key)[0])) {
                    // 空值不参与签名
                    continue;
                }
                if (SIGN_PARAM_NAME.equals(key)) {
                    // sign本身不参与签名
                    requestSign = params.get(key)[0];
                    continue;
                }
                signParamMap.put(key, params.get(key)[0]);
            }
        }

        if (StringUtils.isBlank(requestSign)) {
            signValidateFail(ctx);
            return null;
        }

        // body param
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

        // get sign string
        String signString = "";
        for (String key : signParamMap.keySet()) {
            signString = signString + key + "=" + signParamMap.get(key) + "&";
        }
        signString = signString + SECRET_PARAM_NAME + "=" + KEY_SECRET;
        // md5
        String sign = MD5.MD5Encode(signString).toUpperCase(Locale.CHINA);
        // base64
        sign = new String(Base64.encodeBase64(sign.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8"))
                .toUpperCase(Locale.CHINA);
        if (!requestSign.equals(sign)) {
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
        ctx.setResponseBody("{\"result\":\"非法请求!\"}");
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
    }

}
