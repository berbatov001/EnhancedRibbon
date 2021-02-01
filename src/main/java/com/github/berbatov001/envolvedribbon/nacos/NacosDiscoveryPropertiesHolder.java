package com.github.berbatov001.envolvedribbon.nacos;

public class NacosDiscoveryPropertiesHolder {
    private static NacosDiscoveryProperties STORES;

    static void setNacosDiscoveryProperties(NacosDiscoveryProperties nacosDiscoveryProperties) {
        STORES = nacosDiscoveryProperties;
    }

    public static NacosDiscoveryProperties getNacosDiscoveryProperties() {
        return STORES;
    }
}
