package com.github.berbatov001.envolvedribbon.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

public class RemoteClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClient.class);

    private static final String PROTOCOL_PREFIX = "http://";

    private RestTemplate restTemplate;

    public RemoteClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T call(String serviceName, String path, HttpMethod method, Class<T> responseType) {
        return call(serviceName, path, method, responseType, null);
    }

    public <T> T call(String serviceName, String path, HttpMethod method, Class<T> responseType, String json) {
        return call(serviceName, path, method, responseType, json, new HttpHeaders());
    }

    public <T> T call(String serviceName, String path, HttpMethod method, Class<T> responseType, String json, HttpHeaders headers) {
        validateParameters(serviceName, path, method, responseType);
        path = path.startsWith("/") ? path : "/" + path;
        String url = PROTOCOL_PREFIX + serviceName + path;
        HttpEntity<Object> requestEntity = new HttpEntity<>(json, headers);
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, method, requestEntity, responseType);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("RemoteClient调用远端服务发生异常：【service={}，errorMessage={}】。", serviceName, e.getMessage(), e);
            throw e;
        }
    }

    public <T, R> ResponseEntity<R> callForResponseEntity(String serviceName, String path, HttpMethod method, HttpHeaders headers, T body, Class<R> returnType) {
        path = path.startsWith("/") ? path : "/" + path;
        String url = PROTOCOL_PREFIX + serviceName + path;
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);
        try {
            return restTemplate.exchange(url, method, requestEntity, returnType);
        } catch (Exception e) {
            LOGGER.error("RemoteClient调用远端服务发生异常：【service={}，errorMessage={}】。", serviceName, e.getMessage(), e);
            throw e;
        }
    }

    public InputStream download(String serviceName, String fileUrl) throws IOException {
        //RibbonClient ribbonClient = RibbonClientFactory
        return null;
    }

    private <T> void validateParameters(String serviceName, String path, HttpMethod method, Class<T> responseType){
        Assert.notNull(serviceName,"参数serviceName不能为空。");
        Assert.notNull(path,"参数path不能为空。");
        Assert.notNull(method,"参数method不能为空。");
        Assert.notNull(responseType,"参数responseType不能为空。");
    }
}
