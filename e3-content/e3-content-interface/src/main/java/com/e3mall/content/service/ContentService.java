package com.e3mall.content.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUiDatagrid;
import com.e3mall.pojo.TbContent;

import java.util.List;

/**
 * Created by Suny on 2017/6/19.
 */
public interface ContentService {

    public E3Result addContent(TbContent content);

    public EasyUiDatagrid queryContentById(Long categoryId, Integer page, Integer rows);

    public List<TbContent> queryContentByCid(Long cid);
}
