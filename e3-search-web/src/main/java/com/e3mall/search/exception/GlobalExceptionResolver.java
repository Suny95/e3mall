package com.e3mall.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Suny on 2017/6/21.
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //写日志文件
        logger.info("系统发生异常,请及时检查!!!");
        logger.error("系统发生异常,请及时检查!!!",e);
        //发短信,发邮件通知运维
        //展示错误页面
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/exception");
        return mv;
    }
}
