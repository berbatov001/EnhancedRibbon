package com.github.berbatov001.envolvedribbon.netfix.ribbon;

import com.netflix.loadbalancer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RibbonClient {

    private Logger LOGGER = LoggerFactory.getLogger(RibbonClient.class);

    private ILoadBalancer loadBalancer;

    private LoadBalancerContext loadBalancerContext;

    RibbonClient(ILoadBalancer loadBalancer, LoadBalancerContext loadBalancerContext){
        this.loadBalancer = loadBalancer;
        this.loadBalancerContext = loadBalancerContext;
    }

    public Server getServer(){
        if(this.loadBalancer == null){
            return null;
        }
        return loadBalancer.chooseServer("default");
    }

    public LoadBalancerContext getLoadBalancerContext(){
        if (this.loadBalancerContext == null) {
            return null;
        }
        return loadBalancerContext;
    }

    public ServerStats getServerStats(Server server){
        if (server == null) {
            return null;
        }
        LoadBalancerStats lbStats = ((AbstractLoadBalancer) loadBalancer).getLoadBalancerStats();
        return lbStats.getSingleServerStat(server);
    }

}
