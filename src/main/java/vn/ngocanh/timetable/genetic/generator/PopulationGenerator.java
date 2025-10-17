package vn.ngocanh.timetable.genetic.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.ngocanh.timetable.domain.*;
import vn.ngocanh.timetable.genetic.config.GeneticAlgorithmConfig;
import vn.ngocanh.timetable.genetic.model.TimetableChromosome;
import vn.ngocanh.timetable.genetic.model.TimetableGene;
import vn.ngocanh.timetable.repository.OrderDetailRepository;
import vn.ngocanh.timetable.service.*;

import java.util.*;

/**
 * Tạo quần thể ban đầu cho thuật toán di truyền
 * Mỗi chromosome đại diện cho một thời khóa biểu hoàn chỉnh
 */
@Component
public class PopulationGenerator {
    @Autowired
    private CourseService courseService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private ClassService classService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    private Random random = new Random();

    /**
     * Tạo quần thể ban đầu
     */
    public List<TimetableChromosome> generateInitialPopulation(
            List<Long> courseIds,
            Long semesterConfigId,
            GeneticAlgorithmConfig config,
            Set<String> existingTimeSlots) {

        List<TimetableChromosome> population = new ArrayList<>();

        // Lấy dữ liệu cần thiết
        List<Course> courses = getCoursesByIds(courseIds);
        List<Period> availablePeriods = periodService.getAllPeriod();
        List<vn.ngocanh.timetable.domain.Class> availableClasses = classService.getAllClass();

        // Tạo danh sách các yêu cầu lớp học (CourseClassRequirement)
        List<CourseClassRequirement> requirements = generateCourseClassRequirements(courses, semesterConfigId);

        // Tạo từng chromosome trong quần thể
        for (int i = 0; i < config.getPopulationSize(); i++) {
            TimetableChromosome chromosome = generateRandomChromosome(
                    requirements, availablePeriods, availableClasses, existingTimeSlots);
            population.add(chromosome);
        }

        return population;
    }

    /**
     * Tạo danh sách yêu cầu lớp học dựa trên số sinh viên đăng ký và sức chứa phòng
     */
    private List<CourseClassRequirement> generateCourseClassRequirements(
            List<Course> courses, Long semesterConfigId) {

        List<CourseClassRequirement> requirements = new ArrayList<>();

        for (Course course : courses) {
            // Lấy số sinh viên đăng ký môn học từ OrderDetail
            int enrolledStudents = getEnrolledStudentCount(course.getId(), semesterConfigId);

            // Lấy sức chứa trung bình của các phòng học
            int averageCapacity = getAverageClassCapacity();

            // Tính số lớp cần thiết
            int requiredClasses = (int) Math.ceil((double) enrolledStudents / averageCapacity);
            if (requiredClasses == 0)
                requiredClasses = 1; // Ít nhất 1 lớp

            // Tạo yêu cầu cho từng lớp
            for (int classIndex = 1; classIndex <= requiredClasses; classIndex++) {
                CourseClassRequirement requirement = new CourseClassRequirement(
                        course.getId(),
                        classIndex,
                        (int) course.getLectureHour(), // Số kíp cần thiết
                        Math.min(enrolledStudents, averageCapacity) // Số sinh viên cho lớp này
                );
                requirements.add(requirement);
                enrolledStudents -= averageCapacity; // Trừ đi số sinh viên đã xếp
            }
        }

        return requirements;
    }

    /**
     * Tạo một chromosome ngẫu nhiên
     */
    private TimetableChromosome generateRandomChromosome(
            List<CourseClassRequirement> requirements,
            List<Period> availablePeriods,
            List<vn.ngocanh.timetable.domain.Class> availableClasses,
            Set<String> existingTimeSlots) {

        List<TimetableGene> genes = new ArrayList<>();
        Set<String> usedTimeSlots = new HashSet<>(existingTimeSlots); // Bắt đầu với time slots đã sử dụng

        for (CourseClassRequirement requirement : requirements) {
            List<TimetableGene> classGenes = generateGenesForClass(
                    requirement, availablePeriods, availableClasses, usedTimeSlots);
            genes.addAll(classGenes);
        }

        return new TimetableChromosome(genes);
    }

    /**
     * Tạo các gene cho một lớp học (nhiều kíp liên tiếp)
     */
    private List<TimetableGene> generateGenesForClass(
            CourseClassRequirement requirement,
            List<Period> availablePeriods,
            List<vn.ngocanh.timetable.domain.Class> availableClasses,
            Set<String> usedTimeSlots) {

        List<TimetableGene> classGenes = new ArrayList<>();
        int maxAttempts = 100; // Tối đa 100 lần thử

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            // Chọn phòng học ngẫu nhiên có đủ sức chứa
            vn.ngocanh.timetable.domain.Class selectedClass = selectSuitableClass(
                    availableClasses, requirement.getStudentCount());

            if (selectedClass == null)
                continue;

            // Tìm dãy kíp liên tiếp có thể sử dụng
            List<Period> consecutivePeriods = findConsecutivePeriods(
                    availablePeriods, requirement.getSessionCount(), usedTimeSlots, selectedClass.getId());

            if (consecutivePeriods.size() == requirement.getSessionCount()) {
                // Tạo gene cho từng kíp
                for (int sessionIndex = 0; sessionIndex < consecutivePeriods.size(); sessionIndex++) {
                    Period period = consecutivePeriods.get(sessionIndex);

                    TimetableGene gene = new TimetableGene(
                            requirement.getCourseId(),
                            period.getId(),
                            selectedClass.getId(),
                            requirement.getClassIndex(),
                            sessionIndex + 1);

                    classGenes.add(gene);
                    usedTimeSlots.add(gene.getTimeSlotKey());
                }
                break; // Thành công, thoát khỏi vòng lặp
            }
        }

        // Nếu không tìm được, tạo gene không hợp lệ (sẽ bị phạt trong fitness)
        if (classGenes.isEmpty()) {
            for (int sessionIndex = 1; sessionIndex <= requirement.getSessionCount(); sessionIndex++) {
                TimetableGene gene = new TimetableGene(
                        requirement.getCourseId(),
                        availablePeriods.get(random.nextInt(availablePeriods.size())).getId(),
                        availableClasses.get(random.nextInt(availableClasses.size())).getId(),
                        requirement.getClassIndex(),
                        sessionIndex);
                classGenes.add(gene);
            }
        }

        return classGenes;
    }

    /**
     * Chọn phòng học phù hợp với số sinh viên
     */
    private vn.ngocanh.timetable.domain.Class selectSuitableClass(
            List<vn.ngocanh.timetable.domain.Class> availableClasses, int studentCount) {

        // Lọc các phòng có đủ sức chứa
        List<vn.ngocanh.timetable.domain.Class> suitableClasses = availableClasses.stream()
                .filter(c -> c.getCapacity() >= studentCount)
                .toList();

        if (suitableClasses.isEmpty()) {
            // Nếu không có phòng đủ sức chứa, chọn phòng lớn nhất
            return availableClasses.stream()
                    .max(Comparator.comparing(vn.ngocanh.timetable.domain.Class::getCapacity))
                    .orElse(null);
        }

        // Chọn ngẫu nhiên từ các phòng phù hợp
        return suitableClasses.get(random.nextInt(suitableClasses.size()));
    }

    /**
     * Tìm dãy kíp liên tiếp có thể sử dụng
     */
    private List<Period> findConsecutivePeriods(
            List<Period> availablePeriods,
            int sessionCount,
            Set<String> usedTimeSlots,
            Long classId) {

        // Sắp xếp period theo number để tìm dãy liên tiếp
        List<Period> sortedPeriods = availablePeriods.stream()
                .sorted(Comparator.comparing(Period::getNumber))
                .toList();

        for (int i = 0; i <= sortedPeriods.size() - sessionCount; i++) {
            List<Period> candidate = new ArrayList<>();
            boolean isValid = true;

            // Kiểm tra dãy liên tiếp
            for (int j = 0; j < sessionCount; j++) {
                Period period = sortedPeriods.get(i + j);

                // Kiểm tra số thứ tự liên tiếp
                if (j > 0) {
                    Period prevPeriod = sortedPeriods.get(i + j - 1);
                    if (period.getNumber() != prevPeriod.getNumber() + 1) {
                        isValid = false;
                        break;
                    }
                }

                // Kiểm tra time slot chưa được sử dụng
                String timeSlotKey = period.getId() + "_" + classId;
                if (usedTimeSlots.contains(timeSlotKey)) {
                    isValid = false;
                    break;
                }

                candidate.add(period);
            }

            if (isValid) {
                return candidate;
            }
        }

        return new ArrayList<>(); // Không tìm được dãy liên tiếp
    }

    // Helper methods

    private List<Course> getCoursesByIds(List<Long> courseIds) {
        return courseIds.stream()
                .map(courseService::getCourseById)
                .filter(Objects::nonNull)
                .toList();
    }

    private int getEnrolledStudentCount(Long courseId, Long semesterConfigId) {
        Long count = orderDetailRepository.countByCourseIdAndSemesterConfigId(courseId, semesterConfigId);
        return count != null ? count.intValue() : 0;
    }

    private int getAverageClassCapacity() {
        List<vn.ngocanh.timetable.domain.Class> allClasses = classService.getAllClass();
        if (allClasses.isEmpty())
            return 50;

        return (int) allClasses.stream()
                .mapToLong(vn.ngocanh.timetable.domain.Class::getCapacity)
                .average()
                .orElse(50.0);
    }

    /**
     * Class helper để định nghĩa yêu cầu lớp học
     */
    public static class CourseClassRequirement {
        private Long courseId;
        private Integer classIndex;
        private Integer sessionCount;
        private Integer studentCount;

        public CourseClassRequirement(Long courseId, Integer classIndex,
                Integer sessionCount, Integer studentCount) {
            this.courseId = courseId;
            this.classIndex = classIndex;
            this.sessionCount = sessionCount;
            this.studentCount = studentCount;
        }

        // Getters
        public Long getCourseId() {
            return courseId;
        }

        public Integer getClassIndex() {
            return classIndex;
        }

        public Integer getSessionCount() {
            return sessionCount;
        }

        public Integer getStudentCount() {
            return studentCount;
        }
    }
}
