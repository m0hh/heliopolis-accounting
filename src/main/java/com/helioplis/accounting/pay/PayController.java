package com.helioplis.accounting.pay;

import com.helioplis.accounting.order.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Get;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    PayService payService;
    @GetMapping("retrieve/{payId}")
    public Pay retrievePay(@PathVariable Integer PayId, Principal principal){
        return  payService.retrievePay(PayId);
    }

}
