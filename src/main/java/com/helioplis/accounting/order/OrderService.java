package com.helioplis.accounting.order;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.expense.ExpenseUpdateDTO;
import com.helioplis.accounting.shift.Shift;
import com.helioplis.accounting.shift.ShiftRepo;
import com.helioplis.accounting.shift.ShiftService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final ExcelHelper excelHelper;
    private final OrderRepo orderRepo;
    private final ShiftRepo shiftRepo;
    private final OrderMapper mapper;

    @Async
    public void createFromExcel(InputStream is, Shift shift){
        List<Order> orders = excelHelper.excelToOrders(is, shift);
        orderRepo.saveAll(orders);
    }

    public List<Order> listOrdersfilter(LocalDateTime beforeDate, LocalDateTime afterDate, Integer shiftId, Integer page){
        return orderRepo.findFilter(beforeDate, afterDate, shiftId, PageRequest.of(page, 10));
    }

    public Order updateOrder(OrderUpdateDTO dto) {
        Order myOrder = orderRepo.findById(dto.getId()).orElseThrow(() -> new ApiRequestException("No Order with that ID"));

        if (dto.getShift() != null){
            Integer shiftId = dto.getShift().getId();
            Shift shift = shiftRepo.findById(shiftId).orElseThrow(()-> new ApiRequestException("No Shift by that ID"));
            if (shift.isClosed()){
                throw new ApiRequestException("This Shift is closed open it first and then modify");
            }

        }
        mapper.updateOrderFromDto(dto, myOrder);

        return orderRepo.save(myOrder);
    }

    public Order retrieveOrder(Integer orderId){
        return orderRepo.findById(orderId).orElseThrow(()-> new ApiRequestException("No Order with that Id"));
    }
}

