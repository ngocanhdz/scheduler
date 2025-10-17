package vn.ngocanh.timetable.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ngocanh.timetable.domain.Period;
import vn.ngocanh.timetable.domain.Day;
import vn.ngocanh.timetable.repository.PeriodRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PeriodService {

    private final PeriodRepository periodRepository;
    private final DayService dayService;

    public PeriodService(PeriodRepository periodRepository, DayService dayService) {
        this.periodRepository = periodRepository;
        this.dayService = dayService;
    }

    // CRUD Operations
    public List<Period> getAllPeriod() {
        return this.periodRepository.findAll();
    }

    public Period getPeriodById(long id) {
        return this.periodRepository.findOneById(id);
    }

    public List<Period> getPeriodsByDay(Day day) {
        return this.periodRepository.findByDay(day);
    }

    public Period handleSavePeriod(Period period) {
        return this.periodRepository.save(period);
    }

    public void deletePeriodById(long id) {
        this.periodRepository.deleteById(id);
    }

    public boolean isPeriodNameExist(String name, Day day) {
        return this.periodRepository.existsByNameAndDay(name, day);
    }

    // Excel Export
    public ByteArrayInputStream exportPeriodsToExcel() throws IOException {
        List<Period> periods = getAllPeriod();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Periods");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Số tiết");
            headerRow.createCell(2).setCellValue("Tên tiết");
            headerRow.createCell(3).setCellValue("Giờ bắt đầu");
            headerRow.createCell(4).setCellValue("Giờ kết thúc");
            headerRow.createCell(5).setCellValue("Ngày");

            // Data rows
            int rowNum = 1;
            for (Period period : periods) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(period.getId());
                row.createCell(1).setCellValue(period.getNumber());
                row.createCell(2).setCellValue(period.getName());
                row.createCell(3).setCellValue(period.getStart());
                row.createCell(4).setCellValue(period.getEnd());
                row.createCell(5).setCellValue(period.getDay() != null ? period.getDay().getName() : "");
            }

            // Auto-size columns
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    // Excel Import
    public String importPeriodsFromExcel(MultipartFile file) {
        try {
            List<Period> periods = parseExcelFile(file.getInputStream());

            int createdCount = 0;
            int skippedCount = 0;

            for (Period period : periods) {
                if (!isPeriodNameExist(period.getName(), period.getDay())) {
                    handleSavePeriod(period);
                    createdCount++;
                } else {
                    skippedCount++;
                }
            }

            return String.format("Import thành công! Đã tạo: %d, Bỏ qua (trùng tên): %d",
                    createdCount, skippedCount);

        } catch (Exception e) {
            return "Lỗi khi import: " + e.getMessage();
        }
    }

    private List<Period> parseExcelFile(InputStream inputStream) throws IOException {
        List<Period> periods = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                try {
                    int number = row.getCell(1) != null ? (int) row.getCell(1).getNumericCellValue() : 0;
                    String name = row.getCell(2) != null ? row.getCell(2).getStringCellValue().trim() : "";
                    int start = row.getCell(3) != null ? (int) row.getCell(3).getNumericCellValue() : 0;
                    int end = row.getCell(4) != null ? (int) row.getCell(4).getNumericCellValue() : 0;
                    String dayName = row.getCell(5) != null ? row.getCell(5).getStringCellValue().trim() : "";

                    if (!name.isEmpty() && number > 0 && !dayName.isEmpty()) {
                        Day day = dayService.getDayByName(dayName);
                        if (day != null) {
                            Period period = new Period();
                            period.setNumber(number);
                            period.setName(name);
                            period.setStart(start);
                            period.setEnd(end);
                            period.setDay(day);
                            periods.add(period);
                        }
                    }
                } catch (Exception e) {
                    // Skip invalid rows
                    continue;
                }
            }
        }

        return periods;
    }

    // Excel Template
    public ByteArrayInputStream createExcelTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Periods Template");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID (Bỏ trống khi tạo mới)");
            headerRow.createCell(1).setCellValue("Số tiết");
            headerRow.createCell(2).setCellValue("Tên tiết");
            headerRow.createCell(3).setCellValue("Giờ bắt đầu");
            headerRow.createCell(4).setCellValue("Giờ kết thúc");
            headerRow.createCell(5).setCellValue("Ngày");

            // Sample data
            Row sampleRow = sheet.createRow(1);
            sampleRow.createCell(0).setCellValue("");
            sampleRow.createCell(1).setCellValue(1);
            sampleRow.createCell(2).setCellValue("Tiết 1");
            sampleRow.createCell(3).setCellValue(700);
            sampleRow.createCell(4).setCellValue(750);
            sampleRow.createCell(5).setCellValue("Thứ 2");

            Row sampleRow2 = sheet.createRow(2);
            sampleRow2.createCell(0).setCellValue("");
            sampleRow2.createCell(1).setCellValue(2);
            sampleRow2.createCell(2).setCellValue("Tiết 2");
            sampleRow2.createCell(3).setCellValue(800);
            sampleRow2.createCell(4).setCellValue(850);
            sampleRow2.createCell(5).setCellValue("Thứ 2");

            // Auto-size columns
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }
}
