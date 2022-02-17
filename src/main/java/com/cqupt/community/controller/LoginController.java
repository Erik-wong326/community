package com.cqupt.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/17 12:34
 */
@Controller
public class LoginController {
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }
}
