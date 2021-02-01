package com.github.berbatov001.envolvedribbon.client.loadbalancer;

import com.github.berbatov001.envolvedribbon.netfix.ribbon.RibbonClient;
import com.github.berbatov001.envolvedribbon.netfix.ribbon.RibbonClientFactory;
import com.netflix.loadbalancer.LoadBalancerContext;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;

public class LoadBalancerInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadBalancerInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        final URI originalUri = httpRequest.getURI();
        String serviceName = originalUri.getHost();
        HttpHeaders headers = httpRequest.getHeaders();
        assemblyHttpHeaders(headers);
        return execute(serviceName, httpRequest, body, clientHttpRequestExecution);
    }

    private ClientHttpResponse execute(String serviceName, HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        RibbonClient ribbonClient = RibbonClientFactory.getRibbonClient(serviceName);
        Server server = ribbonClient.getServer();
        LoadBalancerContext loadBalancerContext = ribbonClient.getLoadBalancerContext();
        Assert.notNull(server, serviceName + "没有可用实例。");
        LOGGER.info("开始访问服务【{}】的接口{}，具体实例信息{}。", serviceName, request.getURI().getPath(), server.getHostPort());
        HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper(request, server, loadBalancerContext);
        return execution.execute(httpRequestWrapper, body);
    }


    /**
     * 将原始HttpRequest的Header复制到新Request中，实现header透传。
     * @param headers 新请求的header。
     */
    private void assemblyHttpHeaders(HttpHeaders headers) {
        if (headers != null) {
            if(RequestContextHolder.getRequestAttributes() == null){
                return;
            }
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                if(!headers.containsKey(key) && "content-length".equalsIgnoreCase(key)){
                    String value = httpServletRequest.getHeader(key);
                    headers.add(key, value);
                }
            }
        }
    }
}
