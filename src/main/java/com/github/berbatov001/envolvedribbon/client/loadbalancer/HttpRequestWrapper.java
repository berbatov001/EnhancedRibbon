package com.github.berbatov001.envolvedribbon.client.loadbalancer;

import com.netflix.loadbalancer.LoadBalancerContext;
import com.netflix.loadbalancer.Server;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.net.URI;

public class HttpRequestWrapper implements HttpRequest {

    private final HttpRequest request;

    private final Server server;

    private final LoadBalancerContext loadBalancerContext;

    HttpRequestWrapper(HttpRequest request, Server server, LoadBalancerContext loadBalancerContext){
        this.request = request;
        this.server = server;
        this.loadBalancerContext = loadBalancerContext;
    }

    @Override
    @Nullable
    public String getMethodValue() {
        return this.request.getMethodValue();
    }

    @Override
    @Nullable
    public HttpHeaders getHeaders() {
        return request.getHeaders();
    }

    @Override
    @Nullable
    public URI getURI() {
        Assert.notNull(request, "request 不能为空。");
        Assert.notNull(server, "server 不能为空。");
        Assert.notNull(loadBalancerContext, "loadBalancerContext 不能为空。");
        URI originalUri = request.getURI();
        return loadBalancerContext.reconstructURIWithServer(server, originalUri);
    }
}
