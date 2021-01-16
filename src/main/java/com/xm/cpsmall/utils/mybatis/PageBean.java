package com.xm.cpsmall.utils.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageBean<T> implements Serializable {
    private static final long serialVersionUID = 8656597559014685635L;
    private long total;             //总记录数
    private int pageNum;            // 第几页
    private int pageSize;           // 每页记录数
    private int pages;              // 总页数
    private List<T> list;           //结果集
    private Integer additional;     //附加参数

    public PageBean() {}

    /**
     * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理，
     * 而出现一些问题。
     * @param list          page结果
     */
    public PageBean(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.list = page;
        }else {
            this.list = list;
        }
    }
    public PageBean(PageBean pageBean) {
        this.pageNum = pageBean.getPageNum();
        this.pageSize = pageBean.getPageSize();
        this.total = pageBean.getTotal();
        this.pages = pageBean.getPages();
    }

    public boolean hasNext(){
        if(!ObjectUtil.isAllNotEmpty(pageNum,pageSize,total))
            return false;
        return pageNum * pageSize < total;
    }
}
