package com.easyflight.app.oauth2.template;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Victor Ikoro on 7/14/2017.
 */

public class OAuth2SwitchableRestTemplate implements OAuth2RestOperations {


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
        Assert.notNull(key, "RestTemplate cannot be null");
        Assert.hasText(key,"template key cannot be blank");
        templateMap.put(key, restTemplate);
    }

    public OAuth2RestOperations getTemplate(String key){
        Assert.hasText(key,"template key cannot be blank");
        return templateMap.get(key);

    }

    public void selectTemplate(String key){

        if(!templateMap.containsKey(key)){
            throw new RuntimeException("Template key doesn't exist");
        }
        this.selectedRestTemplate = templateMap.get(key);
    }


    @Override
    public <T> T getForObject(String s, Class<T> aClass, Object... objects) throws RestClientException {
        return selectedRestTemplate.getForObject(s, aClass, objects);
    }

    @Override
    public <T> T getForObject(String s, Class<T> aClass, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.getForObject(s, aClass, map);
    }

    @Override
    public <T> T getForObject(URI uri, Class<T> aClass) throws RestClientException {
        return selectedRestTemplate.getForObject(uri, aClass);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String s, Class<T> aClass, Object... objects) throws RestClientException {
        return selectedRestTemplate.getForEntity(s, aClass, objects);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String s, Class<T> aClass, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.getForEntity(s, aClass, map);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(URI uri, Class<T> aClass) throws RestClientException {
        return selectedRestTemplate.getForEntity(uri, aClass);
    }

    @Override
    public HttpHeaders headForHeaders(String s, Object... objects) throws RestClientException {
        return selectedRestTemplate.headForHeaders(s, objects);
    }

    @Override
    public HttpHeaders headForHeaders(String s, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.headForHeaders(s, map);
    }

    @Override
    public HttpHeaders headForHeaders(URI uri) throws RestClientException {
        return selectedRestTemplate.headForHeaders(uri);
    }

    @Override
    public URI postForLocation(String s, Object o, Object... objects) throws RestClientException {
        return selectedRestTemplate.postForLocation(s, o, objects);
    }

    @Override
    public URI postForLocation(String s, Object o, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.postForLocation(s, o, map);
    }

    @Override
    public URI postForLocation(URI uri, Object o) throws RestClientException {
        return selectedRestTemplate.postForLocation(uri, o);
    }

    @Override
    public <T> T postForObject(String s, Object o, Class<T> aClass, Object... objects) throws RestClientException {
        return selectedRestTemplate.postForObject(s, o, aClass, objects);
    }

    @Override
    public <T> T postForObject(String s, Object o, Class<T> aClass, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.postForObject(s, o, aClass, map);
    }

    @Override
    public <T> T postForObject(URI uri, Object o, Class<T> aClass) throws RestClientException {
        return selectedRestTemplate.postForObject(uri, o, aClass);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String s, Object o, Class<T> aClass, Object... objects) throws RestClientException {
        return selectedRestTemplate.postForEntity(s, o, aClass, objects);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String s, Object o, Class<T> aClass, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.postForEntity(s, o, aClass, map);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(URI uri, Object o, Class<T> aClass) throws RestClientException {
        return selectedRestTemplate.postForEntity(uri, o, aClass);
    }

    @Override
    public void put(String s, Object o, Object... objects) throws RestClientException {
        selectedRestTemplate.put(s, o, objects);
    }

    @Override
    public void put(String s, Object o, Map<String, ?> map) throws RestClientException {
        selectedRestTemplate.put(s, o, map);
    }

    @Override
    public void put(URI uri, Object o) throws RestClientException {
        selectedRestTemplate.put(uri, o);
    }

    @Override
    public <T> T patchForObject(String s, Object o, Class<T> aClass, Object... objects) throws RestClientException {
        return selectedRestTemplate.patchForObject(s, o, aClass, objects);
    }

    @Override
    public <T> T patchForObject(String s, Object o, Class<T> aClass, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.patchForObject(s, o, aClass, map);
    }

    @Override
    public <T> T patchForObject(URI uri, Object o, Class<T> aClass) throws RestClientException {
        return selectedRestTemplate.patchForObject(uri, o, aClass);
    }

    @Override
    public void delete(String s, Object... objects) throws RestClientException {
        selectedRestTemplate.delete(s, objects);
    }

    @Override
    public void delete(String s, Map<String, ?> map) throws RestClientException {
            selectedRestTemplate.delete(s, map);
    }

    @Override
    public void delete(URI uri) throws RestClientException {
        selectedRestTemplate.delete(uri);
    }

    @Override
    public Set<HttpMethod> optionsForAllow(String s, Object... objects) throws RestClientException {
        return selectedRestTemplate.optionsForAllow(s, objects);
    }

    @Override
    public Set<HttpMethod> optionsForAllow(String s, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.optionsForAllow(s, map);
    }

    @Override
    public Set<HttpMethod> optionsForAllow(URI uri) throws RestClientException {
        return selectedRestTemplate.optionsForAllow(uri);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String s, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> aClass, Object... objects) throws RestClientException {
        return selectedRestTemplate.exchange(s, httpMethod, httpEntity, aClass, objects);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String s, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> aClass, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.exchange(s, httpMethod, httpEntity, aClass, map);
    }

    @Override
    public <T> ResponseEntity<T> exchange(URI uri, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> aClass) throws RestClientException {
        return selectedRestTemplate.exchange(uri, httpMethod, httpEntity, aClass);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String s, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference, Object... objects) throws RestClientException {
        return selectedRestTemplate.exchange(s, httpMethod, httpEntity, parameterizedTypeReference, objects);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String s, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.exchange(s, httpMethod, httpEntity, parameterizedTypeReference, map);
    }

    @Override
    public <T> ResponseEntity<T> exchange(URI uri, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference) throws RestClientException {
        return selectedRestTemplate.exchange(uri, httpMethod, httpEntity, parameterizedTypeReference);
    }

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> aClass) throws RestClientException {
        return selectedRestTemplate.exchange(requestEntity, aClass);
    }

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> parameterizedTypeReference) throws RestClientException {
        return selectedRestTemplate.exchange(requestEntity, parameterizedTypeReference);
    }

    @Override
    public <T> T execute(String s, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... objects) throws RestClientException {
        return selectedRestTemplate.execute(s, httpMethod, requestCallback, responseExtractor, objects);
    }

    @Override
    public <T> T execute(String s, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> map) throws RestClientException {
        return selectedRestTemplate.execute(s, httpMethod, requestCallback, responseExtractor, map);
    }

    @Override
    public <T> T execute(URI uri, HttpMethod httpMethod, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        return selectedRestTemplate.execute(uri, httpMethod, requestCallback, responseExtractor);
    }
}
