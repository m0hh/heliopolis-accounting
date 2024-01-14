package com.helioplis.accounting.pay;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/pay/")
@AllArgsConstructor
@Slf4j
public class PayController {
    final private PayService payService;

    @GetMapping("create")
    public List<Pay> createPays(Principal principal){
        List<Pay>  pays= new ArrayList<>();
        return pays;

    }
}
