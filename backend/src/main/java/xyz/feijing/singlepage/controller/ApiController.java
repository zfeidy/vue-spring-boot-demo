package xyz.feijing.singlepage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author feijing
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    public String hello(){
        return "hello world";
    }
}
