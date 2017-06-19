package com.e3mall.content.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiTreeNode;

import java.util.List;

/**
 * Created by Suny on 2017/6/19.
 */

public interface ContentCategoryService {

    public List<EasyUiTreeNode> queryContentCategoryByParentId(Long parentId);

    public E3Result addCategory(Long parentId,String name);

    public E3Result updateCategory(Long id,String name);

    public E3Result delCategory(Long id);
}
