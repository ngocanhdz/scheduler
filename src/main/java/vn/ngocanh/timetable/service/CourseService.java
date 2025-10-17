package vn.ngocanh.timetable.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.ngocanh.timetable.domain.College;
import vn.ngocanh.timetable.domain.Course;
import vn.ngocanh.timetable.repository.CollegeRepository;
import vn.ngocanh.timetable.repository.CourseRepository;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CollegeRepository collegeRepository;

    public CourseService(CourseRepository courseRepository, CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
        this.courseRepository = courseRepository;
    }

    public Course handleSaveCourse(Course course) {
        return this.courseRepository.save(course);
    }

    public College getCollegeByName(String name) {
        return this.collegeRepository.findOneByName(name);
    }

    public List<Course> getAllCourse() {
        return this.courseRepository.findAll();
    }

    public void handleDeleteCourse(long id) {
        this.courseRepository.deleteById(id);
    }

    public Course getCourseById(long id) {
        return this.courseRepository.findOneById(id);
    }

    public List<Course> searchCourses(String code, String name, String collegeName, Integer credit,
            Integer lectureHour) {
        List<Course> courses = this.courseRepository.findAll();
        // Filter by code
        if (code != null && !code.isEmpty()) {
            courses = courses.stream()
                    .filter(course -> course.getCode().toLowerCase().contains(code.toLowerCase()))
                    .collect(Collectors.toList());
        }
        // Filter by name
        if (name != null && !name.isEmpty()) {
            courses = courses.stream()
                    .filter(course -> course.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filter by college
        if (collegeName != null && !collegeName.isEmpty()) {
            courses = courses.stream()
                    .filter(course -> course.getCollege().getName().equals(collegeName))
                    .collect(Collectors.toList());
        }

        // Filter by credit
        if (credit != null) {
            courses = courses.stream()
                    .filter(course -> course.getCredit() == credit)
                    .collect(Collectors.toList());
        }

        // Filter by lecture hour
        if (lectureHour != null) {
            courses = courses.stream()
                    .filter(course -> course.getLectureHour() == lectureHour)
                    .collect(Collectors.toList());
        }

        return courses;
    }

    @Transactional
    public Map<String, Object> getCoursesWithFiltersStaff(Long yearLevel, String college, String sortBy,
            String sortDir) {
        // Lấy data từ native query (Object[] format)
        List<Object[]> rawData = this.courseRepository.findCoursesWithOrderDetailCountByFilter(yearLevel, college,
                sortBy, sortDir);
        List<Map<String, Object>> processedCourses = new ArrayList<>();
        long totalOrderDetails = 0;

        for (Object[] row : rawData) {
            Map<String, Object> courseMap = new HashMap<>(); // Mapping data từ native query result
            courseMap.put("id", ((Number) row[0]).longValue());
            courseMap.put("code", row[1]);
            courseMap.put("name", row[2]);
            courseMap.put("description", row[3]);
            courseMap.put("credit", row[4]);
            courseMap.put("collegeName", row[5]);

            // Số OrderDetail theo filter yearLevel/college
            Long filteredOrderDetailCount = ((Number) row[6]).longValue();
            courseMap.put("orderDetailCount", filteredOrderDetailCount);

            // Tổng số OrderDetail của course (không filter)
            Long totalOrderDetailCount = ((Number) row[7]).longValue();
            courseMap.put("totalOrderDetailCount", totalOrderDetailCount);

            // Tính thống kê (dùng filtered count)
            totalOrderDetails += filteredOrderDetailCount;

            processedCourses.add(courseMap);
        }

        // Return Map chứa tất cả data cần thiết
        Map<String, Object> result = new HashMap<>();
        result.put("courses", processedCourses);
        result.put("totalCourses", processedCourses.size());
        result.put("totalOrderDetails", totalOrderDetails);
        result.put("coursesWithRegistration", processedCourses.size()); // = totalCourses

        return result;
    }

    // Overloaded method cho backward compatibility
    @Transactional
    public Map<String, Object> getCoursesWithFiltersStaff(Long yearLevel, String college) {
        return getCoursesWithFiltersStaff(yearLevel, college, "name", "asc");
    }

    // Helper methods for dropdown data
    public List<Long> getAllYearLevels() {
        // Return year levels 1-4
        return List.of(1L, 2L, 3L, 4L);
    }

    public List<String> getAllColleges() {
        // Get distinct college names from courses
        List<Course> allCourses = this.courseRepository.findAll();
        return allCourses.stream()
                .map(course -> course.getCollege().getName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    // Get courses by names (for timetable creation)
    public List<Course> getCoursesByNames(String[] courseNames) {
        if (courseNames == null || courseNames.length == 0) {
            return new ArrayList<>();
        }

        List<Course> allCourses = this.courseRepository.findAll();
        return allCourses.stream()
                .filter(course -> {
                    for (String name : courseNames) {
                        if (course.getName().equals(name)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    // Get courses by codes (for timetable creation) - more reliable than names
    public List<Course> getCoursesByCodes(String[] courseCodes) {
        if (courseCodes == null || courseCodes.length == 0) {
            return new ArrayList<>();
        }

        List<Course> allCourses = this.courseRepository.findAll();
        return allCourses.stream()
                .filter(course -> {
                    for (String code : courseCodes) {
                        if (course.getCode().equals(code)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    // Get courses with OrderDetail counts by codes (for timetable creation)
    public List<Map<String, Object>> getCoursesWithOrderDetailsByCodes(String[] courseCodes) {
        if (courseCodes == null || courseCodes.length == 0) {
            return new ArrayList<>();
        }

        // Lấy tất cả data từ native query
        List<Object[]> allRawData = this.courseRepository.findCoursesWithOrderDetailCountByFilter(null, null, "name",
                "asc");
        List<Map<String, Object>> result = new ArrayList<>();

        // Filter theo mã môn được chọn
        for (Object[] row : allRawData) {
            String courseCode = (String) row[1]; // code ở vị trí thứ 2

            // Kiểm tra xem mã môn có trong danh sách được chọn không
            boolean isSelected = false;
            for (String selectedCode : courseCodes) {
                if (courseCode.equals(selectedCode)) {
                    isSelected = true;
                    break;
                }
            }

            if (isSelected) {
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("id", ((Number) row[0]).longValue());
                courseMap.put("code", row[1]);
                courseMap.put("name", row[2]);
                courseMap.put("description", row[3]);
                courseMap.put("credit", row[4]);
                courseMap.put("collegeName", row[5]);
                courseMap.put("orderDetailCount", ((Number) row[6]).longValue()); // filtered count
                courseMap.put("totalOrderDetailCount", ((Number) row[7]).longValue()); // total count

                result.add(courseMap);
            }
        }

        return result;
    }

    /**
     * Lấy danh sách course IDs từ course codes
     */
    public List<Long> getCourseIdsByCodes(String[] courseCodes) {
        List<Long> courseIds = new ArrayList<>();
        for (String code : courseCodes) {
            Course course = courseRepository.findOneByCode(code);
            if (course != null) {
                courseIds.add(course.getId());
            }
        }
        return courseIds;
    }

    /**
     * Lấy danh sách tên môn học từ course IDs
     */
    public List<String> getCourseNamesByIds(List<Long> courseIds) {
        List<String> courseNames = new ArrayList<>();
        for (Long id : courseIds) {
            Course course = courseRepository.findOneById(id);
            if (course != null) {
                courseNames.add(course.getCode() + " - " + course.getName());
            }
        }
        return courseNames;
    }

    /**
     * Lấy course ID từ course code
     */
    public Long getCourseIdByCode(String courseCode) {
        Course course = courseRepository.findOneByCode(courseCode);
        return course != null ? course.getId() : null;
    }
}
