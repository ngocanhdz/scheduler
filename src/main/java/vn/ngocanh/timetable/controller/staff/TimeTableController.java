package vn.ngocanh.timetable.controller.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.ngocanh.timetable.domain.SemesterConfig;
import vn.ngocanh.timetable.domain.Timetable;
import vn.ngocanh.timetable.genetic.service.GeneticAlgorithmResult;
import vn.ngocanh.timetable.genetic.service.GeneticAlgorithmService;
import vn.ngocanh.timetable.service.CourseService;
import vn.ngocanh.timetable.service.SemesterConfigService;
import vn.ngocanh.timetable.service.TimetableService;

@Controller
public class TimeTableController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private SemesterConfigService semesterConfigService;
    @Autowired
    private TimetableService timetableService;
    @Autowired
    private GeneticAlgorithmService geneticAlgorithmService;

    @GetMapping("/staff/timetable/course")
    public String getCoursePage(Model model,
            @RequestParam(required = false) Long yearLevel,
            @RequestParam(required = false) String college,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir) { // Lấy Map data - tạm thời dùng
                                                                                    // method hiện tại
        Map<String, Object> courseData = courseService.getCoursesWithFiltersStaff(yearLevel, college, sortBy, sortDir);

        // Filter để chỉ lấy các môn chưa có lịch trong học kỳ hiện tại
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> allCourses = (List<Map<String, Object>>) courseData.get("courses");
        List<Map<String, Object>> coursesWithoutTimetable = allCourses.stream()
                .filter(course -> !timetableService.hasTimetableInCurrentSemester((Long) course.get("id")))
                .collect(java.util.stream.Collectors.toList());

        // Tính lại thống kê
        long totalOrderDetailsFiltered = coursesWithoutTimetable.stream()
                .mapToLong(course -> (Long) course.get("orderDetailCount"))
                .sum();
        // Add vào model
        model.addAttribute("courses", coursesWithoutTimetable); // List<Map> - chỉ môn chưa có lịch
        model.addAttribute("allCourses", this.courseService.getAllCourse().size());
        model.addAttribute("totalCourses", coursesWithoutTimetable.size());
        model.addAttribute("totalOrderDetails", totalOrderDetailsFiltered);

        // Thông tin học kỳ hiện tại
        SemesterConfig currentSemester = timetableService.getCurrentSemester();
        model.addAttribute("currentSemester", currentSemester != null ? currentSemester.getName() : "Chưa có học kỳ");

        // Thống kê lịch học
        if (currentSemester != null) {
            long totalTimetables = timetableService.countTimetablesInSemester(currentSemester.getId());
            model.addAttribute("totalTimetables", totalTimetables);

            // Số môn chưa có lịch
            long coursesWithoutTimetableCount = coursesWithoutTimetable.size();
            model.addAttribute("coursesWithoutTimetable", coursesWithoutTimetableCount);
        } else {
            model.addAttribute("totalTimetables", 0);
            model.addAttribute("coursesWithoutTimetable", this.courseService.getAllCourse().size());
        }

        // Add dropdown data
        model.addAttribute("yearLevels", courseService.getAllYearLevels());
        model.addAttribute("colleges", courseService.getAllColleges());

        // Giữ filter values
        model.addAttribute("selectedYearLevel", yearLevel);
        model.addAttribute("selectedCollege", college);

        // Add sorting info
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "staff/course/show";
    }

    @GetMapping("/staff/timetable/create")
    public String createTimetablePage(Model model,
            @RequestParam(value = "courseCodes", required = false) String[] courseCodes) {

        if (courseCodes == null || courseCodes.length == 0) {
            // Nếu không có môn nào được chọn, redirect về trang danh sách
            return "redirect:/staff/timetable/course";
        }

        // Lấy thông tin các môn học với số OrderDetail theo mã môn
        List<Map<String, Object>> selectedCoursesWithOrderDetails = courseService
                .getCoursesWithOrderDetailsByCodes(courseCodes);

        // Tính tổng số sinh viên đăng ký
        long totalOrderDetails = selectedCoursesWithOrderDetails.stream()
                .mapToLong(course -> (Long) course.get("totalOrderDetailCount"))
                .sum();

        // Thêm thông tin vào model
        model.addAttribute("selectedCourses", selectedCoursesWithOrderDetails);
        model.addAttribute("courseCount", selectedCoursesWithOrderDetails.size());
        model.addAttribute("totalOrderDetails", totalOrderDetails);
        model.addAttribute("currentSemester", this.semesterConfigService.getSemesterConfigByState(true).getName());

        // Tạo danh sách mã môn để hiển thị
        String courseCodesStr = String.join(", ", courseCodes);
        model.addAttribute("courseCodesStr", courseCodesStr);

        return "staff/timetable/create";
    }

    @PostMapping("/staff/timetable/generate")
    @ResponseBody
    public Map<String, Object> generateTimetable(@RequestParam("courseCodes") String[] courseCodes) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("=== GENERATE TIMETABLE REQUEST ===");
            System.out.println("Course codes received: " + java.util.Arrays.toString(courseCodes));

            // Check if services are injected properly
            System.out.println("CourseService: " + (courseService != null ? "OK" : "NULL"));
            System.out.println("TimetableService: " + (timetableService != null ? "OK" : "NULL"));
            System.out.println("GeneticAlgorithmService: " + (geneticAlgorithmService != null ? "OK" : "NULL")); // Validate
                                                                                                                 // input
            if (courseCodes == null || courseCodes.length == 0) {
                System.out.println("ERROR: No course codes provided");
                response.put("success", false);
                response.put("message", "Không có môn học nào được chọn");
                return response;
            } // Kiểm tra học kỳ hiện tại
            System.out.println("Getting current semester...");
            SemesterConfig currentSemester = null;
            try {
                currentSemester = timetableService.getCurrentSemester();
            } catch (Exception e) {
                System.err.println("ERROR getting current semester: " + e.getMessage());
                e.printStackTrace();
                response.put("success", false);
                response.put("message", "Lỗi khi lấy thông tin học kỳ hiện tại: " + e.getMessage());
                return response;
            }

            if (currentSemester == null) {
                System.out.println("ERROR: Current semester is null");
                response.put("success", false);
                response.put("message", "Không tìm thấy học kỳ hiện tại");
                return response;
            }
            System.out.println("Current semester: " + currentSemester.getName()); // Lấy danh sách course IDs từ course
                                                                                  // codes
            System.out.println("Getting course IDs...");
            List<Long> courseIds = null;
            try {
                courseIds = courseService.getCourseIdsByCodes(courseCodes);
            } catch (Exception e) {
                System.err.println("ERROR getting course IDs: " + e.getMessage());
                e.printStackTrace();
                response.put("success", false);
                response.put("message", "Lỗi khi lấy thông tin môn học: " + e.getMessage());
                return response;
            }

            if (courseIds == null || courseIds.isEmpty()) {
                System.out.println("ERROR: No valid course IDs found");
                response.put("success", false);
                response.put("message", "Không tìm thấy môn học hợp lệ");
                return response;
            }
            System.out.println("Course IDs: " + courseIds);

            // Kiểm tra các môn đã có lịch
            System.out.println("Checking existing timetables...");
            List<Long> coursesWithTimetable = courseIds.stream()
                    .filter(courseId -> timetableService.hasTimetableInCurrentSemester(courseId))
                    .collect(Collectors.toList());

            if (!coursesWithTimetable.isEmpty()) {
                List<String> courseNames = courseService.getCourseNamesByIds(coursesWithTimetable);
                response.put("success", false);
                response.put("message", "Các môn sau đã có lịch trong học kỳ này: " + String.join(", ", courseNames));
                return response;
            } // Chạy thuật toán genetic để tạo lịch
            System.out.println("Starting Genetic Algorithm...");
            GeneticAlgorithmResult result = null;
            try {
                result = geneticAlgorithmService.generateTimetable(courseIds, currentSemester.getId());
            } catch (Exception e) {
                System.err.println("ERROR in Genetic Algorithm: " + e.getMessage());
                e.printStackTrace();
                response.put("success", false);
                response.put("message", "Lỗi trong quá trình tạo lịch học: " + e.getMessage());
                response.put("error_type", e.getClass().getSimpleName());
                response.put("error_details",
                        e.getCause() != null ? e.getCause().getMessage() : "No additional details");
                return response;
            }

            System.out.println("GA completed with success: " + result.isSuccessful());
            if (result.isSuccessful()) {
                // Lưu kết quả vào database
                List<Timetable> savedTimetables = null;
                try {
                    savedTimetables = timetableService.saveTimetableFromGAResult(result, currentSemester);
                } catch (Exception e) {
                    System.err.println("ERROR saving timetable: " + e.getMessage());
                    e.printStackTrace();
                    response.put("success", false);
                    response.put("message", "Lỗi khi lưu lịch học: " + e.getMessage());
                    return response;
                }

                response.put("success", true);
                response.put("message", "Tạo lịch thành công cho " + savedTimetables.size() + " lớp học");
                response.put("details", Map.of(
                        "fitness", result.getBestFitness(),
                        "generations", result.getGenerationsExecuted(),
                        "executionTime", result.getExecutionTimeMs(),
                        "conflicts", result.getConflicts(),
                        "violations", result.getViolations(),
                        "completeness", result.getCompleteness(),
                        "classes", savedTimetables.size()));
            } else {
                response.put("success", false);
                response.put("message",
                        "Không thể tạo lịch học hợp lệ. Vui lòng thử lại hoặc điều chỉnh dữ liệu đầu vào.");
                response.put("details", Map.of(
                        "fitness", result.getBestFitness(),
                        "generations", result.getGenerationsExecuted(),
                        "conflicts", result.getConflicts(),
                        "violations", result.getViolations()));
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            response.put("error_type", e.getClass().getSimpleName());
            response.put("error_details", e.getCause() != null ? e.getCause().getMessage() : "No additional details");

            // Log chi tiết cho debugging
            System.err.println("=== ERROR IN GENERATE TIMETABLE ===");
            System.err.println("Error type: " + e.getClass().getSimpleName());
            System.err.println("Error message: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping("/staff/timetable/view")
    public String viewTimetablePage(Model model) {
        // Lấy học kỳ hiện tại
        SemesterConfig currentSemester = timetableService.getCurrentSemester();
        if (currentSemester == null) {
            model.addAttribute("error", "Không tìm thấy học kỳ hiện tại");
            return "staff/timetable/view";
        }

        // Lấy danh sách timetable trong học kỳ hiện tại
        List<Timetable> timetables = timetableService.getTimetablesBySemester(currentSemester.getId());

        // Nhóm theo môn học để hiển thị
        Map<String, List<Timetable>> timetablesByCode = timetables.stream()
                .collect(Collectors.groupingBy(t -> t.getCourse().getCode()));

        model.addAttribute("currentSemester", currentSemester);
        model.addAttribute("timetablesByCode", timetablesByCode);
        model.addAttribute("totalCourses", timetablesByCode.size());
        model.addAttribute("totalClasses", timetables.size());

        return "staff/timetable/view";
    }

    @PostMapping("/staff/timetable/delete")
    @ResponseBody
    public Map<String, Object> deleteTimetable(@RequestParam("courseCode") String courseCode) {
        Map<String, Object> response = new HashMap<>();

        try {
            SemesterConfig currentSemester = timetableService.getCurrentSemester();
            if (currentSemester == null) {
                response.put("success", false);
                response.put("message", "Không tìm thấy học kỳ hiện tại");
                return response;
            }

            Long courseId = courseService.getCourseIdByCode(courseCode);
            if (courseId == null) {
                response.put("success", false);
                response.put("message", "Không tìm thấy môn học");
                return response;
            }

            int deletedCount = timetableService.deleteTimetablesByCourseAndSemester(courseId, currentSemester.getId());

            response.put("success", true);
            response.put("message", "Đã xóa lịch học cho môn " + courseCode + " (" + deletedCount + " lớp)");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
        }

        return response;
    }

}
