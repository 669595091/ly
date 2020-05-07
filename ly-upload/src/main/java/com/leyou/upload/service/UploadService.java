package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;


    public String uploadImage(MultipartFile file) {
       /*
       //创建file对象
        File f = new File("E:\\upload");
        if(!f.exists()){//如果路劲不为空就新建文件夹
            f.mkdirs();
        }

        try {
            //如果存在文件夹就保存文件到目录下
            file.transferTo(new File(f,file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //拼接图片地址返回
        String url = "http://image.leyou.com"+file.getOriginalFilename();
        return url;
        */

        String url = null;
        //获取浏览器传入图片后缀
        String originalFilename = file.getOriginalFilename();
        //获取图片后缀
        String suffix = StringUtils.substringAfter(originalFilename,".");

        try {
            //上传
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),file.getSize(),suffix,null);

            url = "http://image.leyou.com"+storePath.getFullPath();//获取路劲
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
