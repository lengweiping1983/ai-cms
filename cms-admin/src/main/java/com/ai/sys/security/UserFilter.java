package com.ai.sys.security;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {

    public static final String X_R = "X-Requested-With";
    public static final String X_R_VALUE = "XMLHttpRequest";

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        if (isAccessAllowed(request, response, null)) {
            return;
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String xrv = httpServletRequest.getHeader(X_R);
        if (xrv != null && xrv.equalsIgnoreCase(X_R_VALUE)) {
            System.out.println("ajax redirectToLogin.");
            httpServletResponse.setStatus(911);
            // httpServletResponse.sendError(911, "Authentication Failed.");
        } else {
            System.out.println("general redirectToLogin.");

            String loginUrl = getLoginUrl();
            httpServletResponse.addHeader("location", httpServletRequest.getContextPath() + loginUrl);
            httpServletResponse.setStatus(302);
            System.out.println("general redirectToLogin 302.");

            // super.redirectToLogin(request, response);
        }
    }
}
