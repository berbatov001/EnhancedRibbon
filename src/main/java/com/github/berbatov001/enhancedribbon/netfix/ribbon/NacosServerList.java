package com.github.berbatov001.enhancedribbon.netfix.ribbon;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.github.berbatov001.enhancedribbon.nacos.NacosDiscoveryProperties;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class NacosServerList extends AbstractServerList<NacosServer> {

    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private String serviceId;

    NacosServerList(NacosDiscoveryProperties nacosDiscoveryProperties) {
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
        if (instances == null) {
            return result;
        }
        String minVersion = "0.0";
        String maxVersion = "0.0";
        boolean hasCoexistingVersions = false;
        for (int i = 0; i < instances.size(); i++) {
            Instance instance = instances.get(i);
            String version = StringUtils.hasLength(instance.getMetadata().get("version")) ? instance.getMetadata().get("version") : "0.0";
            if (i == 0) {
                minVersion = version;
                maxVersion = version;
            } else {
                if (compareVersion(version, maxVersion) > 0) {
                    maxVersion = version;
                } else {
                    minVersion = version;
                }
            }
            if (nacosDiscoveryProperties.getIdcCode().equals(instance.getMetadata().get("IDC"))) {
                resultInSameIdc.add(new NacosServer(instance));
            }
            result.add(new NacosServer(instance));
        }
        if (!minVersion.equals(maxVersion)) {
            hasCoexistingVersions = true;
        }
        List<NacosServer> nacosServers = resultInSameIdc.size() > 0 ? resultInSameIdc : result;
        for (NacosServer server : nacosServers) {
            server.getMetadata().put("hasCoexistingVersion", Boolean.toString(hasCoexistingVersions));
            server.getMetadata().put("maxVersion", maxVersion);
        }
        return nacosServers;
    }

    private int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        //获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        //循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            //如果位数不一样，比较多余位数。
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
}
