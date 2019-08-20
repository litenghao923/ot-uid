package com.moma.otasset.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jerry昊昊
 * @email litenghao923@qq.com
 * @date 2019-08-20 10:25
 * @company 河南摩码网络科技有限公司
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("login")
    public String toLogin(){
        return "login";
    }
}
