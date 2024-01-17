package com.helioplis.accounting.order;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.helioplis.accounting.exeption.ApiRequestException;
import com.helioplis.accounting.firebase.FirebaseMessagingService;
import com.helioplis.accounting.firebase.Note;
import com.helioplis.accounting.shift.Shift;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ExcelHelper {
    private final DataFormatter dataFormatter;
    private final FirebaseMessagingService firebaseMessagingService;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "order_id", "created", "order_total"};
    static String SHEET = "Worksheet";

    public boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public List<Order> excelToOrders(InputStream is, Shift shift) {
        try {
            log.info("into excel");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Order> orders = new ArrayList<Order>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                if (currentRow.getFirstCellNum() == -1){
                    break;
                }


                Order order = new Order();

                Cell orderIdCell = currentRow.getCell(0);
                if (orderIdCell != null && orderIdCell.getCellType() != CellType.BLANK) {
                    order.setOrderId((int) orderIdCell.getNumericCellValue());
                }else {
                    try {
                        firebaseMessagingService.sendNotificationToTopic(new Note("Order Creation", "order cell row " + currentRow.getRowNum() + " is empty"),"order_creation");
                    }catch (FirebaseMessagingException e) {
                        log.error("Firebase Error", e);
                    }
                    log.error("Error at creating orders: order cell row " + currentRow.getRowNum() + " is empty");
                }
                Cell dateCell = currentRow.getCell(3);
                if (dateCell != null && dateCell.getCellType() != CellType.BLANK) {
                    LocalDateTime date = LocalDateTime.parse(dateCell.getStringCellValue(), formatter);
                    if (shift.getCreatedAt().isAfter(date)){
                        continue;
                    }
                    order.setCreatedAt(date);
                }else {
                    try {
                        firebaseMessagingService.sendNotificationToTopic(new Note("Order Creation", "date cell row " + currentRow.getRowNum() + " is empty"),"order_creation");
                    }catch (FirebaseMessagingException e) {
                        log.error("Firebase Error", e);
                    }
                    log.error("Error at creating orders: date cell row " + currentRow.getRowNum() + " is empty");
                }

                Cell amountCell = currentRow.getCell(17);
                if (amountCell != null && amountCell.getCellType() != CellType.BLANK) {
                    order.setAmount(BigDecimal.valueOf(amountCell.getNumericCellValue()));
                }else {
                    try {
                        firebaseMessagingService.sendNotificationToTopic(new Note("Order Creation", "amount cell row " + currentRow.getRowNum() + " is empty"),"order_creation");
                    }catch (FirebaseMessagingException e) {
                        log.error("Firebase Error", e);
                    }
                    log.error("Error at creating orders: amount cell row " + currentRow.getRowNum() + " is empty");

                }
                order.setShift(shift);
                orders.add(order);

            }
            workbook.close();

            return orders;
        } catch (IOException e) {
            try {
                firebaseMessagingService.sendNotificationToTopic(new Note("Order Creation", "Failed To Parse the Excel file"),"order_creation");
            }catch (FirebaseMessagingException ee) {
                log.error("Firebase Error", ee);
            }
            log.error("Error at creating orders: fail to parse Excel file", e);
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}