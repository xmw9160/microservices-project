package com.xmw.spring.cloud.client.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * @author xmw.
 * @date 2018/10/11 22:21.
 */
public class RestClientsRegistrar
        implements ImportBeanDefinitionRegistrar,
        BeanFactoryAware, EnvironmentAware {

    private BeanFactory beanfactory;
    private Environment environment;

    public static void registerBeanByFactoryBean(String serviceName,
                                                 Object proxy, Class restClientClass, BeanDefinitionRegistry registry) {
        // 将@RestClient 接口代理实现注册为Bean(@Autowire)
        // BeanDefinitionRegistry registry
        String beanName = "RestClient." + serviceName;
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(RestClientClassFactoryBean.class);

        /**
         * <bean>
         *     <constructor-arg>${args}</constructor-arg>
         * </bean>
         */
        // 增加第一个构造器参数引用: proxy
        beanDefinitionBuilder.addConstructorArgValue(proxy);
        // 增加第二个构造器参数引用: restClientClass
        beanDefinitionBuilder.addConstructorArgValue(restClientClass);

        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        ClassLoader classLoader = metadata.getClass().getClassLoader();

        Map<String, Object> attributes =
                metadata.getAnnotationAttributes(EnableRestClient.class.getName(), false);
        // attributes -> {clients: SayingRestService}
        Class<?>[] clientClasses = (Class<?>[]) attributes.get("clients");
        // 接口类对象数组
        // 筛选所有接口
        Stream.of(clientClasses)
                // 过滤接口, 仅选择接口
                .filter(Class::isInterface)
                // 仅选择标注 @RestClient
                .filter(interfaceClass -> findAnnotation(interfaceClass, RestClient.class) != null)
                .forEach(restClientClass -> {
                    // 获取@RestClient 原信息
                    RestClient restClient = findAnnotation(restClientClass, RestClient.class);
                    // 获取调用的应用名称
//                    String serviceName = restClient.name();
                    // 获取调用的应用名称(处理占位符)
                    String serviceName = environment.resolvePlaceholders(restClient.name());

                    // RestTemplate -> serviceName/uri?param...

                    // @RestClient 接口编程JDK动态代理
                    Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{restClientClass},
                            new RequestMappingMethodInvocationHandler(serviceName, beanfactory));
                    // 将@RestClient 接口代理实现注册为Bean(@Autowire)
                    // BeanDefinitionRegistry registry
                    // 实现方式一
                    // registerBeanByFactoryBean(serviceName, proxy, restClientClass, registry);
                    // 实现方式二 SingletonBeanRegistry
                    String beanName = "RestClient." + serviceName;
                    if (registry instanceof SingletonBeanRegistry) {
                        SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry) registry;
                        singletonBeanRegistry.registerSingleton(beanName, proxy);
                    }
                });
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanfactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private static class RestClientClassFactoryBean implements FactoryBean {

        private final Object proxy;
        private final Class<?> restClientClass;

        private RestClientClassFactoryBean(Object proxy, Class<?> restClientClass) {
            this.proxy = proxy;
            this.restClientClass = restClientClass;
        }

        @Override
        public Object getObject() {
            return proxy;
        }

        @Override
        public Class<?> getObjectType() {
            return restClientClass;
        }
    }
}
