package com.github.berbatov001.enhancedribbon.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
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

    /**
     * 没有参数，直接访问。
     *
     * @param serviceName 目标服务名
     * @param path 目标接口
     * @param method HTTP方法名，GET POST PUT DELETE等
     * @param responseType 返回值类型
     * @param <T> 指定返回值类型
     * @return 通过该方法参数responseType的反省指定的是什么类型，这里就返回什么类型。
     */
    public <T> T call(String serviceName, String path, HttpMethod method, Class<T> responseType) {
        return call(serviceName, path, method, responseType, null);
    }

    public <T> T call(String serviceName, String path, HttpMethod method, Class<T> responseType, String body) {
        return call(serviceName, path, method, responseType, body, new HttpHeaders());
    }

    /**
     * 用于访问请求体是String类型的接口
     *
     * @param serviceName 目标服务名
     * @param path  目标接口
     * @param method  HTTP方法名，GET POST PUT DELETE等
     * @param responseType  返回值类型
     * @param body  请求体
     * @param headers  请求header
     * @param <T>  指定返回值类型
     * @return  通过该方法参数responseType的反省指定的是什么类型，这里就返回什么类型。
     */
    public <T> T call(String serviceName, String path, HttpMethod method, Class<T> responseType, String body, HttpHeaders headers) {
        validateParameters(serviceName, path, method, responseType);
        path = path.startsWith("/") ? path : "/" + path;
        String url = PROTOCOL_PREFIX + serviceName + path;
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, method, requestEntity, responseType);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("RemoteClient调用远端服务发生异常：【service={}，errorMessage={}】。", serviceName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 最通用的方法。可以自定义请求体，自定义请求头。
     *
     * @param serviceName 目标服务名
     * @param path 目标接口
     * @param method HTTP方法名，GET POST PUT DELETE等
     * @param headers 自定义请求头
     * @param body  自定义请求头
     * @param returnType  返回值类型
     * @param <T>  定义请求体类型
     * @param <R>  定义返回值类型
     * @return  返回值
     */
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
