package com.helioplis.accounting.order;

import lombok.AllArgsConstructor;
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
public class ExcelHelper {
    private final DataFormatter dataFormatter = new DataFormatter();
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "order_id", "created", "order_total"};
    static String SHEET = "Worksheet";

    public boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public List<Order> excelToOrders(InputStream is) {
        try {
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

                order.setOrderId((int) currentRow.getCell(0).getNumericCellValue());
                LocalDateTime date = LocalDateTime.parse(currentRow.getCell(3).getStringCellValue(), formatter);
                order.setCreatedAt(date);
                order.setAmount(BigDecimal.valueOf(currentRow.getCell(17).getNumericCellValue()));
                orders.add(order);

                }
            workbook.close();

            return orders;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}