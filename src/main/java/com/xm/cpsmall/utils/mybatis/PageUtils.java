package com.xm.cpsmall.utils.mybatis;

import lombok.Data;

public class PageUtils {
    @Data
    static class Page{
        private Integer pageNum;
        private Integer pageSize;
    }

    public static int calcTotalPage(int total,int pageSize){
        return total != 0 ? (int) (Math.ceil(((double) total) / pageSize)) : 0;
    }

    public static void main(String[] args){
        System.out.println(calcTotalPage(0,2));
    }
}
