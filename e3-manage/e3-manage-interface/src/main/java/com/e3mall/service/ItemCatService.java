package com.e3mall.service;

import com.e3mall.common.pojo.EasyUiTreeNode;

import java.util.List;

/**
 * Created by Suny on 2017/6/17.
 */
public interface ItemCatService {

    public List<EasyUiTreeNode> queryItemCatList(Long parentId);
}
