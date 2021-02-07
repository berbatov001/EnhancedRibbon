# EnhancedRibbon
EnhancedRibbon是一款轻量级的微服务SDK，它将原生的Netfix Ribbon和Nacos Client做了简单的封装，实现了服务的注册与发现、本地负载均衡、服务间访问等功能。相比于SpringCloud，它使用更加简单方便。同时更加轻量级，不会引入大量SpringCloud依赖。  

如果你想使用Nacos作为注册中心快速搭建一个微服务体系，并使用HTTP协议作为微服务之间访问方式的话，EnhancedRibbon是一个很好的选择。

## 1. 整体介绍
EnhancedRibbon继承了原生的Netflix Ribbon实现本地负载均衡，同时使用Nacos-Client想Nacos注册中心注册、发送心跳，拉去服务列表。最后通过RestTemlate向远端的实例发送Http请求。如下图：

## 2. 如何使用
### 2.1在你的Springboot工程中引入依赖
```xml
<dependency>  
    <groupId>com.github.berbatov001</groupId>  
    <artifactId>enhanced-ribbon</artifactId>  
    <version>1.0-SNAPSHOT</version>  
</dependency>
```

### 2.2在application.properties文件中添加Nacos配置中心
nacos.discovery.serverAddr=(具体的Nacos集群地址)

### 2.3使用RemoteClient完成服务之间的调用
```
@Autowired  
private RemoteClient remoteClient;
```

RemoteClient提供了多个call方法，用于不同的场景
```
/**
 * 没有参数，直接访问。
 * @param serviceName 目标服务名
 * @param path 目标接口名
 * @param method HTTP方法名，GET POST PUT DELETE等
 * @param responseType 返回值类型
 * @param <T>
 * @return 通过该方法参数responseType的反省指定的是什么类型，这里就返回什么类型。
 */
public <T> T call(String serviceName, String path, HttpMethod method, Class<T> responseType) 
```
```
/**
 * 用于访问请求体是String类型的接口
 *
 * @param serviceName 目标服务名
 * @param path  目标接口名
 * @param method  HTTP方法名，GET POST PUT DELETE等
 * @param responseType  返回值类型
 * @param body  请求体
 * @param headers  请求header
 * @param <T>  指定返回值类型
 * @return  通过该方法参数responseType的反省指定的是什么类型，这里就返回什么类型。
 */
public <T> T call(String serviceName, String path, HttpMethod method, Class<T> responseType, String body, HttpHeaders headers)

例子1：发送Json
 String json = "{\"name\" : “berbatov001”}";
 HttpHeaders headers = new HttpHeaders();
 headers.add("content-type","application/json");
 String result = remoteClient.call("/serviceA", "/test", HttpMethod.POST, String.class, json, headers);
```
```
/**
 * 最通用的方法。可以自定义请求体，自定义请求头。
 *
 * @param serviceName 目标服务名
 * @param path 目标接口
 * @param method HTTP方法名，GET POST PUT DELETE等
 * @param headers 自定义请求头
 * @param body  自定义请求头
 * @param returnType  返回值类型
 * @param <T>  定义请求体类型
 * @param <R>  定义返回值类型
 * @return  返回一个ResponseEntity
 */
public <T, R> ResponseEntity<R> callForResponseEntity(String serviceName, String path, HttpMethod method, HttpHeaders headers, T body, Class<R> returnType)

例子2：访问一个接口，以流的方式获取返回值。
InputStream body = new ServletServerHttpRequest(request).getBody();
byte[] bytes = StreamUtils.copyToByteArray(body);
ResponseEntity<Resource> responseEntity = remoteClient.callForResponseEntity(serviceName, path, method, httpHeaders, bytes, Resource.class);
InputStream respone = remoteResponse.getEntity().getContent();
```

