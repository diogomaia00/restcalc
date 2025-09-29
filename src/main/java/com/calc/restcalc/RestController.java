package com.calc.restcalc;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class RestController {

    @GetMapping("/add")
    public String additionOperation() {
        return "Hello world";
    }
}
