package com.xmw;

import com.xmw.service.EchoService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * @author xmw.
 * @date 2018/9/27 22:42.
 */
@ComponentScan(basePackages = "com.xmw.service")
@EnableTransactionManagement
public class SpringApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册bean
        context.register(EchoService.class);

        context.refresh(); // 启动容器

        context.getBeansOfType(EchoService.class)
                .forEach((beanName, bean) -> {
                    System.err.println("Bean name: " + beanName + ", Bean: " + bean);
                    bean.echo("hahaha.....");
                });

        context.close();   // 关闭容器
    }

    @Component(value = "myTxName")
    public static class MyPlattformTransactionManager implements PlatformTransactionManager {
        @Override
        public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
            return new DefaultTransactionStatus(
                    null, true, true,
                    definition.isReadOnly(), true, null
            );
        }

        @Override
        public void commit(TransactionStatus transactionStatus) throws TransactionException {
            System.out.println("commit...");
        }

        @Override
        public void rollback(TransactionStatus transactionStatus) throws TransactionException {
            System.out.println("rollback....");
        }
    }

}
