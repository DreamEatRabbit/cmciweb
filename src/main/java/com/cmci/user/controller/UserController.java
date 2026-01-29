package com.cmci.user.controller;

import com.cmci.user.model.UserDto;
import com.cmci.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public UserDto getUserInfo(String pUserId) {
        return service.getUserInfo(pUserId);
    }

    @RequestMapping("/addUserInfo")
    @ResponseBody
    public UserDto addUserInfo(UserDto pDto) {
        UserDto rDto = new UserDto();
        rDto.setAddCnt(service.addUserInfo(pDto));
        return rDto;
    }
}
