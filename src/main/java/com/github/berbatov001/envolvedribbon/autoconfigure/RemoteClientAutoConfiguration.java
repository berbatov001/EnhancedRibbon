package com.github.berbatov001.envolvedribbon.autoconfigure;

import com.github.berbatov001.envolvedribbon.client.RemoteClient;
import com.github.berbatov001.envolvedribbon.client.loadbalancer.LoadBalancerInterceptor;
import com.github.berbatov001.envolvedribbon.client.support.CustomizeClientHttpRequestFactory;
import com.github.berbatov001.envolvedribbon.client.support.RestTemplateConnectionProperties;
import com.github.berbatov001.envolvedribbon.nacos.NacosDiscoveryProperties;
import com.github.berbatov001.envolvedribbon.nacos.NacosRegistryHelper;
import com.github.berbatov001.envolvedribbon.nacos.NacosServiceRegistry;
import com.github.berbatov001.envolvedribbon.netfix.ribbon.support.RibbonProperties;
import com.github.berbatov001.envolvedribbon.util.ApplicationContextHolder;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableConfigurationProperties({NacosDiscoveryProperties.class, RibbonProperties.class, RestTemplateConnectionProperties.class})
public class RemoteClientAutoConfiguration {

    @Autowired
    private RestTemplateConnectionProperties restTemplateConnectionProperties;

    public ApplicationContextHolder applicationContextHolder(){
        return new ApplicationContextHolder();
    }

    @Qualifier("ribbonRestTemplate")
    @Autowired(required = false)
    private List<RestTemplate> restTemplates = Collections.emptyList();

    @Bean
    @ConditionalOnMissingClass("com.alibaba.cloud.nacos.registry.NacosServiceRegistry")
    public NacosDiscoveryProperties nacosDiscoveryProperties(){
        return new NacosDiscoveryProperties();
    }

    @Bean
    @ConditionalOnMissingClass("com.alibaba.cloud.nacos.registry.NacosServiceRegistry")
    public NacosServiceRegistry nacosServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties){
        return new NacosServiceRegistry(nacosDiscoveryProperties);
    }

    @Bean
    @ConditionalOnMissingClass("com.alibaba.cloud.nacos.registry.NacosServiceRegistry")
    public NacosRegistryHelper nacosOnRefreshListener(){
        return new NacosRegistryHelper();
    }

    @Bean
    public RestTemplate ribbonRestTemplate(){
        //使用自己定义的CustomizeClientHttpRequestFactory
        RestTemplate restTemplate = new RestTemplate(customizeClientHttpRequestFactory());
        //修改RestTemplate默认的StringHttpMessageConverter
        final String stringHttpMessageConverterClassName = "org.springframework.http.converter.StringHttpMessageConverter";
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        if(CollectionUtils.isEmpty(messageConverters)){
            for(int index = 0; index < messageConverters.size(); index ++){
                HttpMessageConverter<?> httpMessageConverter = messageConverters.get(index);
                if(stringHttpMessageConverterClassName.equals(httpMessageConverter.getClass().getName())){
                    //设置新的StringHttpMessageConverter的默认字符集位UTF-8，解决下游中文乱码问题。
                    messageConverters.set(index, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                }
            }
        }
        return restTemplate;
    }

    @Bean
    public CustomizeClientHttpRequestFactory customizeClientHttpRequestFactory(){
        CustomizeClientHttpRequestFactory customizeClientHttpRequestFactory = new CustomizeClientHttpRequestFactory();
        customizeClientHttpRequestFactory.enhance(restTemplateConnectionProperties);
        return customizeClientHttpRequestFactory;
    }

    @Bean
    RemoteClient remoteClient(){
        return new RemoteClient(ribbonRestTemplate());
    }

    @Bean
    public SmartInitializingSingleton ribbonRestTemplateInitializer(){
        return () -> {
            LoadBalancerInterceptor loadBalancerInterceptor = new LoadBalancerInterceptor();
            this.restTemplates.forEach(restTemplate -> {
                List<ClientHttpRequestInterceptor> list = new ArrayList<>(restTemplate.getInterceptors());
                list.add(loadBalancerInterceptor);
                restTemplate.setInterceptors(list);
            });
        };
    }
}
