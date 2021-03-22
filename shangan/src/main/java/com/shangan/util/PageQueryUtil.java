package com.shangan.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 19:52
 * 分页查询工具类，封装分页查询出来的商品结果
 */
public class PageQueryUtil extends LinkedHashMap<String, Object> {

//    当前页码
    private int page;
//    每页条数
    private int limit;

    /**
     * 构造分页查询对象，以便在 Controller 封装该类后，直接调用分页查询的 Service 接口
     * @param params
     */
    public PageQueryUtil(Map<String, Object> params) {
        this.putAll(params);

//        将分页参数转换为 int 类型
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());

        this.put("start", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
    }
//    Getter & Setter & toString
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "PageUtil{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }
}
