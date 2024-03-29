package com.helioplis.accounting.order;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.expense.ExpenseUpdateDTO;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.shift.Shift;
import com.helioplis.accounting.shift.ShiftRepo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/order/")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private  final OrderService orderService;
    private final ExcelHelper excelHelper;
    private final ShiftRepo shiftRepo;


    @PostMapping("add")
    public ResponseEntity<String> addOrder(@RequestParam(value = "file",required = false) MultipartFile file, @RequestParam(value = "shift_id",required = false) Integer shiftId, Principal principal){
        if (file == null) {
            throw new ApiRequestException("File is required in the request");
        }

        if (shiftId == null) {
            throw new ApiRequestException("Shift ID is required in the request");
        }
        if (! excelHelper.hasExcelFormat(file)){
            throw new ApiRequestException("The input file must be excel format");
        }
        InputStream inputStream;
        try {
            inputStream =  new BufferedInputStream(file.getInputStream());
        } catch (Exception e){
            throw new ApiRequestException(e.getMessage());
        }
        Shift shift = shiftRepo.findById(shiftId).orElseThrow(() -> new ApiRequestException("No shift is by that id"));
        if (shift.isClosed()){
            throw new ApiRequestException("This Shift is closed modify it first");
        }
        orderService.createFromExcel(inputStream, shift);
        return ResponseEntity.ok("Executed");
    }

    @GetMapping("list")
    public List<Order> listOrders(
            @RequestParam(name = "start_date",required = false) LocalDateTime start_date,
            @RequestParam(name = "end_date",required = false) LocalDateTime end_date,
            @RequestParam(name = "shift_id", required = false) Integer shiftId,
            @RequestParam(name = "page", required = false) Integer page



    ){
        if (page == null){
            throw new ApiRequestException("Specify the page");
        }
        return orderService.listOrdersfilter(start_date,end_date,shiftId, page);
    }

    @PutMapping("update")
    public Order updateOrder(@RequestBody OrderUpdateDTO dto, Principal principal){
        return   orderService.updateOrder(dto);
    }

    @GetMapping("retrieve/{orderId}")
    public Order retrieveOrder(@PathVariable Integer orderId, Principal principal){
        return  orderService.retrieveOrder(orderId);
    }
}
