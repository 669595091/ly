package com.leyou.page.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;

    public Map<String,Object> loadData(Long spuId) {
        HashMap<String, Object> map = new HashMap<>();
        Spu spu = goodsClient.querySpuById(spuId);



        //查询spu添加到map
        map.put("spu",spu);
        //查询spuDetail添加到map
        map.put("spuDetail",goodsClient.querySpuDetailBySpuId(spuId));
        //查询sku添加到map
        map.put("sku",goodsClient.querySkuBySpuId(spuId));
        //查询规格参数
        List<SpecParam> specParams = this.specClient.querySpecParam(null, spu.getCid3(), null, null);

        //存放对照表
        Map<Long,Object> specMap = new HashMap<>();
        for (SpecParam specParam:specParams){
            specMap.put(specParam.getId(),specParam.getName());
        }
        map.put("specParams",specMap);
        //存放规格组
        List<SpecGroup> specGroups = specClient.querySpecGroups(spu.getCid3());
        map.put("specGroups",specGroups);


        return map;
    }
}
