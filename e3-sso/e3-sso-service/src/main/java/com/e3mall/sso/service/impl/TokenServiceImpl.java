package com.e3mall.sso.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.redis.JedisClient;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Suny on 2017/6/23.
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_USER_EXPIRE}")
    private Integer SESSION_USER_EXPIRE;

    @Override
    public E3Result queryUserByToken(String token) {
        //根据token查询redis中的用户信息
        String json = jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(json)) {
            //如果查不到,返回错误信息
            return new E3Result(500,"登录已过期,请重新登录!!",null);
        }

        //如果找到,重新设置key的过期时间
        jedisClient.expire("SESSION:" + token,SESSION_USER_EXPIRE);
        //返回用户信息
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return new E3Result(200,"OK",user);
    }
}
