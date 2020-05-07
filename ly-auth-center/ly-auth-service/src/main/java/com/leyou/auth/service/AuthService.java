package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;


    public String accredit(String username, String password) throws Exception {

        try{
        //使用feign调用用户接口
        User user = userClient.queryUser(username, password);
        if (null == user) {
            return null;
        }
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}