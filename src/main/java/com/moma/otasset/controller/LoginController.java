package com.moma.otasset.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author Jerry昊昊
 * @email litenghao923@qq.com
 * @date 2019-08-20 10:25
 * @company 河南摩码网络科技有限公司
 */
@Controller
public class LoginController {

    @GetMapping("login")
    public String toLogin(HttpSession session){
        if (session.getAttribute("admin")!=null){
            return "index";
        }
        return "login";
    }

    @PostMapping("login")
    public String doLogin(String userName, String password, HttpSession session, Model model){
        if(userName.equals("litenghao")&&password.equals("123")){
            session.setAttribute("admin",userName);
            return "index";
        }else {
            model.addAttribute("msg","用户名或密码错误");
            return "login";
        }

    }


    @GetMapping("index")
    public String toIndex(HttpSession session){
        if (session.getAttribute("admin")!=null){
            return "index";
        }
        return "login";
    }

    @GetMapping("account")
    public String toAccount(HttpSession session){
        if (session.getAttribute("admin")!=null){
            return "account";
        }
        return "login";
    }

    @GetMapping("order")
    public String toOrder(HttpSession session){
        if (session.getAttribute("admin")!=null){
            return "order";
        }
        return "login";
    }

    @GetMapping("recharge")
    public String toRecharge(HttpSession session){
        if (session.getAttribute("admin")!=null){
            return "recharge";
        }
        return "login";
    }

    @GetMapping("user")
    public String toUser(HttpSession session){
        if (session.getAttribute("admin")!=null){
            return "user";
        }
        return "login";
    }
}
