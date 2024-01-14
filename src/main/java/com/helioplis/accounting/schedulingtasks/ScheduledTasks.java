package com.helioplis.accounting.schedulingtasks;

import com.helioplis.accounting.pay.PayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class ScheduledTasks {
    final private PayService payService;

    @Scheduled(cron = "0 0 0 1 1-12 ? ")
    private void schedulePay() {
        payService.createPays();
        log.info("Scheduled Pay Ran");
    }
}
