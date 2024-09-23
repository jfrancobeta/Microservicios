package com.formacionbdi.springboot.app.gateway.Controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    
    @GetMapping("/authorized")
    public Map<String,String> authorized(@RequestParam String code){
        return Collections.singletonMap("code", code);
    }
}
