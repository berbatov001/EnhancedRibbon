# EnhancedRibbon
EnhancedRibbon是一款轻量级的微服务SDK，它将原生的Netfix Ribbon和Nacos Client做了简单的封装，实现了服务的注册与发现、本地负载均衡、服务间访问等功能。相比于SpringCloud，它使用更加简单方便。同时更加轻量级，不会引入大量SpringCloud依赖。

## 1. 整体介绍
EnhancedRibbon继承了原生的Netflix Ribbon实现本地负载均衡，同时使用Nacos-Client想Nacos注册中心注册、发送心跳，拉去服务列表。最后通过RestTemlate向远端的实例发送Http请求。如下图：

## 2. 如何使用
### 2.1在你的Springboot工程中引入依赖
<dependency>  
    <groupId>com.github.berbatov001</groupId>  
    <artifactId>enhanced-ribbon</artifactId>  
    <version>1.0-SNAPSHOT</version>  
</dependency> 


### 2.2在application.properties文件中添加Nacos配置中心
nacos.discovery.serverAddr=(具体的Nacos集群地址)

### 2.3使用RemoteClient完成服务之间的调用
@Autowired  
private RemoteClient remoteClient;
