package com.moma.otasset.web;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * file:SendHttpRequest
 * <p>
 * 文件简要说明 发送rest 就请
 *
 * @author 2018年05月18日  韩龚连  创建初始版本
 * @version 2018年05月18日  V1.0  简要版本说明
 * @par 版权信息：
 * 2018 Copyright 河南艾鹿网络科技有限公司 All Rights Reserved.
 */
@Slf4j
public class SendHttpRequest {
	
	/** post 请求 */
	public static String sendPost(String url, JSONObject jsonObject){

		RestTemplate restTemplate = new RestTemplate();
		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<>(jsonObject, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		//如果请求成功
		if(Objects.equals(200, response.getStatusCodeValue())){
			return response.getBody();
		}else{
			return "-1";
		}
		
	}

	/** post 请求  发送字符串 */
	public static String sendPost(String url,String sendContent){

		RestTemplate restTemplate = new RestTemplate();
		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(sendContent, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		//如果请求成功
		if(Objects.equals(200, response.getStatusCodeValue())){
			return response.getBody();
		}else{
			return "-1";
		}

	}

	/** get 请求 */
	public static String sendGet(String url,JSONObject jsonObject){

		RestTemplate restTemplate = new RestTemplate();
		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JSONObject> requestEntity = new HttpEntity<>(jsonObject, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		//如果请求成功
		if(Objects.equals(200, response.getStatusCodeValue())){
			return response.getBody();
		}else{
			return "-1";
		}

	}
}
