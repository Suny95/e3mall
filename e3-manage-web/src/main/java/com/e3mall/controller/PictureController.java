package com.e3mall.controller;

import com.e3mall.common.utils.FastDFSClient;
import com.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suny on 2017/6/18.
 */

/**
 * 图片上传
 */
@Controller
public class PictureController {

    @Value("${UPLOAD_IMAGE_URL}")
    private String UPLOAD_IMAGE_URL;

    @RequestMapping(value = "/pic/upload",produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    @ResponseBody
    public String uploadPicture(MultipartFile uploadFile) {
        try {
            //使用图片上传工具类加载配置
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.properties");
            //获取文件后缀
            String filename = uploadFile.getOriginalFilename();
            String extName = filename.substring(filename.lastIndexOf(".") + 1);
            //上传到服务器,获取到文件路径,并补充完整
            String url = UPLOAD_IMAGE_URL + fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //封装数据,返回客户端
            Map map = new HashMap<>();
            map.put("error",0);
            map.put("url",url);
            return JsonUtils.objectToJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            //上传失败,返回错误信息
            Map map = new HashMap<>();
            map.put("error",1);
            map.put("message","图片上传失败");
            return JsonUtils.objectToJson(map);
        }
    }

}
