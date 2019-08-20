package com.moma.otasset.controller;


import com.alibaba.fastjson.JSONObject;
import com.moma.otasset.service.AssetService;
import com.moma.otasset.util.StringUtil;
import com.moma.otasset.web.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Resource
    private NoticeController noticeController;

    //获取所有用户信息
    @GetMapping
    public WebResult getAllUsers() {
        List<String> allUserData = assetService.getAllUserData();
        if (allUserData != null) {
            return WebResult.okResult(allUserData);
        }
        return WebResult.failResult("未获取到用户相关信息!");
    }

    //处理充值提现
    @PostMapping("recharge")
    public WebResult settingFirmOffer(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        //----------------------------------用户身份检测-------------------------------------------
        //判断验证码不能为空
        Object code = jsonObject.get("code");
        if (code == null && StringUtil.isEmpty(code.toString())) {
            return WebResult.failResult(1023);
        }
        //获取用户信息
        String phone = "13979122221";
        if (StringUtil.isEmpty(phone)) {
            return WebResult.failResult("用户手机号不存在");
        }
        String areaCode = "86";
        //验证手机号验证码正确性
        if (!noticeController.checkPhoneCode(phone, areaCode, code.toString())) {
            return WebResult.failResult(1002);
        }
        //----------------------------------根据执行参数，执行命令---------------------------
        //判断执行类型
        Integer operationType = jsonObject.getInteger("operationType");
        if (operationType == null) {
            return WebResult.failResult("操作类型不能为空");
        }
        //判断uid
        Long uid = jsonObject.getLong("uid");
        if (uid == null) {
            return WebResult.failResult("uid不能为空");
        }
        //判断充值金额
        BigDecimal amount = jsonObject.getBigDecimal("amount");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return WebResult.failResult("充值或提现金额不能为空或者0");
        }
        String res = "";
        if (operationType == 1) {
            res = assetService.rechargeByUid(uid, amount);
        }
        if (operationType == 2) {
            res = assetService.withdrawByUid(uid, amount);
        }
        if (!"".equals(res)) {
            return WebResult.okResult(res);
        }
        return WebResult.failResult("操作失败");
    }

}
