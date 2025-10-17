package vn.ngocanh.timetable.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ngocanh.timetable.domain.Day;
import vn.ngocanh.timetable.repository.DayRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DayService {

    private final DayRepository dayRepository;

    public DayService(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }

    // CRUD Operations
    public List<Day> getAllDay() {
        return this.dayRepository.findAll();
    }

    public Day getDayById(long id) {
        return this.dayRepository.findOneById(id);
    }

    public Day getDayByName(String name) {
        return this.dayRepository.findOneByName(name);
    }

    public Day handleSaveDay(Day day) {
        return this.dayRepository.save(day);
    }

    public void deleteDayById(long id) {
        this.dayRepository.deleteById(id);
    }

    public boolean isDayNameExist(String name) {
        return this.dayRepository.existsByName(name);
    }

    // Excel Export
    public ByteArrayInputStream exportDaysToExcel() throws IOException {
        List<Day> days = getAllDay();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Days");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Tên ngày");
            headerRow.createCell(2).setCellValue("Số thứ tự");

            // Data rows
            int rowNum = 1;
            for (Day day : days) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(day.getId());
                row.createCell(1).setCellValue(day.getName());
                row.createCell(2).setCellValue(day.getNumber());
            }

            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    // Excel Import
    public String importDaysFromExcel(MultipartFile file) {
        try {
            List<Day> days = parseExcelFile(file.getInputStream());

            int createdCount = 0;
            int skippedCount = 0;

            for (Day day : days) {
                if (!isDayNameExist(day.getName())) {
                    handleSaveDay(day);
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

    private List<Day> parseExcelFile(InputStream inputStream) throws IOException {
        List<Day> days = new ArrayList<>();

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
                    String name = row.getCell(1) != null ? row.getCell(1).getStringCellValue().trim() : "";
                    int number = row.getCell(2) != null ? (int) row.getCell(2).getNumericCellValue() : 0;

                    if (!name.isEmpty() && number > 0) {
                        Day day = new Day();
                        day.setName(name);
                        day.setNumber(number);
                        days.add(day);
                    }
                } catch (Exception e) {
                    // Skip invalid rows
                    continue;
                }
            }
        }

        return days;
    }

    // Excel Template
    public ByteArrayInputStream createExcelTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Days Template");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID (Bỏ trống khi tạo mới)");
            headerRow.createCell(1).setCellValue("Tên ngày");
            headerRow.createCell(2).setCellValue("Số thứ tự");

            // Sample data
            Row sampleRow = sheet.createRow(1);
            sampleRow.createCell(0).setCellValue("");
            sampleRow.createCell(1).setCellValue("Thứ 2");
            sampleRow.createCell(2).setCellValue(2);

            Row sampleRow2 = sheet.createRow(2);
            sampleRow2.createCell(0).setCellValue("");
            sampleRow2.createCell(1).setCellValue("Thứ 3");
            sampleRow2.createCell(2).setCellValue(3);

            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }
}
