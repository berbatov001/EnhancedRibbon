package com.github.berbatov001.enhancedribbon.client.support;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomizeClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

    private int defaultConnectionTimeout = -1;
    private int defaultReadTimeout = -1;

    private Map<String, RestTemplateConnectionProperties.ConnectionConfiguration> connectionConfigurationMap = new LinkedHashMap<>();

    public void enhance(RestTemplateConnectionProperties restTemplateConnectionProperties) {
        Map<String, RestTemplateConnectionProperties.ConnectionConfiguration> connections = restTemplateConnectionProperties.getConnections();
        Map<String, RestTemplateConnectionProperties.ConnectionConfiguration> newConnectionMap = new LinkedHashMap<>();
        connections.forEach((group, connectionConfiguration) -> {
            String[] urls = connectionConfiguration.getUrl().split(",");
            Arrays.stream(urls).filter(StringUtils::hasLength).forEach(api -> {
                newConnectionMap.put(api, connectionConfiguration);
            });
        });
        connectionConfigurationMap = newConnectionMap;
        defaultConnectionTimeout = restTemplateConnectionProperties.getDefaultConnectTimeout();
        defaultReadTimeout = restTemplateConnectionProperties.getDefaultReadTimeout();
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection, @NonNull String httpMethod) throws IOException {
        int connectTimeout = -1;
        int readTimeout = -1;
        String path = connection.getURL().getPath();
        RestTemplateConnectionProperties.ConnectionConfiguration connectionConfiguration = connectionConfigurationMap.get(path);
        if (connectionConfiguration != null && connectionConfiguration.getConnectionTimeout() >= 0) {
            connectTimeout = connectionConfiguration.getConnectionTimeout();
        } else if (defaultConnectionTimeout >= 0) {
            connectTimeout = defaultConnectionTimeout;
        }
        if (connectionConfiguration != null && connectionConfiguration.getReadTimeout() >= 0) {
            readTimeout = connectionConfiguration.getReadTimeout();
        } else if (defaultReadTimeout >= 0) {
            readTimeout = defaultReadTimeout;
        }
        if (connectTimeout >= 0) {
            connection.setConnectTimeout(connectTimeout);
        }
        if (readTimeout >= 0) {
            connection.setReadTimeout(readTimeout);
        }

        connection.setDoInput(true);

        connection.setInstanceFollowRedirects("GET".equals(httpMethod));

        connection.setDoOutput("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod) || "DELETE".equals(httpMethod));

        connection.setRequestMethod(httpMethod);
    }
}
