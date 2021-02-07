package com.github.berbatov001.enhancedribbon.netfix.ribbon;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.loadbalancer.Server;

import java.util.Map;

public class NacosServer extends Server {
    private final MetaInfo metaInfo;

    private final Instance instance;

    private final Map<String, String> metadata;

    NacosServer(final Instance instance) {
        super(instance.getIp(), instance.getPort());
        this.instance = instance;
        this.metaInfo = new MetaInfo() {
            @Override
            public String getAppName() {
                return instance.getServiceName();
            }

            @Override
            public String getServerGroup() {
                return null;
            }

            @Override
            public String getServiceIdForDiscovery() {
                return null;
            }

            @Override
            public String getInstanceId() {
                return instance.getInstanceId();
            }
        };
        this.metadata = instance.getMetadata();
    }

    @Override
    public MetaInfo getMetaInfo() {
        return super.getMetaInfo();
    }

    public Instance getInstance(){
        return instance;
    }

    public Map<String, String> getMetadata(){
        return metadata;
    }
}
