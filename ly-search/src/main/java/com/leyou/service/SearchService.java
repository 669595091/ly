package com.leyou.service;

import com.leyou.common.po.PageResult;
import com.leyou.pojo.Goods;
import com.leyou.repository.GoodsRepository;
import com.leyou.utils.SearchRequest;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    @Autowired
    private GoodsRepository goodsRepository;


    public PageResult<Goods> search(SearchRequest searchRequest) {
        //用户搜索关键字
        String key = searchRequest.getKey();
        //第几页
        Integer page = searchRequest.getPage();
        //创建查询对象NativeSearchQueryBuilder
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //查询条件
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("all",key).operator(Operator.AND));
        //分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page-1,searchRequest.getSize()));
       //过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));
        //搜索
        Page<Goods> goodsPage = goodsRepository.search(nativeSearchQueryBuilder.build());


        return new PageResult<>(goodsPage.getTotalElements(),new Long(goodsPage.getTotalPages()),goodsPage.getContent());
    }
}
