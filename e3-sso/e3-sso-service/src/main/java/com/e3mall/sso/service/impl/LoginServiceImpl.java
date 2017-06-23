package com.e3mall.sso.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.redis.JedisClient;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbUserMapper;
import com.e3mall.pojo.TbUser;
import com.e3mall.pojo.TbUserExample;
import com.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * 用户登录处理
 * Created by Suny on 2017/6/23.
 */
@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_USER_EXPIRE}")
    private Integer SESSION_USER_EXPIRE;

    @Override
    public E3Result userLogin(String username, String password) {
         // 1.查询数据库用户名和密码是否正确
        TbUserExample example = new TbUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<TbUser> user = userMapper.selectByExample(example);

        // 2.如果用户不存在,返回错误信息
        if (user == null || user.size() == 0) {
            return new E3Result(500,"用户名或密码错误",null);
        }
        //如果用户存在,获取用户信息
        TbUser user1 = user.get(0);
        //验证密码是否正确,由于数据库的密码是加密的,所以这里先对密码进行加密,再与数据库的密码进行验证
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user1.getPassword())) {
            return new E3Result(500,"用户名或密码错误",null);
        }

        // 3.如果用户存在并且密码正确,生成token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 4.将token作为key写入redis中,并设置生存时间
        //写入缓存时,将密码清空,后期可能会调用用户数据,我们不需要将密码展示
        user1.setPassword(null);
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user1));
        jedisClient.expire("SESSION:" + token,SESSION_USER_EXPIRE);

         // 5.将token返回controller
        return new E3Result(200,"OK",token);
    }
}
