package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class Hello {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }


}
