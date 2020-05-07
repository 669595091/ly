package com.leyou.client;

import com.leyou.common.po.PageResult;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("item-service")
public interface GoodsClient  {

    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(@RequestParam(value = "key",required = false) String key,
                                            @RequestParam(value = "saleable",required = false) Boolean saleable,
                                            @RequestParam(value = "page",defaultValue = "1") Integer page,
                                            @RequestParam(value = "rows",defaultValue = "5") Integer rows
    );
    //http://item-service/spu/page
    //http://127.0.0.1:9081/spu/page


    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long id);

    ///sku/list?id=196
    @GetMapping("sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    @PutMapping
    public Void updateGoods(@RequestBody SpuBo spuBo);

    //根据spuid查询spu
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id")Long spuId);


}
