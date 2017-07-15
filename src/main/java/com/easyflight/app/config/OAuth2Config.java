package com.easyflight.app.config;

import com.easyflight.app.oauth2.template.OAuth2SwitchableRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Victor Ikoro on 7/15/2017.
 */

@Configuration
public class OAuth2Config {


    private final OAuth2ClientContext oauth2ClientContext;
    private final Environment environment;

    private OAuth2RestTemplate facebookRestTemplate;
    private OAuth2RestTemplate googleRestTemplate;

    @Autowired
    public OAuth2Config(OAuth2ClientContext oauth2ClientContext, Environment environment) {
        this.oauth2ClientContext = oauth2ClientContext;
        this.environment = environment;
    }

    @PostConstruct
    public void init(){
        facebookRestTemplate = new OAuth2RestTemplate(facebookResourceDetails(), oauth2ClientContext);
        googleRestTemplate = new OAuth2RestTemplate(googleResourceDetails(), oauth2ClientContext);

    }
    @Bean
    @ConfigurationProperties("facebook.client")
    public AuthorizationCodeResourceDetails facebookResourceDetails() {
        return new AuthorizationCodeResourceDetails();
    }


    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails googleResourceDetails() {
        AuthorizationCodeResourceDetails authorizationCodeResourceDetails = new AuthorizationCodeResourceDetails();
        List<String> scopes = new ArrayList<>();
        Collections.addAll(scopes, environment.getProperty("google.auth.scopes").split(","));
        authorizationCodeResourceDetails.setScope(scopes);
        return authorizationCodeResourceDetails;
    }


    @Bean
    public OAuth2SwitchableRestTemplate oAuth2RestTemplate(){
        OAuth2SwitchableRestTemplate restTemplate = new OAuth2SwitchableRestTemplate(googleRestTemplate, "google");
        restTemplate.putTemplate(facebookRestTemplate, "facebook");
        return restTemplate;
    }


}
