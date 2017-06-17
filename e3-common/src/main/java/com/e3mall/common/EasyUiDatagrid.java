package com.e3mall.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Suny on 2017/6/17.
 */
public class EasyUiDatagrid implements Serializable{
    private Long total;
    private List<?> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
