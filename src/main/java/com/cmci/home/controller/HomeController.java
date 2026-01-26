package com.cmci.home.controller;

import com.cmci.common.service.CommonService;
import com.cmci.common.util.CommonUtil;
import com.cmci.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService service;

    @Autowired
    private CommonUtil commonUtil;

    @RequestMapping("/")
    public String home(Model model) {

        model.addAttribute("greeting", "hello world");

        return "home";
    }

    @RequestMapping("/view")
    public ModelAndView view(String param) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("greeting", "hello world - View");
        mv.addObject("obj", service.getTestObject("abcd00000000000001"));
        mv.setViewName("/home/view");
        return mv;
    }

    @RequestMapping("/getPythonResult")
    public ResponseEntity<String> getPythonResult(String type) {
        if("SSIM".equals(type)) {
            //String imgByte = java.util.Base64.getEncoder().encodeToString(commonUtil.getPythonResultSsim().getBytes());
            //String imgStr = commonUtil.getPythonResultSsim();
            //byte[] imgByte = imgStr.getBytes();
            //byte[] imgByte = commonUtil.getPythonResultSsim();

            //HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.IMAGE_JPEG);
            // Optional: set content length
            //headers.setContentLength(imgByte.length);

            String encodedImgByte = java.util.Base64.getEncoder().encodeToString(commonUtil.getPythonResultSsim());
            //String imgByte = commonUtil.getPythonResultSsim();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"python_result_ssim.jpg\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(encodedImgByte);
            //return new ResponseEntity<>(imgByte, headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}