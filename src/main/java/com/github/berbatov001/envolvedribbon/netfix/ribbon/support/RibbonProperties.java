package com.github.berbatov001.envolvedribbon.netfix.ribbon.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ribbon")
public class RibbonProperties {

    private Integer connectTimeout;
    private Integer readTimeout;
    private Boolean gZipPayload;
    private Integer maxAutoRetries;
    private Integer maxAutoRetriesNextServer;
    private String primeConnectionsURI;
    private String clientClassName;
    private String listOfServers;
    private Boolean isClientAuthRequired;
    private Boolean enableZoneAffinity;
    private Boolean followRedirects;
    private Integer port;
    private Boolean prioritizeVipAddressBasedServers;
    private Boolean okToRetryOnAllOperations;
    private String nFLoadBalancerClassName;
    private String primeConnectionsClassName;
    private Integer connIdleEvictTimeMilliSeconds;
    private Integer poolMinThreads;
    private Integer connectionCleanerRepeatInterval;
    private Integer maxTotalTimeToPrimeConnection;
    private String nlWSServerListClassName;
    private Integer maxRetriesPerServerPrimeConnection;
    private Integer maxConnectionsPerHost;
    private Integer maxTotalConnections;
    private Integer connectionMangerTimeout;
    private Integer poolKeepAliveTime;
    private Boolean userIPAddrForServer;
    private String vipAddressResolverClassName;
    private Boolean enableConnectionPool;
    private Integer poolMaxThreads;
    private Boolean enablePrimeConnections;
    private Float minPrimeConnectionsRatio;
    private String poolKeepAliveTimeUnits;
    private Boolean enableGZIPContentEncodingFilter;
    private String nFLoadBalancerPingClassName;
    private Boolean enableZoneExclusivity;
    private Boolean connectionPoolCleanerTaskEnabled;
    private String nFLoadBalancerRuleClassName;

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Boolean getgZipPayload() {
        return gZipPayload;
    }

    public void setgZipPayload(Boolean gZipPayload) {
        this.gZipPayload = gZipPayload;
    }

    public Integer getMaxAutoRetries() {
        return maxAutoRetries;
    }

    public void setMaxAutoRetries(Integer maxAutoRetries) {
        this.maxAutoRetries = maxAutoRetries;
    }

    public Integer getMaxAutoRetriesNextServer() {
        return maxAutoRetriesNextServer;
    }

    public void setMaxAutoRetriesNextServer(Integer maxAutoRetriesNextServer) {
        this.maxAutoRetriesNextServer = maxAutoRetriesNextServer;
    }

    public String getPrimeConnectionsURI() {
        return primeConnectionsURI;
    }

    public void setPrimeConnectionsURI(String primeConnectionsURI) {
        this.primeConnectionsURI = primeConnectionsURI;
    }

    public String getClientClassName() {
        return clientClassName;
    }

    public void setClientClassName(String clientClassName) {
        this.clientClassName = clientClassName;
    }

    public String getListOfServers() {
        return listOfServers;
    }

    public void setListOfServers(String listOfServers) {
        this.listOfServers = listOfServers;
    }

    public Boolean getClientAuthRequired() {
        return isClientAuthRequired;
    }

    public void setClientAuthRequired(Boolean clientAuthRequired) {
        isClientAuthRequired = clientAuthRequired;
    }

    public Boolean getEnableZoneAffinity() {
        return enableZoneAffinity;
    }

    public void setEnableZoneAffinity(Boolean enableZoneAffinity) {
        this.enableZoneAffinity = enableZoneAffinity;
    }

    public Boolean getFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(Boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getPrioritizeVipAddressBasedServers() {
        return prioritizeVipAddressBasedServers;
    }

    public void setPrioritizeVipAddressBasedServers(Boolean prioritizeVipAddressBasedServers) {
        this.prioritizeVipAddressBasedServers = prioritizeVipAddressBasedServers;
    }

    public Boolean getOkToRetryOnAllOperations() {
        return okToRetryOnAllOperations;
    }

    public void setOkToRetryOnAllOperations(Boolean okToRetryOnAllOperations) {
        this.okToRetryOnAllOperations = okToRetryOnAllOperations;
    }

    public String getnFLoadBalancerClassName() {
        return nFLoadBalancerClassName;
    }

    public void setnFLoadBalancerClassName(String nFLoadBalancerClassName) {
        this.nFLoadBalancerClassName = nFLoadBalancerClassName;
    }

    public String getPrimeConnectionsClassName() {
        return primeConnectionsClassName;
    }

    public void setPrimeConnectionsClassName(String primeConnectionsClassName) {
        this.primeConnectionsClassName = primeConnectionsClassName;
    }

    public Integer getConnIdleEvictTimeMilliSeconds() {
        return connIdleEvictTimeMilliSeconds;
    }

    public void setConnIdleEvictTimeMilliSeconds(Integer connIdleEvictTimeMilliSeconds) {
        this.connIdleEvictTimeMilliSeconds = connIdleEvictTimeMilliSeconds;
    }

    public Integer getPoolMinThreads() {
        return poolMinThreads;
    }

    public void setPoolMinThreads(Integer poolMinThreads) {
        this.poolMinThreads = poolMinThreads;
    }

    public Integer getConnectionCleanerRepeatInterval() {
        return connectionCleanerRepeatInterval;
    }

    public void setConnectionCleanerRepeatInterval(Integer connectionCleanerRepeatInterval) {
        this.connectionCleanerRepeatInterval = connectionCleanerRepeatInterval;
    }

    public Integer getMaxTotalTimeToPrimeConnection() {
        return maxTotalTimeToPrimeConnection;
    }

    public void setMaxTotalTimeToPrimeConnection(Integer maxTotalTimeToPrimeConnection) {
        this.maxTotalTimeToPrimeConnection = maxTotalTimeToPrimeConnection;
    }

    public String getNlWSServerListClassName() {
        return nlWSServerListClassName;
    }

    public void setNlWSServerListClassName(String nlWSServerListClassName) {
        this.nlWSServerListClassName = nlWSServerListClassName;
    }

    public Integer getMaxRetriesPerServerPrimeConnection() {
        return maxRetriesPerServerPrimeConnection;
    }

    public void setMaxRetriesPerServerPrimeConnection(Integer maxRetriesPerServerPrimeConnection) {
        this.maxRetriesPerServerPrimeConnection = maxRetriesPerServerPrimeConnection;
    }

    public Integer getMaxConnectionsPerHost() {
        return maxConnectionsPerHost;
    }

    public void setMaxConnectionsPerHost(Integer maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public Integer getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(Integer maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public Integer getConnectionMangerTimeout() {
        return connectionMangerTimeout;
    }

    public void setConnectionMangerTimeout(Integer connectionMangerTimeout) {
        this.connectionMangerTimeout = connectionMangerTimeout;
    }

    public Integer getPoolKeepAliveTime() {
        return poolKeepAliveTime;
    }

    public void setPoolKeepAliveTime(Integer poolKeepAliveTime) {
        this.poolKeepAliveTime = poolKeepAliveTime;
    }

    public Boolean getUserIPAddrForServer() {
        return userIPAddrForServer;
    }

    public void setUserIPAddrForServer(Boolean userIPAddrForServer) {
        this.userIPAddrForServer = userIPAddrForServer;
    }

    public String getVipAddressResolverClassName() {
        return vipAddressResolverClassName;
    }

    public void setVipAddressResolverClassName(String vipAddressResolverClassName) {
        this.vipAddressResolverClassName = vipAddressResolverClassName;
    }

    public Boolean getEnableConnectionPool() {
        return enableConnectionPool;
    }

    public void setEnableConnectionPool(Boolean enableConnectionPool) {
        this.enableConnectionPool = enableConnectionPool;
    }

    public Integer getPoolMaxThreads() {
        return poolMaxThreads;
    }

    public void setPoolMaxThreads(Integer poolMaxThreads) {
        this.poolMaxThreads = poolMaxThreads;
    }

    public Boolean getEnablePrimeConnections() {
        return enablePrimeConnections;
    }

    public void setEnablePrimeConnections(Boolean enablePrimeConnections) {
        this.enablePrimeConnections = enablePrimeConnections;
    }

    public Float getMinPrimeConnectionsRatio() {
        return minPrimeConnectionsRatio;
    }

    public void setMinPrimeConnectionsRatio(Float minPrimeConnectionsRatio) {
        this.minPrimeConnectionsRatio = minPrimeConnectionsRatio;
    }

    public String getPoolKeepAliveTimeUnits() {
        return poolKeepAliveTimeUnits;
    }

    public void setPoolKeepAliveTimeUnits(String poolKeepAliveTimeUnits) {
        this.poolKeepAliveTimeUnits = poolKeepAliveTimeUnits;
    }

    public Boolean getEnableGZIPContentEncodingFilter() {
        return enableGZIPContentEncodingFilter;
    }

    public void setEnableGZIPContentEncodingFilter(Boolean enableGZIPContentEncodingFilter) {
        this.enableGZIPContentEncodingFilter = enableGZIPContentEncodingFilter;
    }

    public String getnFLoadBalancerPingClassName() {
        return nFLoadBalancerPingClassName;
    }

    public void setnFLoadBalancerPingClassName(String nFLoadBalancerPingClassName) {
        this.nFLoadBalancerPingClassName = nFLoadBalancerPingClassName;
    }

    public Boolean getEnableZoneExclusivity() {
        return enableZoneExclusivity;
    }

    public void setEnableZoneExclusivity(Boolean enableZoneExclusivity) {
        this.enableZoneExclusivity = enableZoneExclusivity;
    }

    public Boolean getConnectionPoolCleanerTaskEnabled() {
        return connectionPoolCleanerTaskEnabled;
    }

    public void setConnectionPoolCleanerTaskEnabled(Boolean connectionPoolCleanerTaskEnabled) {
        this.connectionPoolCleanerTaskEnabled = connectionPoolCleanerTaskEnabled;
    }

    public String getnFLoadBalancerRuleClassName() {
        return nFLoadBalancerRuleClassName;
    }

    public void setnFLoadBalancerRuleClassName(String nFLoadBalancerRuleClassName) {
        this.nFLoadBalancerRuleClassName = nFLoadBalancerRuleClassName;
    }
}
