package com.e3mall.item.pojo;

import com.e3mall.pojo.TbItem;

/**
 * Created by Suny on 2017/6/22.
 */
public class Item extends TbItem {
    public Item() {
    }

    public Item(TbItem tbItem) {
        this.setId(tbItem.getId());
        this.setBarcode(tbItem.getBarcode());
        this.setCid(tbItem.getCid());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());
        this.setImage(tbItem.getImage());
        this.setNum(tbItem.getNum());
        this.setPrice(tbItem.getPrice());
        this.setSellPoint(tbItem.getSellPoint());
        this.setTitle(tbItem.getTitle());
        this.setStatus(tbItem.getStatus());
    }

    public String[] getImages() {
        if (super.getImage() != null && !"".equals(super.getImage())) {
            return super.getImage().split(",");
        }
        return null;
    }
}
