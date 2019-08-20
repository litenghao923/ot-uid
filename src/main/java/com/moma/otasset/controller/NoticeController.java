package com.moma.otasset.controller;

import com.alibaba.fastjson.JSONObject;
import com.moma.otasset.util.AiluPaasUtil;
import com.moma.otasset.util.StringUtil;
import com.moma.otasset.web.WebResult;
import lombok.extern.slf4j.Slf4j;
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
        String phone = jsonObject.getString("phone");
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
            Boolean imgFlag = false;
            //验证验证码
            if (imgFlag) {
                //发送短信验证码
                return sendPhoneCode(phone, areaCode);
            } else {
                return WebResult.failResult("图片验证码错误，短信发送失败");
            }
        } catch (Exception e) {
            log.error("发送验证码 异常：", e);
            return WebResult.failException();
        }
    }

    /**
     * 发送验证码
     */
    public WebResult sendPhoneCode(String phone, String areaCode) {
        try {
            JSONObject jsonObject = new JSONObject();
            if (StringUtil.isEmpty(areaCode) || "86".equals(areaCode)) {
                //普通短信验证
                jsonObject = ailuPaasUtil.sendPhoneCode(phone);
            } else {
                //根据模板id发送短信
                jsonObject = ailuPaasUtil.sendPhoneCode("+" + phone, areaCode, 4);
            }
            Integer code = jsonObject.getInteger("code");
            if (Objects.equals(0, code)) {
                return WebResult.okResult(1007);
            }
            return WebResult.failResult(jsonObject.getString("msg"));
        } catch (Exception e) {
            e.printStackTrace();
            return WebResult.failException("验证码发送异常，请重试");
        }
    }

}
