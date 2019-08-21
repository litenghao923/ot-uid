package com.moma.otasset.controller;

import com.alibaba.fastjson.JSONObject;
import com.moma.otasset.service.redis.ObjectRedisService;
import com.moma.otasset.util.AiluPaasUtil;
import com.moma.otasset.util.StringUtil;
import com.moma.otasset.web.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("notice")
public class NoticeController {

    @Resource
    AiluPaasUtil ailuPaasUtil;

    @Autowired
    ObjectRedisService objectRedisService;


    /**
     * 注册 发送验证码,需要判断图片验证码是否正确
     *
     * @return com.ailu.lightningrod.web.WebResult
     * @date 2018-06-15
     * @author hgl
     * @see
     * @since
     */
    @PostMapping("sendCode")
    public WebResult sendIdentifyingCodeCheckImg(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        //获取用户手机号
        Integer type = jsonObject.getInteger("type");
        String uid = jsonObject.getString("uid");
        if (type == null) {
            return WebResult.failResult("参数异常!");
        }
        if (uid == null) {
            return WebResult.failResult("参数异常!");
        }
        String phone = "";
        switch (type) {
            case 1:
                phone = "15652571183";//13979122221
                break;
            case 2:
                phone = "15517993232";//18621670791
                break;
        }
        if (StringUtil.isEmpty(phone)) {
            return WebResult.failResult("用户手机号不存在");
        }
        String areaCode = "86";
        //修复台湾区号被截取的问题
        if ("886".equals(areaCode) && !"886".equals(phone.substring(0, 3))) {
            StringBuilder s = new StringBuilder(phone);
            phone = s.replace(0, 1, "886").toString();
        }
        try {
            //发送短信验证码
            return sendPhoneCode(phone, type.compareTo(1) == 0 ? "充值提现操作" : "转账操作", uid);
        } catch (Exception e) {
            log.error("发送验证码 异常：", e);
            return WebResult.failException();
        }
    }

    /**
     * 发送验证码
     */
    public WebResult sendPhoneCode(String phone, String type, String uid) {
        try {
           /* JSONObject jsonObject = new JSONObject();
            if (StringUtil.isEmpty(areaCode) || "86".equals(areaCode)) {
                //普通短信验证
                jsonObject = ailuPaasUtil.sendPhoneCode(phone);
            } else {
                //根据模板id发送短信
                //jsonObject = ailuPaasUtil.sendPhoneCode("+" + phone, areaCode, 4);
            }*/
            String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
            String content = "【比特助手】您的" + type + "验证码是" + code + "。如非本人操作，请忽略本短信";
            JSONObject jsonObject = ailuPaasUtil.smsSendMsg(phone, content, "2398914");
            Integer resCode = jsonObject.getInteger("code");
            if (Objects.equals(0, resCode)) {
                objectRedisService.set(phone + uid, code);
                return WebResult.okResult(1007);
            }
            return WebResult.failResult(jsonObject.getString("msg"));
        } catch (Exception e) {
            e.printStackTrace();
            return WebResult.failException("验证码发送异常，请重试");
        }
    }

    /**
     * 检测验证码
     */
    public boolean checkPhoneCode(String phone, String uid, String code) {
        try {
            Object o = objectRedisService.get(phone + uid);
            if (o != null && code.equals(o.toString())) {
                return true;
            }
           /* String string = o.toString();

            JSONObject jsonObject = ailuPaasUtil.checkPhoneCode(phone, code);*/
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("验证验证码异常", e);
            return false;
        }
    }

}
