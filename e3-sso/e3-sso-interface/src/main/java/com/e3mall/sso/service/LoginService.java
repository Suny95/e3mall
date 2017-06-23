package com.e3mall.sso.service;

import com.e3mall.common.pojo.E3Result;

/**
 * Created by Suny on 2017/6/23.
 */
public interface LoginService {

    E3Result userLogin(String username, String password);
}
