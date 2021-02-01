package com.github.berbatov001.envolvedribbon.nacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

public class NacosRegistryHelper implements ApplicationListener<WebServerInitializedEvent> {

    @Autowired
    private NacosServiceRegistry nacosServiceRegistry;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        NacosDiscoveryPropertiesHolder.setNacosDiscoveryProperties(nacosDiscoveryProperties);
        int port = webServerInitializedEvent.getWebServer().getPort();
        nacosDiscoveryProperties.setPort(port);
        nacosServiceRegistry.registry();
    }
}
