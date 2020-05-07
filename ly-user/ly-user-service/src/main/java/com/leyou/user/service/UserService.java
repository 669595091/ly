package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate template;

    //标识
    static final String KEY_PREFIX = "user:code:phone:";

    //验证用户名
    public Boolean checkData(String data, Integer type) {
        //type:  1.用户名   2.手机
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
        }

        //没查到返回0,用户名可用,否则返回1表已存在
        boolean b = userMapper.selectCount(user) != 1;

        return b;


    }
    //手机验证
    public Boolean sendVerifyCode(String phone) {
        //产生验证码,指定长度生成一个随机值
        String code = NumberUtils.generateCode(5);

        try {
            // 发送短信...

            //存入到redis中，并指定有效周期为5分钟
            template.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);

            return true;
        } catch (Exception e) {
            //logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            return false;
        }
    }

    public Boolean register(User user, String code) {
        //获取验证码
        String key = this.template.opsForValue().get(KEY_PREFIX + user.getPhone());
        //没有取到值
        if(null != key){
            return false;
        }
        //渠道数据,但验证码错误
        if(!code.equals(key)){
            return false;
        }
        //插入数据库
        String salt = CodecUtils.generateSalt();  //生成盐
        user.setSalt(salt);
        //进行加密
        String md5Hex = CodecUtils.md5Hex(user.getPassword(), salt);
        //设置加密后密码
        user.setPassword(md5Hex);
        user.setCreated(new Date());
        //插入数据库
        boolean flag = this.userMapper.insertSelective(user)==1;
        if(flag){
            //插入成功,删除redis数据
            this.template.delete(KEY_PREFIX + user.getPhone());
        }
        return flag;


    }

    public User queryUser(String username, String password) {
        //查询用户
        User user = new User();
        user.setUsername(username);

        User queryUser = this.userMapper.selectOne(user);
        //如果查询为空
        if(null == queryUser){
            return null;
        }
        //取出盐
        String salt = queryUser.getSalt();
        String newPassword = CodecUtils.md5Hex(password,salt);
        if(!queryUser.getPassword().equals(newPassword)){
            return null;
        }
        return queryUser;

    }
}