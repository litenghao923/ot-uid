package com.moma.otasset.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.moma.otasset.config.ot.AssetApi;
import com.moma.otasset.dao.domain.AssetChange;
import com.moma.otasset.dao.domain.AssetUser;
import com.moma.otasset.dao.vo.AssetChangeVo;
import com.moma.otasset.service.AssetService;
import com.moma.otasset.util.StringUtil;
import com.moma.otasset.web.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private NoticeController noticeController;

    @Autowired
    AssetApi assetApi;

    //获取所有用户信息
    @GetMapping("getAllUsers")
    public WebResult getAllUsers(HttpServletRequest request) {
        List<AssetUser> allUserData = assetService.getAllUserData();
        if (allUserData != null) {
            JSONArray jsonArray = new JSONArray();
            allUserData.forEach(o -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("niceName", o.getNickName());
                jsonObject.put("uid", o.getHuobiUid());
                jsonArray.add(jsonObject);
            });
            return WebResult.okResult(jsonArray);
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
        //调用充提接口
        JSONObject subuserTransfer = createSubuserTransfer(uid, amount, operationType == 1 ? "充值" : "提现");
        if (subuserTransfer.getInteger("status") != 2000) {
            return WebResult.failResult(subuserTransfer.getString("msg"));
        }
        //入库充值提现记录
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

    @GetMapping("changeList")
    public WebResult changeList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            List<AssetChange> assetChangeByPage = assetService.getAssetChangeByPage(pageNum, pageSize);
            if (assetChangeByPage != null) {
                JSONObject jsonObject = new JSONObject();
                int maxPage = assetService.getMaxPage(2, pageSize);
                jsonObject.put("maxPage", maxPage);
                jsonObject.put("nowPage", pageNum);
                List<AssetChangeVo> assetChangeVos = new ArrayList<>();
                assetChangeByPage.forEach(o -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format = simpleDateFormat.format(o.getcTime());
                    AssetChangeVo assetChangeVo = new AssetChangeVo();
                    BeanUtils.copyProperties(o, assetChangeVo);
                    assetChangeVo.setCTime(format);
                    assetChangeVos.add(assetChangeVo);
                });
                jsonObject.put("changeList", assetChangeVos);
                return WebResult.okResult(jsonObject);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("maxPage", 0);
            jsonObject.put("nowPage", 0);
            jsonObject.put("changeList", Collections.EMPTY_LIST);
            return WebResult.okResult(jsonObject);
        } catch (Exception e) {
            log.error("获取充提记录出错，错误信息:{}", e.getMessage(), e);
            return WebResult.failException();
        }
    }


    @GetMapping("accountOverview")
    public WebResult accountOverview() {
        try {
            Map<String, BigDecimal> accountOverview = assetService.getAccountOverview();
            return WebResult.okResult(accountOverview);
        } catch (Exception e) {
            log.error("获取账户总览数据出错，错误信息：{}", e.getMessage(), e);
            return WebResult.failException();
        }
    }

    @PostMapping("transferAccounts")
    public WebResult transferAccounts(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        //----------------------------------用户身份检测-------------------------------------------
        //判断验证码不能为空
        Object code = jsonObject.get("code");
        if (code == null && StringUtil.isEmpty(code.toString())) {
            return WebResult.failResult(1023);
        }
        //获取用户信息
        String phone = "18621670791";
        if (StringUtil.isEmpty(phone)) {
            return WebResult.failResult("用户手机号不存在");
        }
        String areaCode = "86";
        //验证手机号验证码正确性
        if (!noticeController.checkPhoneCode(phone, areaCode, code.toString())) {
            return WebResult.failResult(1002);
        }
        try {
            //执行业务
            BigDecimal amount = jsonObject.getBigDecimal("amount");
            //1.交易团队转给币coin 2.币coin转给交易团队
            Integer type = jsonObject.getInteger("type");
            if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
                return WebResult.failResult("转账金额不能为0");
            }
            if (type == null) {
                return WebResult.failResult("type不能为空");
            }
            String s = assetService.biCoinToTeam(amount, type);
            return WebResult.okResult(s);
        } catch (Exception e) {
            log.info("转账出现异常，请检查，错误信息：{}", e.getMessage(), e);
            return WebResult.failException();
        }
    }

    @GetMapping("allAssetUsers")
    public WebResult allAssetUsers(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<AssetUser> assetUserByPage = assetService.getAssetUserByPage(pageNum, pageSize);
        if (assetUserByPage != null && assetUserByPage.size() > 0) {
            JSONObject jsonObject = new JSONObject();
            int maxPage = assetService.getMaxPage(1, pageSize);
            jsonObject.put("maxPage", maxPage);
            jsonObject.put("nowPage", pageNum);
            jsonObject.put("assetUsers", assetUserByPage);
            return WebResult.okResult(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", 0);
        jsonObject.put("nowPage", 0);
        jsonObject.put("changeList", Collections.EMPTY_LIST);
        return WebResult.okResult(Collections.EMPTY_LIST);
    }

    private JSONObject createSubuserTransfer(Long uid, BigDecimal amount, String type) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subUid", uid);
        jsonObject.put("amount", amount);
        jsonObject.put("currency", "USDT");
        jsonObject.put("type", type);
        try {
            String body = assetApi.createSubuserTransfer(jsonObject).execute().body();
            JSONObject res = JSONObject.parseObject(body);
            return res;
        } catch (Exception e) {
            log.error("调用充提接口出错，请检查，错误信息：{}", e.getMessage(), e);
            return null;
        }
    }

}
