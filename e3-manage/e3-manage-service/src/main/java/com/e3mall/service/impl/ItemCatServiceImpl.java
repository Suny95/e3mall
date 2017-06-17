package com.e3mall.service.impl;

import com.e3mall.common.pojo.EasyUiTreeNode;
import com.e3mall.mapper.TbItemCatMapper;
import com.e3mall.pojo.TbItemCat;
import com.e3mall.pojo.TbItemCatExample;
import com.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suny on 2017/6/17.
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUiTreeNode> queryItemCatList(Long parentId) {
        //创建条件对象
        TbItemCatExample example = new TbItemCatExample();
        //设置条件
        example.createCriteria().andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
        //准备返回值对象
        ArrayList<EasyUiTreeNode> treeNodes = new ArrayList<>();
        //遍历查询的结果,封装数据到返回值对象中
        for (TbItemCat itemCat : itemCatList) {
            EasyUiTreeNode node = new EasyUiTreeNode();
            node.setId(itemCat.getId());
            node.setText(itemCat.getName());
            node.setState(itemCat.getIsParent() ? "closed" : "open");
            //添加到返回值对象的集合中
            treeNodes.add(node);
        }
        return treeNodes;
    }
}
