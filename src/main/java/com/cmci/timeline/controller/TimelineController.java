package com.cmci.timeline.controller;


import com.cmci.timeline.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/timeline")
public class TimelineController {

    @Autowired
    private TimelineService service;

    @RequestMapping("/view")
    public String timeline(Model model) {
        return "/timeline/timelineTest";
    }

}
