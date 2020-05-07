package com.leyou.page.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Service
public class FileService {

    @Value("${ly.thymeleaf.destPath}")
    private String path;
    @Autowired
    private PageService pageService;
    @Autowired
    private TemplateEngine templateEngine;

    public boolean exists(Long spuId) {
        File file = new File(path);
        if(!file.exists()){
            //没有文件的话创建文件夹
            file.mkdirs();
        }

        return new File(file,spuId+"+.html").exists();
    }

    public void syncCreateHtml(Long spuId) {

        Context context = new Context();   //创建上下文对象
        //放入数据
        context.setVariables(pageService.loadData(spuId));
        //创建文件对象
        File file = new File(path,spuId+"+.html");

        try {
            //打印输出流
            PrintWriter printWriter = new PrintWriter(file,"utf-8");
            //产生静态文件
            templateEngine.process("item",context,printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //删除文件
    public void syncDeleteHtml(Long id) {
        File file = new File(path,id+".html");
        file.deleteOnExit();

    }
}
