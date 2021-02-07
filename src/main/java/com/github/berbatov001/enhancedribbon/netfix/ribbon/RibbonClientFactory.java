package com.github.berbatov001.enhancedribbon.netfix.ribbon;

import com.github.berbatov001.enhancedribbon.nacos.NacosDiscoveryProperties;
import com.github.berbatov001.enhancedribbon.nacos.NacosDiscoveryPropertiesHolder;
import com.github.berbatov001.enhancedribbon.netfix.ribbon.rule.SdkZoneAvoidanceRule;
import com.github.berbatov001.enhancedribbon.netfix.ribbon.support.RibbonProperties;
import com.github.berbatov001.enhancedribbon.util.ApplicationContextHolder;
import com.netflix.client.ClientException;
import com.netflix.client.config.ClientConfigFactory;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RibbonClientFactory {
    private static final Map<String, RibbonClient> namedRCMap = new ConcurrentHashMap<>();
    private static final Map<String, IClientConfig> namedConfig = new ConcurrentHashMap<>();

    public static synchronized RibbonClient getRibbonClient(String serviceName) {
        RibbonClient ribbonClient = namedRCMap.get(serviceName);
        if (ribbonClient != null) {
            return ribbonClient;
        }
        try {
            ILoadBalancer lb = registerNamedLoadBalancer(serviceName);
            LoadBalancerContext context = new LoadBalancerContext(lb);
            ribbonClient = new RibbonClient(lb, context);
            namedRCMap.put(serviceName, ribbonClient);
        } catch (ClientException exception) {
            throw new RuntimeException("创建Load Balancer失败", exception);
        }
        return ribbonClient;
    }

    private static ILoadBalancer registerNamedLoadBalancer(String serviceName) throws ClientException {
        IClientConfig config = getNamedConfig(serviceName);
        SdkZoneAvoidanceRule rule = new SdkZoneAvoidanceRule();
        rule.setPredicate();
        rule.initWithNiwsConfig(config);
        IPing ping = new DummyPing();
        ServerListUpdater serverListUpdater = new PollingServerListUpdater(config);
        NacosDiscoveryProperties nacosDiscoveryProperties = NacosDiscoveryPropertiesHolder.getNacosDiscoveryProperties();
        NacosServerList serverList = new NacosServerList(nacosDiscoveryProperties);
        serverList.initWithNiwsConfig(config);
        ServerListFilter<NacosServer> serverListFilter = serverList.getFilterImpl(config);
        return new ZoneAwareLoadBalancer<>(config, rule, ping, serverList, serverListFilter, serverListUpdater);
    }

    public static IClientConfig getNamedConfig(String serviceName) {
        return namedConfig.computeIfAbsent(serviceName, ignore -> {
            RibbonProperties ribbonProperties = ApplicationContextHolder.getApplicationContext().getBean(RibbonProperties.class);
            IClientConfig config = ClientConfigFactory.DEFAULT.newConfig();
            //设置DefaultClientConfigImpl的clientName属性，否则导致NacosServerList无法获取Server列表。
            config.loadProperties(serviceName);
            if (ribbonProperties.getConnectTimeout() != null) {
                config.set(CommonClientConfigKey.ConnectTimeout, ribbonProperties.getConnectTimeout());
            }
            if (ribbonProperties.getReadTimeout() != null) {
                config.set(CommonClientConfigKey.ReadTimeout, ribbonProperties.getReadTimeout());
            }
            if (ribbonProperties.getgZipPayload() != null) {
                config.set(CommonClientConfigKey.GZipPayload, ribbonProperties.getgZipPayload());
            }
            if (ribbonProperties.getMaxAutoRetries() != null) {
                config.set(CommonClientConfigKey.MaxAutoRetries, ribbonProperties.getMaxAutoRetries());
            }
            if (ribbonProperties.getMaxAutoRetriesNextServer() != null) {
                config.set(CommonClientConfigKey.MaxAutoRetriesNextServer, ribbonProperties.getMaxAutoRetriesNextServer());
            }
            if (ribbonProperties.getPrimeConnectionsURI() != null) {
                config.set(CommonClientConfigKey.PrimeConnectionsURI, ribbonProperties.getPrimeConnectionsURI());
            }
            if (ribbonProperties.getClientClassName() != null) {
                config.set(CommonClientConfigKey.ClientClassName, ribbonProperties.getClientClassName());
            }
            if (ribbonProperties.getListOfServers() != null) {
                config.set(CommonClientConfigKey.ListOfServers, ribbonProperties.getListOfServers());
            }
            if (ribbonProperties.getClientAuthRequired() != null) {
                config.set(CommonClientConfigKey.IsClientAuthRequired, ribbonProperties.getClientAuthRequired());
            }
            if (ribbonProperties.getEnableZoneAffinity() != null) {
                config.set(CommonClientConfigKey.EnableZoneAffinity, ribbonProperties.getEnableZoneAffinity());
            }
            if (ribbonProperties.getFollowRedirects() != null) {
                config.set(CommonClientConfigKey.FollowRedirects, ribbonProperties.getFollowRedirects());
            }
            if (ribbonProperties.getPort() != null) {
                config.set(CommonClientConfigKey.Port, ribbonProperties.getPort());
            }
            if (ribbonProperties.getPrioritizeVipAddressBasedServers() != null) {
                config.set(CommonClientConfigKey.PrioritizeVipAddressBasedServers, ribbonProperties.getPrioritizeVipAddressBasedServers());
            }
            if (ribbonProperties.getOkToRetryOnAllOperations() != null) {
                config.set(CommonClientConfigKey.OkToRetryOnAllOperations, ribbonProperties.getOkToRetryOnAllOperations());
            }
            if (ribbonProperties.getnFLoadBalancerClassName() != null) {
                config.set(CommonClientConfigKey.NFLoadBalancerClassName, ribbonProperties.getnFLoadBalancerClassName());
            }
            if (ribbonProperties.getPrimeConnectionsClassName() != null) {
                config.set(CommonClientConfigKey.PrimeConnectionsClassName, ribbonProperties.getPrimeConnectionsClassName());
            }
            if (ribbonProperties.getConnIdleEvictTimeMilliSeconds() != null) {
                config.set(CommonClientConfigKey.ConnIdleEvictTimeMilliSeconds, ribbonProperties.getConnIdleEvictTimeMilliSeconds());
            }
            if (ribbonProperties.getPoolMinThreads() != null) {
                config.set(CommonClientConfigKey.PoolMinThreads, ribbonProperties.getPoolMinThreads());
            }
            if (ribbonProperties.getConnectionCleanerRepeatInterval() != null) {
                config.set(CommonClientConfigKey.ConnectionCleanerRepeatInterval, ribbonProperties.getConnectionCleanerRepeatInterval());
            }
            if (ribbonProperties.getMaxTotalTimeToPrimeConnection() != null) {
                config.set(CommonClientConfigKey.MaxTotalTimeToPrimeConnections, ribbonProperties.getMaxTotalTimeToPrimeConnection());
            }
            if (ribbonProperties.getNlWSServerListClassName() != null) {
                config.set(CommonClientConfigKey.NIWSServerListClassName, ribbonProperties.getNlWSServerListClassName());
            }
            if (ribbonProperties.getMaxRetriesPerServerPrimeConnection() != null) {
                config.set(CommonClientConfigKey.MaxRetriesPerServerPrimeConnection, ribbonProperties.getMaxRetriesPerServerPrimeConnection());
            }
            if (ribbonProperties.getMaxConnectionsPerHost() != null) {
                config.set(CommonClientConfigKey.MaxConnectionsPerHost, ribbonProperties.getMaxConnectionsPerHost());
            }
            if (ribbonProperties.getMaxTotalConnections() != null) {
                config.set(CommonClientConfigKey.MaxTotalConnections, ribbonProperties.getMaxTotalConnections());
            }
            if (ribbonProperties.getConnectionMangerTimeout() != null) {
                config.set(CommonClientConfigKey.ConnectionManagerTimeout, ribbonProperties.getConnectionMangerTimeout());
            }
            if (ribbonProperties.getPoolKeepAliveTime() != null) {
                config.set(CommonClientConfigKey.PoolKeepAliveTime, ribbonProperties.getPoolKeepAliveTime());
            }
            if (ribbonProperties.getUserIPAddrForServer() != null) {
                config.set(CommonClientConfigKey.UseIPAddrForServer, ribbonProperties.getUserIPAddrForServer());
            }
            if (ribbonProperties.getVipAddressResolverClassName() != null) {
                config.set(CommonClientConfigKey.VipAddressResolverClassName, ribbonProperties.getVipAddressResolverClassName());
            }
            if (ribbonProperties.getEnableConnectionPool() != null) {
                config.set(CommonClientConfigKey.EnableConnectionPool, ribbonProperties.getEnableConnectionPool());
            }
            if (ribbonProperties.getPoolMaxThreads() != null) {
                config.set(CommonClientConfigKey.PoolMaxThreads, ribbonProperties.getPoolMaxThreads());
            }
            if (ribbonProperties.getEnablePrimeConnections() != null) {
                config.set(CommonClientConfigKey.EnablePrimeConnections, ribbonProperties.getEnablePrimeConnections());
            }
            if (ribbonProperties.getMinPrimeConnectionsRatio() != null) {
                config.set(CommonClientConfigKey.MinPrimeConnectionsRatio, ribbonProperties.getMinPrimeConnectionsRatio());
            }
            if (ribbonProperties.getPoolKeepAliveTimeUnits() != null) {
                config.set(CommonClientConfigKey.PoolKeepAliveTimeUnits, ribbonProperties.getPoolKeepAliveTimeUnits());
            }
            if (ribbonProperties.getEnableGZIPContentEncodingFilter() != null) {
                config.set(CommonClientConfigKey.EnableGZIPContentEncodingFilter, ribbonProperties.getEnableGZIPContentEncodingFilter());
            }
            if (ribbonProperties.getnFLoadBalancerPingClassName() != null) {
                config.set(CommonClientConfigKey.NFLoadBalancerPingClassName, ribbonProperties.getnFLoadBalancerPingClassName());
            }
            if (ribbonProperties.getEnableZoneExclusivity() != null) {
                config.set(CommonClientConfigKey.EnableZoneExclusivity, ribbonProperties.getEnableZoneExclusivity());
            }
            if (ribbonProperties.getConnectionPoolCleanerTaskEnabled() != null) {
                config.set(CommonClientConfigKey.ConnectionPoolCleanerTaskEnabled, ribbonProperties.getConnectionPoolCleanerTaskEnabled());
            }
            if (ribbonProperties.getnFLoadBalancerRuleClassName() != null) {
                config.set(CommonClientConfigKey.NFLoadBalancerRuleClassName, ribbonProperties.getnFLoadBalancerRuleClassName());
            }
            return config;
        });
    }

}
