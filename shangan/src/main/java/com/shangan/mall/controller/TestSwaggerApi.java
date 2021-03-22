package com.shangan.mall.controller;

import com.shangan.mall.entity.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author Alva
 * @CreateTime 2021/1/24 22:30
 * 用户数据查询方法使用 GET 请求，用户添加方法 使用 POST 请求，对应的修改和删除操作使用 PUT 和 DELETE 请求，同时对于 api 的请求路径也按照设计规范进行设置，虽然有些映射路径相同，但是会根据请求方法进行区分.
 * 同时，每一个返回结果我们都统一使用 Result 类进行包装之后再返回给前端，并使用 @ResponseBody 注解将其转换为 json 格式。
 *
 *  @ApiOperation 注解给 API 增加说明
 *  @ApiImplicitParams、@ApiImplicitParam 注解给参数增加说明
 *  @RequestBody、@RestController 注解进行请求实体的转换和响应结果的格式化输出，
 *  以普遍使用的 json 数据为例，这两个注解的作用分别可以将请求中的数据解析成 json 并绑定为实体对象以及将响应结果以 json 格式返回给请求发起者
 *  @PathVariable 路径参数接收，可以将参数拼入请求路径中
 */

@RestController
public class TestSwaggerApi {

    static Map<Integer, User> userMap = Collections.synchronizedMap(new HashMap<>());

//    初始化 userMap
    static {
        User user = new User();
        user.setUserId(1L);
        user.setLoginName("Alva");
        user.setPasswordMd5("11111");
        User user2 = new User();
        user.setUserId(2L);
        user2.setLoginName("Sandy");
        user2.setPasswordMd5("222222");
        userMap.put(1, user);
        userMap.put(2, user2);
    }

    @ApiOperation(value = "获取用户列表")
    @GetMapping("/users")
    public List<User> getUserList() {
        return new ArrayList<>(userMap.values());
    }

    @ApiOperation(value = "新增用户", notes = "根据User对象新增用户")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PostMapping("/users")
    public String postUser(@RequestBody User user) {
        userMap.put(user.getUserId().intValue(), user);
        return "新增成功";
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Integer id) {
        return userMap.get(id);
    }

    @ApiOperation(value = "更新用户详细信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "User")
    })
    @PutMapping("/users/{id}")
    public String putUser(@PathVariable Integer id, @RequestBody User user) {
        User tempUser = userMap.get(id);
        tempUser.setLoginName(user.getLoginName());
        tempUser.setPasswordMd5(user.getPasswordMd5());
        userMap.put(id, tempUser);
        return "更新成功";
    }

    @ApiOperation(value = "删除用户", notes = "根据id删除对象")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userMap.remove(id);
        return "删除成功";
    }
}
