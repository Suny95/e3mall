package com.e3mall.sso.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.mapper.TbUserMapper;
import com.e3mall.pojo.TbUser;
import com.e3mall.pojo.TbUserExample;
import com.e3mall.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Suny on 2017/6/23.
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public E3Result checkData(String param, Integer type) {
        //判断type,生成对应的查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1:用户名 2:手机号 3:邮箱
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        }else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        }else if (type == 3) {
            criteria.andEmailEqualTo(param);
        }else {
            return new E3Result(406,"数据类型错误!",false);
        }
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            //如果有数据返回false
            return new E3Result(200,"",false);
        }
        //没有数据返回true
        return new E3Result(200,"",true);
    }

    @Override
    public E3Result register(TbUser user) {
        //再次校验数据完整性
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getPassword())) {
            return new E3Result(500,"用户数据不完整,注册失败",false);
        }

        //校验数据是否存在
        //校验用户名
        E3Result result = checkData(user.getUsername(), 1);
        if (!(Boolean) result.getData()) {
            return new E3Result(500,"用户名已存在",false);
        }
        //校验手机号
        result = checkData(user.getPhone(),2);
        if (!(Boolean) result.getData()) {
            return new E3Result(500,"手机号已被注册",false);
        }

        //补全属性
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        //对密码加密, spring框架自带了md5加密工具类,DigestUtils.md5DigestAsHex() 在spring-core的jar包中
        String md5Pwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pwd);

        //插入数据
        userMapper.insertSelective(user);
        //返回成功
        return new E3Result(200,"注册成功",null);
    }
}
