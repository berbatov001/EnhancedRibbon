package com.github.berbatov001.enhancedribbon.netfix.ribbon;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.berbatov001.enhancedribbon.nacos.NacosDiscoveryProperties;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;

import java.util.ArrayList;
import java.util.List;

public class NacosServerList extends AbstractServerList<NacosServer> {

    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private String serviceId;

    NacosServerList(NacosDiscoveryProperties nacosDiscoveryProperties){
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
    }

    @Override
    public List<NacosServer> getInitialListOfServers() {
        return null;
    }

    @Override
    public List<NacosServer> getUpdatedListOfServers() {
        return null;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

        this.serviceId = clientConfig.getClientName();
    }

    private List<NacosServer> getServers() {
        try {
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            List<Instance> instances = namingService.selectInstances(serviceId, true);
            return instancesToServerList(instances);
        } catch (Exception e) {
            throw new IllegalStateException("无法从注册中心获取到实例列表，serviceId=" + serviceId, e);
        }
    }

    private List<NacosServer> instancesToServerList(List<Instance> instances) {
        List<NacosServer> result = new ArrayList<>();
        List<NacosServer> resultInSameIdc = new ArrayList<>();
        if(instances == null){
            return result;
        }
        //优先选取同机房的实例
        for (Instance instance : instances) {
            if (nacosDiscoveryProperties.getIdcCode().equals(instance.getMetadata().get("IDC"))) {
                resultInSameIdc.add(new NacosServer(instance));
            }
            result.add(new NacosServer(instance));
        }
        return resultInSameIdc.size() > 0 ? resultInSameIdc : result;
    }
}
