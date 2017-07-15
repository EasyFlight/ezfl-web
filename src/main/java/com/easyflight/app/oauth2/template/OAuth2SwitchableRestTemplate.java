package com.easyflight.app.oauth2.template;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Victor Ikoro on 7/14/2017.
 */
public class OAuth2SwitchableRestTemplate extends RestTemplate implements OAuth2RestOperations {


    private OAuth2RestOperations selectedRestTemplate;
    private Map<String, OAuth2RestOperations>  templateMap ;

    public OAuth2SwitchableRestTemplate(OAuth2RestOperations defaultRestTemplate, String key) {
        templateMap = new ConcurrentHashMap<>();
        putTemplate(defaultRestTemplate,key);
        selectedRestTemplate = defaultRestTemplate;
    }
    
    @Override
    public OAuth2AccessToken getAccessToken() throws UserRedirectRequiredException {
        return  selectedRestTemplate.getAccessToken();
    }

    @Override
    public OAuth2ClientContext getOAuth2ClientContext() {
        return selectedRestTemplate.getOAuth2ClientContext();
    }

    @Override
    public OAuth2ProtectedResourceDetails getResource() {
        return selectedRestTemplate.getResource();
    }

    public void putTemplate(OAuth2RestOperations restTemplate, String key){
        assert restTemplate != null : "RestTemplate cannot be null";
        assert StringUtils.isNotEmpty(key) : "template key cannot be blank";
        templateMap.put(key, restTemplate);
    }
}
