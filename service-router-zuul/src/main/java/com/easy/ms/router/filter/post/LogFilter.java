package com.easy.ms.router.filter.post;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.netflix.zuul.filters.support.ResettableServletInputStreamWrapper;
import org.springframework.util.StreamUtils;

import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {

        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("url: {}", request.getRequestURL());
        log.info("method: {}", request.getMethod());
        Gson gson = new Gson();
        log.info("request params: {}", gson.toJson(request.getParameterMap()));
        log.info("request body: {}", getRequestBody(request));
        log.info("response:{}", getResponseBody(ctx));
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            return StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

}
