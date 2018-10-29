package com.xmw.spring.cloud.client.loadbalance;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xmw.
 * @date 2018/10/9 6:17.
 */
public class LoadBalanceRequestInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private DiscoveryClient discoveryClient;
    // Map key service name, value URLs
    private volatile Map<String, Set<String>> targetUrlsCache = new HashMap<>();

    @Scheduled(fixedRate = 10 * 1000) //10秒钟更新一次缓存
    public void updateInvocationTargetUrlsCache() {
        // 获取当前应用的机器列表
        // http://${ip}:${port}
        Map<String, Set<String>> oldTargetUrlsCache = this.targetUrlsCache;
        Map<String, Set<String>> newTargetUrlsCache = new HashMap<>();
        discoveryClient.getServices().forEach(serverName -> {
            List<ServiceInstance> instances = discoveryClient.getInstances(serverName);
            Set<String> newTargetUrls = instances.stream()
                    .map(s -> (s.isSecure() ? "https://" : "http://") + s.getHost() + ":" + s.getPort())
                    .collect(Collectors.toSet());
            newTargetUrlsCache.put(serverName, newTargetUrls);
        });

        // swap
        this.targetUrlsCache = newTargetUrlsCache;
        oldTargetUrlsCache.clear(); // help gc
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //URI : /${app-name}/uri
        // "/" + serviceName + "/say
        URI requestURI = request.getURI();

        String path = requestURI.getPath();
        String[] parts = StringUtils.split(path.substring(1), "/");
        String appName = parts[0];
        String uri = parts[1];

        // 服务器列表快照, 避免方法多次获取的结果不一致
        List<String> targetUrls = new ArrayList<>(targetUrlsCache.get(appName));
        int size = targetUrls.size();
        // size=3, index=0-2
        int index = new Random().nextInt(size);
        // 选择其中一台服务器
        String targetUrL = targetUrls.get(index);
        // 最终服务器地址
        String actualURL = targetUrL + "/" + uri + "?" + requestURI.getQuery();
        System.out.println("本次请求的uri: " + actualURL);
        // 执行请求
//        List<HttpMessageConverter<?>> messageConverters = Arrays.asList(
//                new ByteArrayHttpMessageConverter(),
//                new StringHttpMessageConverter());
//        RestTemplate restTemplate = new RestTemplate(messageConverters);
//        // 响应内容
//        ResponseEntity<InputStream> entity = restTemplate.getForEntity(actualURL, InputStream.class);
//        // 响应头
//        HttpHeaders headers = entity.getHeaders();
//        // 响应主体
//        InputStream responseBody = entity.getBody();
//        return new SimpleClientHttpResponse(headers, responseBody);

        URL url = new URL(actualURL);
        URLConnection urlConnection = url.openConnection();
        InputStream responseBody = urlConnection.getInputStream();
        return new SimpleClientHttpResponse(new HttpHeaders(), responseBody);
    }

    private static class SimpleClientHttpResponse implements ClientHttpResponse {

        private HttpHeaders httpHeaders;
        private InputStream body;

        public SimpleClientHttpResponse(HttpHeaders httpHeaders, InputStream body) {
            this.httpHeaders = httpHeaders;
            this.body = body;
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.OK;
        }

        @Override
        public int getRawStatusCode() {
            return 0;
        }

        @Override
        public String getStatusText() {
            return "ok";
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getBody() {
            return this.body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.httpHeaders;
        }
    }
}
