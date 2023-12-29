package com.helioplis.accounting.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final ExcelHelper excelHelper;
    private final OrderRepo orderRepo;

    @Async
    public void createFromExcel(InputStream is){
        List<Order> orders = excelHelper.excelToOrders(is);
        orderRepo.saveAll(orders);
    }
}
