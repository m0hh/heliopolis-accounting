package com.helioplis.accounting.order;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.shift.Shift;
import com.helioplis.accounting.shift.ShiftRepo;
import com.helioplis.accounting.shift.ShiftService;
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
    private final ShiftRepo shiftRepo;

    @Async
    public void createFromExcel(InputStream is, Shift shift){
        List<Order> orders = excelHelper.excelToOrders(is, shift);
        orderRepo.saveAll(orders);
    }
}
