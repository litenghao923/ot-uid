package com.moma.otasset.controller;

import com.alibaba.fastjson.JSONObject;
import com.moma.otasset.service.AssetService;
import com.moma.otasset.web.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    AssetService assetService;

    @PostMapping("addUser")
    public WebResult addUser(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        Long uid = jsonObject.getLong("uid");
        String nickName = jsonObject.getString("nickName");
        if (uid == null) {
            return WebResult.failResult("uid不能为空");
        }
        if (nickName == null) {
            return WebResult.failResult("昵称不能为空");
        }
        Integer integer = assetService.addUser(uid, nickName);
        if (integer > 0) {
            return WebResult.okResult("操作成功!");
        }
        return WebResult.failResult("操作失败!");
    }


}
