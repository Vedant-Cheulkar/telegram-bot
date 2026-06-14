package com.vedant.telegrambot.service;

import com.vedant.telegrambot.entity.WorkEntryEntity;
import com.vedant.telegrambot.repository.WorkEntryRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;

@Service
public class ExcelService {

    private final WorkEntryRepository workEntryRepository;

    public ExcelService(
            WorkEntryRepository workEntryRepository
    ) {
        this.workEntryRepository = workEntryRepository;
    }

    public String generateExcel() throws Exception {

        XSSFWorkbook workbook =
                new XSSFWorkbook();

        XSSFSheet sheet =
                workbook.createSheet("Panchayat Works");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Sr No");
        header.createCell(1).setCellValue("Work Name");
        header.createCell(2).setCellValue("Amount");

        List<WorkEntryEntity> works =
                workEntryRepository.findAll();

        int rowNum = 1;

        for (WorkEntryEntity work : works) {

            Row row =
                    sheet.createRow(rowNum);

            row.createCell(0)
                    .setCellValue(rowNum);

            row.createCell(1)
                    .setCellValue(work.getWorkName());

            row.createCell(2)
                    .setCellValue(work.getAmount());

            rowNum++;
        }

        String fileName =
                "panchayat_report.xlsx";

        FileOutputStream out =
                new FileOutputStream(fileName);

        workbook.write(out);

        out.close();
        workbook.close();

        return fileName;
    }
}