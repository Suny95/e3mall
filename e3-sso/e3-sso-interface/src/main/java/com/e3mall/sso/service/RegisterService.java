package com.e3mall.sso.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbUser;

/**
 * Created by Suny on 2017/6/23.
 */
public interface RegisterService {

    E3Result checkData(String param, Integer type);

    E3Result register(TbUser user);
}
