package vn.ngocanh.timetable.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ngocanh.timetable.domain.Class;
import vn.ngocanh.timetable.repository.ClassRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    // CRUD Operations
    public List<Class> getAllClass() {
        return this.classRepository.findAll();
    }

    public Class getClassById(long id) {
        return this.classRepository.findOneById(id);
    }

    public Class handleSaveClass(Class classEntity) {
        return this.classRepository.save(classEntity);
    }

    public void deleteClassById(long id) {
        this.classRepository.deleteById(id);
    }

    public boolean isClassNameExist(String name) {
        return this.classRepository.existsByName(name);
    }

    // Excel Export
    public ByteArrayInputStream exportClassesToExcel() throws IOException {
        List<Class> classes = getAllClass();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Classes");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Tên phòng");
            headerRow.createCell(2).setCellValue("Sức chứa");

            // Data rows
            int rowNum = 1;
            for (Class classEntity : classes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(classEntity.getId());
                row.createCell(1).setCellValue(classEntity.getName());
                row.createCell(2).setCellValue(classEntity.getCapacity());
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
    public String importClassesFromExcel(MultipartFile file) {
        try {
            List<Class> classes = parseExcelFile(file.getInputStream());

            int createdCount = 0;
            int skippedCount = 0;

            for (Class classEntity : classes) {
                if (!isClassNameExist(classEntity.getName())) {
                    handleSaveClass(classEntity);
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

    private List<Class> parseExcelFile(InputStream inputStream) throws IOException {
        List<Class> classes = new ArrayList<>();

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
                    long capacity = row.getCell(2) != null ? (long) row.getCell(2).getNumericCellValue() : 0;

                    if (!name.isEmpty() && capacity > 0) {
                        Class classEntity = new Class();
                        classEntity.setName(name);
                        classEntity.setCapacity(capacity);
                        classes.add(classEntity);
                    }
                } catch (Exception e) {
                    // Skip invalid rows
                    continue;
                }
            }
        }

        return classes;
    }

    // Excel Template
    public ByteArrayInputStream createExcelTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Classes Template");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID (Bỏ trống khi tạo mới)");
            headerRow.createCell(1).setCellValue("Tên phòng");
            headerRow.createCell(2).setCellValue("Sức chứa");

            // Sample data
            Row sampleRow = sheet.createRow(1);
            sampleRow.createCell(0).setCellValue("");
            sampleRow.createCell(1).setCellValue("Phòng A101");
            sampleRow.createCell(2).setCellValue(50);

            Row sampleRow2 = sheet.createRow(2);
            sampleRow2.createCell(0).setCellValue("");
            sampleRow2.createCell(1).setCellValue("Phòng B202");
            sampleRow2.createCell(2).setCellValue(60);

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
