package com.shangan.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Alva
 * @CreateTime 2021/1/24 23:10
 * 使用统一的结果响应对象来处理请求的数据返回，这样做的好处是可以保证所有接口响应数据格式的统一，
 * 大大地减少接口响应的工作量和避免接口应答的不统一而造成的开发问题。
 * 例如，有些接口需要返回简单的对象，比如字符串或者数字；
 * 有些接口需要返回一个复杂的对象，比如用户详情接口、商品详情接口，这些接口就需要返回不同的对象；
 * 有些接口又需要返回列表对象或者分页数据，这些对象又复杂了一些。
 * @ApiModelProperty: 在该类表示在 Swagger 中对属性的说明
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 业务码，比如成功、失败、权限不足等 code，可自定义
     */
    @ApiModelProperty("返回码")
    private int resultCode;

    /**
     * 返回信息，后端在进行业务处理后返回给前端一个提示信息，可自行定义
     */
    @ApiModelProperty("返回信息")
    private String message;

    /**
     * 数据结果，泛型，可以是列表、单个对象、数字、布尔值等
     */
    @ApiModelProperty("返回数据")
    private T data;
}
