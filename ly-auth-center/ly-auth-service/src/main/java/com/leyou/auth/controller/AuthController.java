package com.leyou.auth.controller;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 今后登录直接找这里，登录的参数为用户名和密码
     * 没有返回结果，登录成功后会以用户的信息为基础
     * 生成token，token经过cookie来返回
     * @param username
     * @param password
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@PathVariable("username")String username,
                                         @PathVariable("password")String password,
                                         HttpServletRequest request,
                                         HttpServletResponse response
                                         ) throws Exception {
        //生成加密后的字符串token
        String token = authService.accredit(username,password);
        if(null == token){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //向浏览器写入cookie
        CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getCookieMaxAge(), null, true);
        //如果生成了token则，使用cookie保存token
        return ResponseEntity.ok().build();
    }

    /**
     * 使用注解直接获取对应的cookie的值
     *
     * 由于每次用户在前端操作都会执行verify方法，所以，我们可以在verify中重新生成token和cookie
     *
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN") String s,HttpServletRequest request, HttpServletResponse response) {

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(s,jwtProperties.getPublicKey());
            if(userInfo==null){

                // 生成token
                String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                //向浏览器写入cookie
                CookieUtils.setCookie(request,response,jwtProperties.getCookieName(),token,jwtProperties.getCookieMaxAge(), null, true);

                return ResponseEntity.ok(userInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 抛出异常，证明token无效，直接返回401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }


}
