package com.e3mall.common.pojo;

import java.io.Serializable;

/**
 * Created by Suny on 2017/6/17.
 */
public class EasyUiTreeNode implements Serializable {

    private Long id;
    private String text;
    private String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
