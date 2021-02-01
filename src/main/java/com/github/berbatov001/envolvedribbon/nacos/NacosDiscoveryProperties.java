package com.github.berbatov001.envolvedribbon.nacos;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.client.naming.utils.UtilAndComs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.alibaba.nacos.api.PropertyKeyConst.*;

@ConfigurationProperties("nacos.discovery")
public class NacosDiscoveryProperties {
    private static final Logger log = LoggerFactory.getLogger(NacosDiscoveryProperties.class);

    private NamingService namingService;

    private String serverAddr;

    private String endpoint;

    private String namespace;

    private long watchDelay = 30000;

    private String logName;

    @Value("${nacos.discovery.service:${spring.application.name:}}")
    private String service;

    private float weight = 1;

    private String clusterName = "DEFAULT";

    private String namingLoadCacheAtStart = "false";

    private Map<String, String> metadata = new HashMap<>();

    private boolean registerEnabled = true;

    private String ip;

    private String networkInterface = "";

    private int port = -1;

    private String accessKey;

    private String secretKey;

    private String idcCode;

    public NamingService getNamingService() {
        return namingService;
    }

    public void setNamingService(NamingService namingService) {
        this.namingService = namingService;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public long getWatchDelay() {
        return watchDelay;
    }

    public void setWatchDelay(long watchDelay) {
        this.watchDelay = watchDelay;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNamingLoadCacheAtStart() {
        return namingLoadCacheAtStart;
    }

    public void setNamingLoadCacheAtStart(String namingLoadCacheAtStart) {
        this.namingLoadCacheAtStart = namingLoadCacheAtStart;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public boolean isRegisterEnabled() {
        return registerEnabled;
    }

    public void setRegisterEnabled(boolean registerEnabled) {
        this.registerEnabled = registerEnabled;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(String networkInterface) {
        this.networkInterface = networkInterface;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getIdcCode() {
        return idcCode;
    }

    public void setIdcCode(String idcCode) {
        this.idcCode = idcCode;
    }

    @PostConstruct
    public void init() throws SocketException {
        serverAddr = Objects.toString(serverAddr, "");
        endpoint = Objects.toString(endpoint, "");
        namespace = Objects.toString(namespace, "");
        logName = Objects.toString(logName, "");
        accessKey = Objects.toString(accessKey, "");
        secretKey = Objects.toString(secretKey, "");
    }

    public NamingService namingServiceInstance() {
        if (null != namingService) {
            return namingService;
        }
        Properties properties = new Properties();
        properties.put(SERVER_ADDR, serverAddr);
        properties.put(NAMESPACE, namespace);
        properties.put(UtilAndComs.NACOS_NAMING_LOG_NAME, logName);

        if (endpoint.contains(":")) {
            int index = endpoint.indexOf(":");
            properties.put(ENDPOINT, endpoint.substring(0, index));
            properties.put(ENDPOINT_PORT, endpoint.substring(index + 1));
        } else {
            properties.put(ENDPOINT, endpoint);
        }
        properties.put(ACCESS_KEY, accessKey);
        properties.put(SECRET_KEY, secretKey);
        properties.put(CLUSTER_NAME, clusterName);
        properties.put(NAMING_LOAD_CACHE_AT_START, namingLoadCacheAtStart);

        try {
            namingService = NamingFactory.createNamingService(properties);
        } catch (Exception e) {
            log.error("create naming service error! properties={},e={},", this, e);
            return null;
        }
        return namingService;
    }
}
