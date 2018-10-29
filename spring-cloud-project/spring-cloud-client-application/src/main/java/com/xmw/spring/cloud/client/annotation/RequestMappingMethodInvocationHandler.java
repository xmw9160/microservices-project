package com.xmw.spring.cloud.client.annotation;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * @author xmw.
 * @date 2018/10/13 7:57.
 */
public class RequestMappingMethodInvocationHandler implements InvocationHandler {

    /**
     * 服务名称
     */
    private final String serviceName;

    private final BeanFactory beanFactory;

    /**
     * 方法参数名称获取器
     */
    private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    public RequestMappingMethodInvocationHandler(String serviceName, BeanFactory beanFactory) {
        this.serviceName = serviceName;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // 过滤@RequestMapping方法
        GetMapping getMapping = findAnnotation(method, GetMapping.class);
        if (getMapping != null) {
            // 得到URI
            String[] uri = getMapping.value();
            // http://${service}/${uri[0]}  此处假设只有一个参数
            StringBuilder urlBuilder = new StringBuilder("http://").append(serviceName).append(uri[0]);
            // 获取方法参数数量
            int count = method.getParameterCount();
            // 获取方法参数类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 获取方法注解
            Annotation[][] annotations = method.getParameterAnnotations();
            //Annotation[] annotations = method.getDeclaredAnnotations();
            // 方法参数是有顺序的
            //TODO fixme
            // String[] parameterNames = nameDiscoverer.getParameterNames(method);

            StringBuilder queryStringBuilder = new StringBuilder();

            for (int i = 0; i < count; i++) {
                // 参数i上面的所有注解
                Annotation[] paramAnnotations = annotations[i];

                // 方法参数类型
                Class<?> paramType = parameterTypes[0];
                // 方法参数名称
//                RequestParam requestParam = paramType.getAnnotation(RequestParam.class);
                RequestParam requestParam = (RequestParam) paramAnnotations[0];
                if (requestParam != null) {
//                    String paramName = parameterNames[0];
                    String paramName = "";
                    // HTTP 请求参数
                    String requestParamName = StringUtils.hasText(requestParam.value()) ? requestParam.value() : paramName;
                    String requestParamValue =
                            String.class.equals(paramType) ? (String) args[i] : String.valueOf(args[i]);
                    // uri?name=vale&n2=v2
                    queryStringBuilder.append("&")
                            .append(requestParamName).append("=").append(requestParamValue);
                }

                String queryString = queryStringBuilder.toString();
                if (StringUtils.hasText(queryString)) {
                    urlBuilder.append("?").append(queryString);
                }
                // http://${service}/${uri[0]}?${queryString}
                String url = urlBuilder.toString();

                // 获取Restemplate, Bean的名称为: loadBalanceRestTemplate
                RestTemplate restTemplate = beanFactory.getBean("loadBalanceRestTemplate", RestTemplate.class);
                return restTemplate.getForObject(url, method.getReturnType());
            }
        }
        return null;
    }
}
