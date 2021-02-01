package com.github.berbatov001.envolvedribbon.netfix.ribbon.predicate;

import com.github.berbatov001.envolvedribbon.netfix.ribbon.NacosServer;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SdkGrayEnabledPredicate extends AbstractServerPredicate {
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkGrayEnabledPredicate.class);

    public SdkGrayEnabledPredicate(IRule rule) {
        super(rule);
    }

    @Override
    public boolean apply(PredicateKey predicateKey) {
        return sdkGrayApply(predicateKey.getServer());
    }

    /**
     * 如果灰度开启、版本一致，则返回true。
     *
     * @param server
     * @return
     */
    private boolean sdkGrayApply(Server server) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return true;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if ("true".equals(request.getHeader("grepEnable"))) {
            NacosServer nacosServer = (NacosServer) server;
            String serviceName = nacosServer.getInstance().getServiceName();
            Map<String, String> metadata = nacosServer.getMetadata();
            //header中的服务版本
            String serName = serviceName.substring(serviceName.lastIndexOf("@") + 1);
            String headerVersion = request.getHeader(serName);
            //server中的服务版本
            String serVersion = metadata.get("version");
            if (StringUtils.hasLength(headerVersion) && StringUtils.hasLength(serVersion)) {
                return headerVersion.equals(serVersion);
            }
            return false;
        }
        return true;
    }
}
