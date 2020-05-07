package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.po.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        //开启分页
        PageHelper.startPage(page,rows);
        //查询条件封装到example中
        Example example = new Example(Spu.class);
        //获取criteria对象
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isBlank(key)){
            criteria.andLike("title","%"+key+"%");//创建like搜索条件:title like %小米%
        }
        //排序
        if(null != saleable){
            criteria.andEqualTo("saleable",saleable);//设置上架条件是否为true

        }
        Page<Spu> spuPage = (Page<Spu>) this.spuMapper.selectByExample(example);

        ArrayList<SpuBo> spuBos = new ArrayList<>();
        List<Spu> spuPageResult = spuPage.getResult();
        for (Spu spu:spuPageResult){
            SpuBo spuBo = new SpuBo();
            //因为spu中缺少两个字段,所以将spu中的数据copy到spubo中返回
            BeanUtils.copyProperties(spu,spuBo);
            //通过分类ID获取分类名称
            List<String> names = this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));//返回tb_category的一级二级三级菜单的名称
            //将获取到的名称拼接为前端显示样式
            String join = StringUtils.join(names, "/");
            //分类名称
            spuBo.setCname(join);
            //品牌名称
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());

            spuBos.add(spuBo);


        }

        return new PageResult<>(spuPage.getTotal(),new Long(spuPage.getPages()),spuBos);

    }

    @Transactional
    public void saveGoods(SpuBo spuBo) {
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(new Date());
        //保存数据
        this.spuMapper.insertSelective(spuBo);

        Long id = spuBo.getId();
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(id);
        //保存spuDetail到数据
        this.spuDetailMapper.insertSelective(spuDetail);

        List<Sku> skus = spuBo.getSkus();
        //保存sku和stock
        saveSkus(spuBo,skus);

        //发送消息
        sendMessage(id,"insert");

    }

    private void saveSkus(SpuBo spuBo, List<Sku> skus) {
        for (Sku s:skus){
            s.setSpuId(spuBo.getId());
            s.setCreateTime(new Date());
            s.setLastUpdateTime(new Date());
            //保存
            this.skuMapper.insertSelective(s);

            Stock stock = new Stock();
            stock.setSkuId(s.getSpuId());
            stock.setStock(s.getStock());
            this.stockMapper.insert(stock);

        }
    }

    public SpuDetail querySpuDetailBySpuId(Long id) {
        return  this.spuDetailMapper.selectByPrimaryKey(id);
    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);

        List<Sku> skuList = skuMapper.select(sku);
        for (Sku s:skuList){
            Long sId = s.getId();
            Stock stock = this.stockMapper.selectByPrimaryKey(sId);
            s.setStock(stock.getStock());

        }
        return skuList;
    }
    //修改商品表
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        spuBo.setLastUpdateTime(new Date());
        //更新spu
        this.spuMapper.updateByPrimaryKey(spuBo);
        //更新spudetail
        this.spuDetailMapper.updateByPrimaryKey(spuBo.getSpuDetail());
        //查询sku,stock后删除
        Long spuId = spuBo.getId();
        Sku sku = new Sku();
        sku.setSpuId(spuId);

        List<Sku> skuList = this.skuMapper.select(sku);//查询到sku
        for (Sku s:skuList){
            this.skuMapper.delete(s);//删除sku
            this.stockMapper.deleteByPrimaryKey(s.getId());//删除stock

        }
        saveSkus(spuBo,spuBo.getSkus());//新增sku

        //发送消息
        sendMessage(spuId,"update");



    }

    public Spu querySpuById(Long spuId) {
        return this.spuMapper.selectByPrimaryKey(spuId);
    }

    //发送消息的通用方法
    public void sendMessage(Long id,String type){//id商品id,type就是增删改查的操作类型
        this.amqpTemplate.convertAndSend("item."+type,id);
    }
}
