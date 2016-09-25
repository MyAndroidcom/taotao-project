package com.xhp.mybatis.bean;

import java.util.List;

/*
 * 封装easyui的datagride格式的数据参数
 * total     rows
 * */

public class EasyUIResult {
    private Long total;
    private List<?> rows;
    public EasyUIResult(Long total, List<?> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }
    public EasyUIResult() {
        // TODO Auto-generated constructor stub
    }
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
