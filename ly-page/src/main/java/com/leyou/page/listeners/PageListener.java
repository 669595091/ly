package com.leyou.page.listeners;

import com.leyou.page.service.FileService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageListener {

    @Autowired
    private FileService fileService;
    //创建文件
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.item.create.queues", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",//交换机名字
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))//routerkey
    public void listenCreatePage(Long id) throws Exception {
        //创建静态文件
        fileService.syncCreateHtml(id);

    }
    //删除文件
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.item.delete.queues", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",//交换机名字
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))//routerkey
    public void listenDeletePage(Long id) throws Exception {
        //创建静态文件
        fileService.syncDeleteHtml(id);

    }
}
