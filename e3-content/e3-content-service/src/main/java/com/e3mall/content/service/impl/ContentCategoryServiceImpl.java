package com.e3mall.content.service.impl;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiTreeNode;
import com.e3mall.content.service.ContentCategoryService;
import com.e3mall.mapper.TbContentCategoryMapper;
import com.e3mall.pojo.TbContentCategory;
import com.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Suny on 2017/6/19.
 */
@Service
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
            EasyUiTreeNode treeNode = new EasyUiTreeNode();
            treeNode.setText(category.getName());
            treeNode.setId(category.getId());
            treeNode.setState(category.getIsParent() ? "closed" : "open");
            //将数据添加进集合
            easyUiList.add(treeNode);
        }
        return easyUiList;
    }

    /**
     * 新增内容分类
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public E3Result addCategory(Long parentId, String name) {
        //补全属性
        TbContentCategory category = new TbContentCategory();
        category.setParentId(parentId);
        category.setName(name);
        category.setCreated(new Date());
        category.setSortOrder(1);
        category.setUpdated(category.getCreated());
        category.setIsParent(false);
        //1-正常  2-删除
        category.setStatus(1);
        Boolean flag = false;
        flag = categoryMapper.insertSelective(category) == 1 ? true : false;
        if (flag) {
            //如果新增成功,那么修改父节点状态
            TbContentCategory category1 = new TbContentCategory();
            category1.setId(parentId);
            category1.setIsParent(true);
            flag = categoryMapper.updateByPrimaryKeySelective(category1) == 1 ? true : false;
        }

        E3Result result = new E3Result(500, "error", null);
        if (flag) {
            TbContentCategoryExample example = new TbContentCategoryExample();
            example.createCriteria().andParentIdEqualTo(parentId).andNameEqualTo(name).andCreatedEqualTo(category.getCreated());
            List<TbContentCategory> list = categoryMapper.selectByExample(example);
            result = new E3Result(200, "OK", list.get(0));
        }
        return result;
    }

    /**
     * 修改内容分类名称
     * @param id
     * @param name
     * @return
     */
    @Override
    public E3Result updateCategory(Long id, String name) {
        TbContentCategory category = new TbContentCategory();
        category.setId(id);
        category.setName(name);
        categoryMapper.updateByPrimaryKeySelective(category);

        E3Result result = new E3Result(200, "OK", null);
        return result;
    }

    /**
     * 根据id删除内容分类
     * 判断是否有兄弟节点,以及是否有子节点,如果有子节点是父节点,那么不允许删除
     * @param id
     * @return
     */
    @Override
    public E3Result delCategory(Long id) {
        TbContentCategory category = categoryMapper.selectByPrimaryKey(id);
        //判断该节点是否是父节点
        Boolean isParent = category.getIsParent();
        if (isParent) {
            //如果是父节点,获取它所有的子节点
            TbContentCategoryExample example = new TbContentCategoryExample();
            //设置条件,查询所有父节点是该节点的对象,并且状态是正常的
            example.createCriteria().andParentIdEqualTo(category.getId()).andStatusEqualTo(1);
            //获取子节点
            List<TbContentCategory> childList = categoryMapper.selectByExample(example);
            //定义集合,存储子节点id
            List<Long> list = new ArrayList<>();
            //将自身也加入集合
            list.add(id);
            //遍历子节点中是否有父节点,如果有,不许删除,没有就将id存入集合
            for (TbContentCategory category1 : childList) {
                if (category1.getIsParent()) {
                    E3Result result = new E3Result(500, "对不起!子节点中有父节点,请先删除其中所有的父节点再删除自身!!", null);
                    return result;
                }else {
                    list.add(category1.getId());
                }
            }

            //批量删除节点
            TbContentCategoryExample example1 = new TbContentCategoryExample();
            example1.createCriteria().andIdIn(list);
            categoryMapper.deleteByExample(example1);

            //修改父节点状态
            TbContentCategory parent = categoryMapper.selectByPrimaryKey(category.getParentId());
            parent.setIsParent(false);
            parent.setUpdated(new Date());
            categoryMapper.updateByPrimaryKeySelective(parent);
            return new E3Result(200,"OK",null);
        }else {
            //如果不是父节点,判断是否有兄弟节点,有兄弟节点就不需改变父节点状态
            TbContentCategoryExample example = new TbContentCategoryExample();
            example.createCriteria().andParentIdEqualTo(category.getParentId());
            List<TbContentCategory> list = categoryMapper.selectByExample(example);
            if (list.size() == 1) {
                //说明没有兄弟节点, 要改变父节点的状态
                TbContentCategory parent = categoryMapper.selectByPrimaryKey(category.getParentId());
                parent.setIsParent(false);
                parent.setUpdated(new Date());
                categoryMapper.updateByPrimaryKeySelective(parent);
            }else {
                //有兄弟节点,直接删除自身
                categoryMapper.deleteByPrimaryKey(category.getId());
            }
            return new E3Result(200,"OK",null);
        }
    }

}
