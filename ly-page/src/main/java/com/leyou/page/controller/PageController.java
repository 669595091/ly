package com.leyou.page.controller;

import com.leyou.page.service.FileService;
import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
    @Autowired
    private PageService pageService;
    @Autowired
    private FileService fileService;

    @GetMapping("item/{id}.item")
    public String toPage(@PathVariable("id")Long spuId, Model model){

        model.addAllAttributes(pageService.loadData(spuId) );
        if(fileService.exists(spuId)){//判断路径下有没有静态html文件
            fileService.syncCreateHtml(spuId);

        }

        return "item";
    }


}
