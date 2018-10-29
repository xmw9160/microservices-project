package com.xmw.service;

import com.xmw.annotation.TransactionalService;

/**
 * @author xmw.
 * @date 2018/9/27 22:40.
 */
@TransactionalService(value = "echoService-2018", txName = "myTxName")  // @Service + @Transactional
// 定义它的名称
public class EchoService {

    public void echo(String message) {
        System.out.println(message);
    }
}
