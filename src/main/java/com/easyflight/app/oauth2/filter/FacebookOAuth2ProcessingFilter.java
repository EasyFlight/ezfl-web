package com.easyflight.app.oauth2.filter;

import com.easyflight.app.oauth2.template.OAuth2SwitchableRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Victor Ikoro on 7/15/2017.
 */
@Component
public class FacebookOAuth2ProcessingFilter extends OAuth2ClientAuthenticationProcessingFilter {

    private OAuth2SwitchableRestTemplate oAuth2SwitchableRestTemplate;

    @Autowired
    public FacebookOAuth2ProcessingFilter(OAuth2SwitchableRestTemplate oAuth2SwitchableRestTemplate) {
        super("/login/facebook");
        this.oAuth2SwitchableRestTemplate = oAuth2SwitchableRestTemplate;
        this.restTemplate =  oAuth2SwitchableRestTemplate.getTemplate("facebook");
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        //Switch selected restTemplate to Facebook
        oAuth2SwitchableRestTemplate.selectTemplate("facebook");
    }

}
