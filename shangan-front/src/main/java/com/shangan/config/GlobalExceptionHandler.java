package com.shangan.config;

/**
 * @Author Alva
 * @CreateTime 2021/1/27 12:04
 * 创建统一异常处理器
 */
public class GlobalExceptionHandler extends RuntimeException{

    public GlobalExceptionHandler() {
    }

    public GlobalExceptionHandler(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     * @param message
     */
    public static void fail(String message) {
        throw new GlobalExceptionHandler(message);
    }


}
