package com.helioplis.accounting.order;

import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final ExcelHelper excelHelper;
    private final OrderRepo orderRepo;

    public String createFromExcel(InputStream is){
        List<Order> orders = excelHelper.excelToOrders(is);
        orderRepo.saveAll(orders);
        return "Saved";
    }
}