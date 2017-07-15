package com.easyflight.app.config;

import com.easyflight.app.oauth2.MapUserInfoTokenServices;
import com.easyflight.app.oauth2.filter.FacebookOAuth2ProcessingFilter;
import com.easyflight.app.oauth2.filter.GoogleOAuth2ProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor.Ikoro on 7/11/2017.
 */

@EnableOAuth2Client
@Configuration
public class SSOSecurityConfig  extends WebSecurityConfigurerAdapter{

    private final OAuth2ClientContext oauth2ClientContext;
    private final Environment environment;
    private final AuthorizationCodeResourceDetails facebookResourceDetails;
    private final AuthorizationCodeResourceDetails googleResourceDetails;
    private final FacebookOAuth2ProcessingFilter facebookOAuth2ProcessingFilter;
    private final GoogleOAuth2ProcessingFilter googleOAuth2ProcessingFilter;

    private OAuth2RestTemplate facebookRestTemplate;
    private OAuth2RestTemplate googleRestTemplate;

    @Autowired
    public SSOSecurityConfig(OAuth2ClientContext oauth2ClientContext,
                             Environment environment,
                             AuthorizationCodeResourceDetails facebookResourceDetails,
                             AuthorizationCodeResourceDetails googleResourceDetails,
                             FacebookOAuth2ProcessingFilter facebookOAuth2ProcessingFilter, GoogleOAuth2ProcessingFilter googleOAuth2ProcessingFilter) {
        this.oauth2ClientContext = oauth2ClientContext;
        this.environment = environment;
        this.facebookResourceDetails = facebookResourceDetails;
        this.googleResourceDetails = googleResourceDetails;
        this.facebookOAuth2ProcessingFilter = facebookOAuth2ProcessingFilter;
        this.googleOAuth2ProcessingFilter = googleOAuth2ProcessingFilter;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/user/**").authenticated().anyRequest().permitAll().and()
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/google")).and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .logout().logoutSuccessUrl("/");
    }

    private Filter facebookSSOFilter() {
        UserInfoTokenServices tokenServices = new MapUserInfoTokenServices(facebookResource().getUserInfoUri(), facebookResourceDetails.getClientId());
        tokenServices.setRestTemplate(facebookRestTemplate);
        facebookOAuth2ProcessingFilter.setTokenServices(tokenServices);
        return facebookOAuth2ProcessingFilter;
    }

    private Filter  googleSSOFilter(){
        UserInfoTokenServices tokenServices = new MapUserInfoTokenServices(googleResource().getUserInfoUri(), googleResourceDetails.getClientId());
        tokenServices.setRestTemplate(googleRestTemplate);
        googleOAuth2ProcessingFilter.setTokenServices(tokenServices);
        return googleOAuth2ProcessingFilter;
    }

    private Filter ssoFilter(){
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(facebookSSOFilter());
        filters.add(googleSSOFilter());
        filter.setFilters(filters);
        return filter;

    }

    @Bean
    @ConfigurationProperties("facebook.resource")
    public ResourceServerProperties facebookResource() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }





}
