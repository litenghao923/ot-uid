package com.moma.otasset.util;

import com.ailu.paas.AiluPaasNotice;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Version 1.0
 * @Since JDK1.8
 * @Author HYK
 * @Company 河南艾鹿
 * @Date 2018/1/10 0010 15:18*/


@Slf4j
@Component
public class AiluPaasUtil {

	private static final String CODE_PRE = "CODE_";

	// 验证码失效时间
	private static final int CODE_OVERDUE_MINUTE = 10;

	@Value("${application.sms.appKey}")
	private String appKey;


	@Value("${application.sms.secret}")
	private String secret;


	@Value("${application.mail.admin-email}")
	private String adminEmail;


	public AiluPaasNotice getAiluPaasNotice() {
		return AiluPaasNotice.getAiluPaas(appKey, secret);
	}

	public AiluPaasNotice getAiluPaasNotice(String typeKey) {
		return AiluPaasNotice.getAiluPaas(appKey, secret);
	}

/**
	 * 发送手机验证码
	 *
	 * @param phone 手机号
	 * @return*/


	public JSONObject sendPhoneCode(String phone) throws Exception {
		return sendPhoneCode(phone,4);
	}

	public JSONObject sendPhoneCode(String phone,int codeLenth) throws Exception {
		return getAiluPaasNotice().smsSendValidCode(phone,codeLenth);
	}

	public JSONObject sendPhoneCode(String phone,String areaCode,int codeLenth) throws Exception {
		return getAiluPaasNotice().smsSendValidCode(phone,areaCode,codeLenth);
	}

	/**
	 * 根据模板Id发送短信
	 * @param phone
	 * @param content
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	public JSONObject smsSendMsg(String phone,String content,String templateId) throws Exception {
		return getAiluPaasNotice().smsSendMsg(phone,content,templateId);
	}

	/**
	 * 根据模板Id发送语音
	 * @param phone
	 * @param templateId
	 * @param templateParams
	 * @param contents
	 * @return
	 * @throws Exception
	 */
	public JSONObject smsSendBigWarningMsg(String phone,String templateId, List<String> templateParams, List<String> contents) throws Exception {
		return getAiluPaasNotice().smsSendVoiceWarningMsg(phone, templateId, templateParams, contents);
	}



/**
	 * 检查手机验证码
	 *
	 * @param phone 手机号
	 * @param code  验证码
	 * @return
	 * @throws Exception*/


	public JSONObject checkPhoneCode(String phone, String code) throws Exception {
		return getAiluPaasNotice().smsCheckValidCode(phone, code);
	}

/**
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @return
	 * @description 向管理员发送预警
	 * @date 2018/1/11
	 * @author hgl
	 * @see
	 * @since*/


	public boolean sendEmailErrorMsg(String subject, String content) {
		boolean flag = false;
		subject = StringUtil.isEmpty(subject) ? "topcoin系统出现错误" : subject;
		JSONObject jsonObject = null;
		int zero = 0;
		if (StringUtil.isEmpty(adminEmail)) {
			log.error("获取管理员邮箱地址失败，无法发送预警邮件！");
			return flag;
		}
		try {
			String[] adminEmails = adminEmail.split(",");
			flag = true;
			for (String email : adminEmails) {
				jsonObject = getAiluPaasNotice().mailSendWithSubject(adminEmail, subject, content);
				if (jsonObject == null || zero!=jsonObject.getInteger("code")) {
					flag = false;
					log.error("发送异常邮件失败！");
				}
			}
		} catch (Exception e) {
			log.error("系统异常，发送异常邮件出错：{}", e);
		}
		return flag;
	}

}
