package com.shangan.mall.controller;

import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "v1", tags = "上岸商城商品图片上传相关接口")
@RequestMapping("/api/private/v1")
public class PicUploadController {

    @PostMapping(value = "/upload")
    public Result<?> fileUpload(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request) {
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "D://upload//"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String filename = "/temp-rainy/" + fileName;
        //model.addAttribute("filename", filename);
        String filename = "/goods-img/" + fileName;
        return ResultGenerator.genSuccessResult((Object) filename);
    }
}
