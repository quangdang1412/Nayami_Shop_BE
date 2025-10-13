package com.apinayami.demo.service.Impl;

import com.apinayami.demo.dto.request.ProductDTO;
import com.apinayami.demo.dto.response.DashBoardResponseDTO;
import com.apinayami.demo.dto.response.ProductBestSellingDTO;
import com.apinayami.demo.service.IBillService;
import com.apinayami.demo.service.IProductService;
import com.apinayami.demo.service.IReportService;
import com.apinayami.demo.util.Enum.EBillStatus;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final IBillService billService;
    private final IProductService productService;

    @Override
    public byte[] exportDashBoardOfMonth() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);


        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        // ================== Sheet 1: Tổng quan ==================
        Sheet sheet1 = workbook.createSheet("Tổng quan");
        Row header1 = sheet1.createRow(0);
        Cell cell0 = header1.createCell(0);
        Cell cell1 = header1.createCell(1);
        cell0.setCellValue("Chỉ tiêu");
        cell0.setCellStyle(headerStyle);
        cell1.setCellValue("Giá trị");
        cell1.setCellStyle(headerStyle);

        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate sameDayLastYear = now.minusYears(1);
        LocalDate firstDayOfMonthLastYear = sameDayLastYear.withDayOfMonth(1);

        DashBoardResponseDTO revenueCurrent = billService.getRevenueOrProfitByTime(firstDayOfMonth, now, EBillStatus.COMPLETED, 0);
        DashBoardResponseDTO profitCurrent = billService.getRevenueOrProfitByTime(firstDayOfMonth, now, EBillStatus.COMPLETED, 1);
        DashBoardResponseDTO revenueLastYear = billService.getRevenueOrProfitByTime(firstDayOfMonthLastYear, sameDayLastYear, EBillStatus.COMPLETED, 0);
        DashBoardResponseDTO profitLastYear = billService.getRevenueOrProfitByTime(firstDayOfMonthLastYear, sameDayLastYear, EBillStatus.COMPLETED, 1);

        BigDecimal revenueNew = revenueCurrent.getData().stream()
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profitNew = profitCurrent.getData().stream()
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal revenueOld = revenueCurrent.getData().stream()
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profitOld = profitCurrent.getData().stream()
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Tính % tăng trưởng so với kỳ trước

        //TODO Khuc nay co the tao bug  neu k check == 0
        BigDecimal percent;
        if (revenueOld.compareTo(BigDecimal.ZERO) == 0) {
            if (revenueNew.compareTo(BigDecimal.ZERO) == 0) {
                percent = BigDecimal.ZERO;
            } else {
                percent = BigDecimal.valueOf(100);
            }
        } else {
            percent = (revenueNew.subtract(revenueOld))
                    .divide(revenueOld, 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        List<ProductBestSellingDTO> listProductDTOBestSelling = billService.getProductBestSellingByTime(firstDayOfMonth, now, EBillStatus.COMPLETED);
        List<ProductDTO> listProductDTOOutOfStock = productService.getProductOutOfStock();
        StringBuilder nameProductBestSelling = new StringBuilder();
        StringBuilder nameProductOutOfStock = new StringBuilder();
        for (ProductBestSellingDTO dto : listProductDTOBestSelling) {
            nameProductBestSelling.append(dto.getName()).append(",");
        }
        for (ProductDTO dto : listProductDTOOutOfStock) {
            nameProductOutOfStock.append(dto.getName()).append(",");
        }
        Object[][] data1 = {
                {"Doanh thu", revenueNew + " VND"},
                {"Lợi nhuận", profitNew + " VND"},
                {"So với kỳ trước", percent + "%"},
                {"Top SP bán chạy", nameProductBestSelling},
                {"SP sắp hết hàng", nameProductOutOfStock}
        };

        int rowIdx = 1;
        for (Object[] rowData : data1) {
            Row row = sheet1.createRow(rowIdx++);
            row.createCell(0).setCellValue(rowData[0].toString());
            row.createCell(1).setCellValue(rowData[1].toString());
        }

        // ================== Sheet 2: Doanh thu & Lợi nhuận ==================
        Sheet sheet2 = workbook.createSheet("Doanh thu & Lợi nhuận");
        Row header2 = sheet2.createRow(0);
        Cell cell2 = header2.createCell(0);
        Cell cell3 = header2.createCell(1);
        Cell cell4 = header2.createCell(2);
        cell2.setCellValue("Ngày");
        cell2.setCellStyle(headerStyle);
        cell3.setCellValue("Doanh thu");
        cell3.setCellStyle(headerStyle);
        cell4.setCellValue("Lợi nhuận");
        cell4.setCellStyle(headerStyle);


        List<Object[]> data2 = new ArrayList<>();
        for (int i = 0; i < revenueCurrent.getTime().size(); i++) {
            String time = revenueCurrent.getTime().get(i);
            String revenue = revenueCurrent.getData().get(i);
            String profit = profitCurrent.getData().get(i);
            data2.add(new Object[]{time, Double.parseDouble(revenue), Double.parseDouble(profit)});
        }
        rowIdx = 1;
        for (Object[] rowData : data2) {
            Row row = sheet2.createRow(rowIdx++);
            row.createCell(0).setCellValue(rowData[0].toString());
            row.createCell(1).setCellValue((Double) rowData[1]);
            row.createCell(2).setCellValue((Double) rowData[2]);
        }

        // ================== Sheet 3: SP bán chạy ==================
        Sheet sheet3 = workbook.createSheet("SP bán chạy");
        Row header3 = sheet3.createRow(0);
        Cell cell5 = header3.createCell(0);
        Cell cell6 = header3.createCell(1);
        Cell cell7 = header3.createCell(2);
        Cell cell8 = header3.createCell(3);
        Cell cell9 = header3.createCell(4);

        cell5.setCellValue("Mã SP");
        cell5.setCellStyle(headerStyle);
        cell6.setCellValue("Tên SP");
        cell6.setCellStyle(headerStyle);
        cell7.setCellValue("Số lượng tồn kho");
        cell7.setCellStyle(headerStyle);
        cell8.setCellValue("Số lượng bán");
        cell8.setCellStyle(headerStyle);
        cell9.setCellValue("Doanh thu");
        cell9.setCellStyle(headerStyle);

        List<Object[]> data3 = new ArrayList<>();
        for (int i = 0; i < listProductDTOBestSelling.size(); i++) {
            ProductBestSellingDTO dto = listProductDTOBestSelling.get(i);
            data3.add(new Object[]{dto.getId(), dto.getName(), dto.getQuantity(), dto.getQuantitySold(), dto.getUnitPrice() * dto.getQuantitySold()});
        }
        rowIdx = 1;
        for (Object[] rowData : data3) {
            Row row = sheet3.createRow(rowIdx++);
            row.createCell(0).setCellValue(rowData[0].toString());
            row.createCell(1).setCellValue(rowData[1].toString());
            row.createCell(2).setCellValue((Integer) rowData[2]);
            row.createCell(3).setCellValue((Integer) rowData[3]);
            row.createCell(4).setCellValue((Double) rowData[4]);
        }

        // ================== Sheet 4: SP sắp hết hàng ==================
        Sheet sheet4 = workbook.createSheet("SP sắp hết hàng");
        Row header4 = sheet4.createRow(0);
        Cell cell10 = header4.createCell(0);
        Cell cell11 = header4.createCell(1);
        Cell cell12 = header4.createCell(2);
        Cell cell13 = header4.createCell(3);
        Cell cell14 = header4.createCell(4);

        cell10.setCellValue("Mã SP");
        cell10.setCellStyle(headerStyle);

        cell11.setCellValue("Tên SP");
        cell11.setCellStyle(headerStyle);

        cell12.setCellValue("Tồn kho");
        cell12.setCellStyle(headerStyle);

        cell13.setCellValue("Giá nhập");
        cell13.setCellStyle(headerStyle);

        cell14.setCellValue("Giá bán");
        cell14.setCellStyle(headerStyle);

        List<Object[]> data4 = new ArrayList<>();
        for (int i = 0; i < listProductDTOOutOfStock.size(); i++) {
            ProductDTO dto = listProductDTOOutOfStock.get(i);
            data4.add(new Object[]{dto.getId(), dto.getName(), dto.getQuantity(), dto.getOriginalPrice(), dto.getUnitPrice()});
        }
        rowIdx = 1;
        for (Object[] rowData : data4) {
            Row row = sheet4.createRow(rowIdx++);
            row.createCell(0).setCellValue(rowData[0].toString());
            row.createCell(1).setCellValue(rowData[1].toString());
            row.createCell(2).setCellValue((Integer) rowData[2]);
            row.createCell(3).setCellValue((Double) rowData[3]);
            row.createCell(4).setCellValue((Double) rowData[4]);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
}
