package vn.ngocanh.timetable.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import vn.ngocanh.timetable.domain.Course;
import vn.ngocanh.timetable.domain.User;
import vn.ngocanh.timetable.repository.RoleRepository;
import vn.ngocanh.timetable.repository.UserRepository;
import vn.ngocanh.timetable.repository.CollegeRepository;
import vn.ngocanh.timetable.repository.CourseRepository;

@Service
public class UploadService {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;
    private final CollegeRepository collegeRepository;
    private final ServletContext servletContext;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;

    public UploadService(ServletContext servletContext, PasswordEncoder passwordEncoder,
            UserRepository userRepository, RoleRepository roleRepository, CollegeRepository collegeRepository,
            CourseRepository courseRepository) {
        this.servletContext = servletContext;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.collegeRepository = collegeRepository;
        this.courseRepository = courseRepository;
    }

    public String handleUploadFile(MultipartFile file, String folderPath) {
        if (file.isEmpty()) {
            return "";
        }
        String rootPath = this.servletContext.getRealPath("/resources/images");
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        try {
            byte[] bytes;
            bytes = file.getBytes();

            File dir = new File(rootPath + File.separator + folderPath);
            if (!dir.exists())
                dir.mkdirs();

            // Create the file on server
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);

            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return finalName;
    }

    public List<User> importUsersFromExcel(MultipartFile file) {
        List<User> users = new ArrayList<>();
        Set<String> existingEmails = new HashSet<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                User user = createUserFromRow(row);

                if (user != null) {
                    String email = user.getEmail();
                    // Check for duplicate emails in current import
                    if (existingEmails.contains(email)) {
                        continue;
                    } // Check for duplicate emails in database
                    if (this.userRepository.existsByEmail(email)) {
                        continue;
                    }
                    existingEmails.add(email);
                    users.add(user);
                }
            }

            return userRepository.saveAll(users);
        } catch (Exception e) {
            // Log the error but don't throw it
            e.printStackTrace();
            return users;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // Format numbers without decimal places if they're whole numbers
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.format("%.0f", numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    switch (cell.getCachedFormulaResultType()) {
                        case NUMERIC:
                            return String.valueOf(cell.getNumericCellValue());
                        case STRING:
                            return cell.getStringCellValue();
                        case BOOLEAN:
                            return String.valueOf(cell.getBooleanCellValue());
                        default:
                            return "";
                    }
                } catch (Exception e) {
                    return "";
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    // Helper method to validate and format user data
    private User createUserFromRow(Row row) {
        User user = new User();

        String email = getCellValueAsString(row.getCell(0)); // Email is in column B (index 1)
        if (email.isEmpty()) {
            return null; // Skip rows without email as it's required
        }
        user.setEmail(email.toLowerCase().trim());

        String password = getCellValueAsString(row.getCell(4)); // Password is in column C (index 2)
        if (!password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        user.setFullName(getCellValueAsString(row.getCell(3))); // Full Name in column D (index 3)
        user.setAddress(getCellValueAsString(row.getCell(2))); // Address in column E (index 4)

        // Format phone number - remove any non-digit characters
        String phone = getCellValueAsString(row.getCell(1)); // Phone in column F (index 5)
        if (!phone.isEmpty()) {
            phone = phone.replaceAll("[^0-9]", "");
            user.setPhoneNumber(phone);
            ;
        }

        String role = getCellValueAsString(row.getCell(5)); // Role in column G (index 6)
        user.setRole(this.roleRepository.findOneByName(role.toUpperCase().trim())); // Normalize role to uppercase
        String yearLevelStr = getCellValueAsString(row.getCell(6));
        if (!yearLevelStr.isEmpty()) {
            yearLevelStr = yearLevelStr.replaceAll("[^0-9]", "");
            try {
                user.setYearLevel(Long.parseLong(yearLevelStr));
            } catch (NumberFormatException e) {
                // Optionally log or handle invalid number format
            }
        }
        String collegeName = getCellValueAsString(row.getCell(7)); // College in column H (index 8)
        if (!collegeName.isEmpty()) {
            user.setCollege(this.collegeRepository.findOneByName(collegeName.toUpperCase().trim()));
        }
        return user;
    }

    public void exportUsersToExcel(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("OutputStream cannot be null");
        }

        List<User> users = this.userRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = { "ID", "Email", "Full Name", "Address", "Phone", "Role", "Year Level", "College" };
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Create data rows
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getEmail() != null ? user.getEmail() : "");
                row.createCell(2).setCellValue(user.getFullName() != null ? user.getFullName() : "");
                row.createCell(3).setCellValue(user.getAddress() != null ? user.getAddress() : "");
                row.createCell(4).setCellValue(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
                row.createCell(5).setCellValue(user.getRole() != null ? user.getRole().getName() : "");
                row.createCell(6).setCellValue(user.getYearLevel());
                row.createCell(7).setCellValue(user.getCollege() != null ? user.getCollege().getName() : "");

            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new IOException("Failed to export users to Excel: " + e.getMessage(), e);
        }
    }

    public List<Course> importCoursesFromExcel(MultipartFile file) {
        List<Course> courses = new ArrayList<>();
        Set<String> existingCodes = new HashSet<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Course course = createCourseFromRow(row);

                if (course != null) {
                    String code = course.getCode();
                    // Check for duplicate emails in current import
                    if (existingCodes.contains(code)) {
                        continue;
                    } // Check for duplicate emails in database
                    if (this.userRepository.existsByEmail(code)) {
                        continue;
                    }
                    existingCodes.add(code);
                    courses.add(course);
                }
            }

            return this.courseRepository.saveAll(courses);
        } catch (Exception e) {
            // Log the error but don't throw it
            e.printStackTrace();
            return courses;
        }
    }

    private Course createCourseFromRow(Row row) {
        Course course = new Course();

        String code = getCellValueAsString(row.getCell(6)); // Email is in column B (index 1)
        if (code.isEmpty()) {
            return null; // Skip rows without email as it's required
        }
        course.setCode(code.toUpperCase().trim());
        course.setName(getCellValueAsString(row.getCell(0)));
        course.setEnglishName(getCellValueAsString(row.getCell(1)));
        course.setLectureHour(Double.parseDouble(getCellValueAsString(row.getCell(2)))); // 4
        course.setPracticalHour(Long.parseLong(getCellValueAsString(row.getCell(3))));
        course.setCredit(Long.parseLong(getCellValueAsString(row.getCell(4))));
        course.setDescription(getCellValueAsString(row.getCell(5)));
        String collegeName = getCellValueAsString(row.getCell(7));
        if (!collegeName.isEmpty()) {
            course.setCollege(this.collegeRepository.findOneByName(collegeName.toUpperCase().trim()));
        }
        return course;
    }

    public void exportCoursesToExcel(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("OutputStream cannot be null");
        }

        List<Course> courses = this.courseRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = { "ID", "Name", "English Name", "Lecture Hour", "Practical Hour", "Credit",
                    "Description", "Code", "College" };
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Create data rows
            int rowNum = 1;
            for (Course course : courses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(course.getId());
                row.createCell(1).setCellValue(course.getName() != null ? course.getName() : "");
                row.createCell(2).setCellValue(course.getEnglishName() != null ? course.getEnglishName() : "");
                row.createCell(3).setCellValue(course.getLectureHour());
                row.createCell(4).setCellValue(course.getPracticalHour());
                row.createCell(5).setCellValue(course.getCredit());
                row.createCell(6).setCellValue(course.getDescription() != null ? course.getDescription() : "");
                row.createCell(7).setCellValue(course.getCode() != null ? course.getCode() : "");
                row.createCell(8).setCellValue(course.getCollege() != null ? course.getCollege().getName() : "");

            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new IOException("Failed to export users to Excel: " + e.getMessage(), e);
        }
    }

}
