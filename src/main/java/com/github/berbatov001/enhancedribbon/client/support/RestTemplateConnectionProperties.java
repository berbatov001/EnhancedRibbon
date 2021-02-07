package com.github.berbatov001.enhancedribbon.client.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties("rest-template")
public class RestTemplateConnectionProperties {

    private Map<String, ConnectionConfiguration> connections = new LinkedHashMap<>();

    private int defaultConnectTimeout = -1;

    private int defaultReadTimeout = -1;

    public Map<String, ConnectionConfiguration> getConnections() {
        return connections;
    }

    public void setConnections(Map<String, ConnectionConfiguration> connections) {
        this.connections = connections;
    }

    public int getDefaultConnectTimeout() {
        return defaultConnectTimeout;
    }

    public void setDefaultConnectTimeout(int defaultConnectTimeout) {
        this.defaultConnectTimeout = defaultConnectTimeout;
    }

    public int getDefaultReadTimeout() {
        return defaultReadTimeout;
    }

    public void setDefaultReadTimeout(int defaultReadTimeout) {
        this.defaultReadTimeout = defaultReadTimeout;
    }

    public static class ConnectionConfiguration {
        private int connectionTimeout = -1;
        private int readTimeout = -1;
        private String url;

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
