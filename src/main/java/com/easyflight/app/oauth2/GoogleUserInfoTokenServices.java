package com.easyflight.app.oauth2;

/**
 * Created by Victor Ikoro on 7/13/2017.
 */


import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;


public class GoogleUserInfoTokenServices extends UserInfoTokenServices {


    public GoogleUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
    }

    protected Object getPrincipal(Map<String, Object> map) {
        //Return user object map
        return map;
    }

}