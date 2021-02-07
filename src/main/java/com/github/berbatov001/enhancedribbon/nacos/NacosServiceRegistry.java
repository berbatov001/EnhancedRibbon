package com.github.berbatov001.enhancedribbon.nacos;

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NacosServiceRegistry {
    private static final Logger log = LoggerFactory.getLogger(NacosServiceRegistry.class);

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    private final NamingService namingService;

    public NacosServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        namingService = nacosDiscoveryProperties.getNamingService();
    }

    void registry() {
        String serviceId = nacosDiscoveryProperties.getService();
        if (StringUtils.isEmpty(serviceId)) {
            log.warn("Nacos Client无法获取ServerId。");
            return;
        }
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            if (StringUtils.hasLength(hostName) && hostName.length() >= 3) {
                String idcName = hostName.substring(0, 3);
                nacosDiscoveryProperties.getMetadata().put("IDC", idcName);
                nacosDiscoveryProperties.setIdcCode(idcName);
            } else {
                log.warn("主机名{}不符合规范", hostName);
            }
        } catch (UnknownHostException exception) {
            log.warn("获取主机名发生异常。具体原因：" + exception.getMessage(), exception);
        }

        Instance instance = new Instance();
        instance.setIp(nacosDiscoveryProperties.getIp());
        instance.setPort(nacosDiscoveryProperties.getPort());
        instance.setWeight(nacosDiscoveryProperties.getWeight());
        instance.setClusterName(nacosDiscoveryProperties.getClusterName());
        instance.setMetadata(nacosDiscoveryProperties.getMetadata());

        try {
            namingService.registerInstance(serviceId, instance);
            log.info("注册完成！实例信息：{} {}:{}", serviceId, instance.getIp(), instance.getPort());
        } catch (Exception exception) {
            log.error("{}注册失败！", serviceId, exception);
        }
    }
}
