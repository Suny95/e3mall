package com.e3mall.content.service;

import com.e3mall.common.pojo.EasyUiTreeNode;

import java.util.List;

/**
 * Created by Suny on 2017/6/19.
 */

public interface ContentCategoryService {

    public List<EasyUiTreeNode> queryContentCategoryByParentId(Long parentId);
}
