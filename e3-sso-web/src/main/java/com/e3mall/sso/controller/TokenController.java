package com.e3mall.sso.controller;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Suny on 2017/6/23.
 */
@Controller
public class TokenController {
    @Autowired
    private TokenService tokenService;

    /**
     * spring4.1版本之前必须要手动拼字符串
     */
/*    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String queryUserByToken(@PathVariable String token, String callback) {
        E3Result result = tokenService.queryUserByToken(token);
        if (StringUtils.isNotBlank(callback)) {
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }*/

    /**
     * spring4.2版本之后提供了一个API,可以帮我们完成js字符串的拼接
     * @param token
     * @param callback
     * @return
     */
    @RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object queryUserByToken(@PathVariable String token, String callback) {
        E3Result result = tokenService.queryUserByToken(token);
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }
}
