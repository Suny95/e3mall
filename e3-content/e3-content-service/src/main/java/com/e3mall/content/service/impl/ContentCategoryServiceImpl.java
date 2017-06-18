package com.e3mall.content.service.impl;

import com.e3mall.common.pojo.EasyUiTreeNode;
import com.e3mall.content.service.ContentCategoryService;
import com.e3mall.mapper.TbContentCategoryMapper;
import com.e3mall.pojo.TbContentCategory;
import com.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suny on 2017/6/19.
 */
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper categoryMapper;

    /**
     * 根据父级类目ID查询所有子级类目
     * @param parentId
     * @return
     */
    @Override
    public List<EasyUiTreeNode> queryContentCategoryByParentId(Long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        //设置查询条件
        example.createCriteria().andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> list = categoryMapper.selectByExample(example);
        //重新封装数据
        List<EasyUiTreeNode> easyUiList = new ArrayList<>();
        for (TbContentCategory category : list) {
            EasyUiTreeNode node = new EasyUiTreeNode();
            node.setId(category.getParentId());
            node.setText(category.getName());
            node.setState(category.getIsParent() ? "closed" : "open");
            //将数据添加进集合
            easyUiList.add(node);
        }
        return easyUiList;
    }
}
