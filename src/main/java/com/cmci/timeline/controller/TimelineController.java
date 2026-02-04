package com.cmci.timeline.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/timeline")
public class TimelineController {

    @RequestMapping("/view")
    public String timeline(Model model) {
        return "/timeline/timelineTest";
    }

}
