package com.leyou.common.po;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private Long total;//一共多少条数据
    private Long totalPage;//一共多少页
    private List<T> items;//每页显示的数据

    public PageResult() {
        super();
    }

    public PageResult(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }

    //get和set

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
