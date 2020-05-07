package com.bigdata;
import com.leyou.client.GoodsClient;
import com.leyou.common.po.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.pojo.*;
import com.leyou.LySearchService;
import com.leyou.repository.GoodsRepository;
import com.leyou.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class IndexTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private IndexService indexService;

    @Test
    public void init(){
        //建库
        elasticsearchTemplate.createIndex(Goods.class);
        //
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void loadData(){

        int page=1;
        while (true){
            //使用FeignClient 调用商品微服务
            PageResult<SpuBo> pageResult = goodsClient.querySpuByPage(null, null, page, 50);
            //为空，
            if(pageResult==null){
                break;
            }
            page++;
             //获取商品list
            List<SpuBo> items = pageResult.getItems();

            List<Goods> list=new ArrayList<Goods>();

            for(SpuBo spuBo:items){
                //spubo-goods
               Goods goods= indexService.buildGoods(spuBo);
               list.add(goods);

            }
            //保存到索引库
            goodsRepository.saveAll(list);


        }


    }


}
