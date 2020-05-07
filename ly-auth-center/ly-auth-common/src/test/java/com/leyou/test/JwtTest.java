package com.leyou.test;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    //先创建tmp/rsa目录
    private static final String pubKeyPath = "E:\\IDEA项目\\temp\\rsa.pub";

    private static final String priKeyPath = "E:\\IDEA项目\\temp\\rsa.pri";

    private PublicKey publicKey; //公钥对象
    private PrivateKey privateKey; //私钥对象

    //第一步产生公钥,私钥 产生后注释
/*    @Test
    public void init() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "1234");

    }*/
    //在所有的测试执行之前，先加载公钥和私钥
    @Before
    public void loadData() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);//获取公钥
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);//获取私钥
    }
    //私钥加密,公钥解密
    //产生字符串,加密
    @Test
    public void getToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(12L, "tom"), privateKey, 5);
        System.out.println("token = " + token);
    }
    //解密
    @Test
    public void jieMi() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MTIsInVzZXJuYW1lIjoidG9tIiwiZXhwIjoxNTc4NzMxNTQwfQ.KRKKvSgM__3T1KuxPTX5I8BQ4Dtcq0PbVRMeICmi2MnIluNM2zQ8lSBQA9kzz-QOy0QDXS3bp_J54lwDYTv5P9YyGRyN-OR61PvvuIF8T2IbevjvPKGZhCH8u30XPmX4V0WqekQ4Cu109ky9IpSJeFjdxsEMjW9BrV0omcZSftE";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());

    }


}
