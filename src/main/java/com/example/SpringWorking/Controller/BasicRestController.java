package com.example.SpringWorking.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringWorking.services.PostService;

@RestController
@RequestMapping("/api/v1")
public class BasicRestController {
    @Autowired
    private PostService postserv;
    Logger logger= LoggerFactory.getLogger(BasicRestController.class);

    @GetMapping("/")
    public  String home() {
        logger.debug("sample error using logger slf4j.");
        return "sample debug";
    }

}
