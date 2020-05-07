package com.leyou.listeners;

import com.leyou.service.IndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private IndexService indexService;

    /**
     * 处理insert和update的消息
     *
     * @param id
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.item.create.queues", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",//交换机名字
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert", "item.update"}))//routerkey
    public void listenCreate(Long id) throws Exception {
        if (id == null) {
            return;
        }
        // 创建或更新索引
        this.indexService.createIndex(id);
    }
    //删除索引
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.item.delete.queues", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange",//交换机名字
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.delete"}))//routerkey
    public void listenDelete(Long id) throws Exception {
        if (id == null) {
            return;
        }
        // 删除索引
        this.indexService.deleteIndex(id);
    }
}
