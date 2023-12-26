package com.helioplis.accounting.order;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.ast.Or;
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
            boolean inData = true;
            while (rows.hasNext() && inData) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                if (currentRow.getFirstCellNum() == -1){
                    break;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Order order = new Order();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            order.setOrderId((int) currentCell.getNumericCellValue());
                            break;

                        case 3:
                            LocalDateTime date = LocalDateTime.parse(currentCell.getStringCellValue(), formatter);
                            order.setCreatedAt(date);
                            break;
                        case 8:
                            order.setAmount(BigDecimal.valueOf(currentCell.getNumericCellValue()));
                            break;


                        default:
                            break;
                    }

                    cellIdx++;
                }
                orders.add(order);
            }


            workbook.close();

            return orders;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}