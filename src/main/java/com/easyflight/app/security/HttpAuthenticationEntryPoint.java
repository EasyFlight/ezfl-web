package com.easyflight.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor.Ikoro on 9/15/2017.
 */
public class HttpAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    protected final Log logger = LogFactory.getLog(this.getClass());

    public HttpAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {

        if (this.isPossibleAjaxRequest(request)) {
            this.sendUnauthorizedJsonResponse(response, exception);
        } else {
            super.commence(request, response, exception);
        }
    }

    private void sendUnauthorizedJsonResponse(HttpServletResponse response, AuthenticationException exception) {
        PrintWriter out = null;
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            out = response.getWriter();


            ObjectMapper objectMapper =  new ObjectMapper();
            Map<String, Object> data = new HashMap<>();
            data.put("code", 440);
            data.put("message", "SESSION_TIMEOUT");
            data.put("description", "Your session has expired.");

            objectMapper.writeValue(out, data);
            out.flush();


        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);

            }
        }

    }

    private boolean isPossibleAjaxRequest(HttpServletRequest request) {

        String requestedWithHeader = request.getHeader("X-Requested-With");
        String requestedWithJson = "xmlhttprequest";

        String acceptHeader = request.getHeader("Accept");
        String acceptsJson = "application/json";

        boolean isAcceptHeaderContainsJson = acceptHeader != null && acceptHeader.toLowerCase().contains(acceptsJson);
        boolean isRequestHeaderEqualJson = requestedWithHeader != null && requestedWithHeader.equalsIgnoreCase(requestedWithJson);
        return isAcceptHeaderContainsJson || isRequestHeaderEqualJson;
    }
}
