package com.e3mall.item.listener;

import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suny on 2017/6/22.
 */
public class HtmlGenericListener implements MessageListener {

    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 商品静态页面生成目录
     */
    @Value("${HTML_GENERIC_PATH}")
    private String HTML_GENERIC_PATH;

    @Override
    public void onMessage(Message message) {
        //创建一个模板
        //从消息中获取商品id
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = Long.parseLong(text);
            //等待事务提交
            Thread.sleep(50);

            //根据商品id查询到商品的基本信息与商品描述信息
            TbItem tbItem = itemService.queryTbItemById(itemId);
            Item item = new Item(tbItem);
            TbItemDesc itemDesc = itemService.queryItemDescById(itemId);

             //创建数据集
             Map data =new HashMap();
             data.put("item",item);
             data.put("itemDesc",itemDesc);

            //创建模板对象,加载模板文件
             Configuration configuration = freeMarkerConfigurer.getConfiguration();
             Template template = configuration.getTemplate("item.ftl");

            //创建输出流指定输出位置与文件名,文件名是  商品id.html
            Writer out = new FileWriter(HTML_GENERIC_PATH + itemId + ".html");
            //写出文件
            template.process(data,out);
            //关闭流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
