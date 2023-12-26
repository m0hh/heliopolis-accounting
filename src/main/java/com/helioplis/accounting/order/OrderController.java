package com.helioplis.accounting.order;

import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.expense.Expense;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/order/")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private  final ExcelHelper excelHelper;

    @PostMapping("add")
    public List<Order> addOrder(@RequestParam("file") MultipartFile file, Principal principal){
        InputStream inputStream;
        try {
            inputStream =  new BufferedInputStream(file.getInputStream());
        } catch (Exception e){
            throw new ApiRequestException(e.getMessage());
        }
        List<Order> orders = excelHelper.excelToOrders(inputStream);
        return orders;
    }
}
