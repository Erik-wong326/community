package com.cqupt.community.entity;


/**
 * @author Erik_Wong
 * @version 1.0
 * 封装分页相关信息
 */
public class Page {
    //当前页码
    private int current = 1;
    //显示上限
    private int limit = 10;
    //数据总数量(用于计算总页数)
    private int rows;
    //查询路径(复用分页的链接)
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        //避免 current 写成负数等非法数值
        if (current >= 1){
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100){
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows>=0){
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * 供数据库使用
     * @return
     */
    public int getOffset(){
        //current * limit - limit
        return (current-1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        //rows / limit 总行数 / 每页可显示的数据
        if (0 == rows % limit){
            return rows /limit;
        }else {//不能整除的情况
            return rows / limit+1;
        }
    }

    /**
     * 获取起始页码 -> 当前页离他最近的两页
     * @return
     */
    public int getFrom(){
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取结束页码
     * @return
     */
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }

}
