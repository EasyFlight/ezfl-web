package com.easyflight.app.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import java.util.Set;

/**
 * Created by Victor.Ikoro on 9/15/2017.
 */
//@Component
public class RelayTokenFilter extends ZuulFilter {

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        // Alter ignored headers as per: https://gitter.im/spring-cloud/spring-cloud?at=56fea31f11ea211749c3ed22
        Set<String> headers = (Set<String>) ctx.get("ignoredHeaders");
        // We need our JWT tokens relayed to resource servers
        headers.remove("authorization");

        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10000;
    }
}