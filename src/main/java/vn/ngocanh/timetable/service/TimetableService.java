package vn.ngocanh.timetable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ngocanh.timetable.domain.*;
import vn.ngocanh.timetable.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimetableService {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterConfigRepository semesterConfigRepository;

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private ClassRepository classRepository;

    /**
     * Kiểm tra xem một môn học đã có lịch trong học kỳ hiện tại chưa
     */
    public boolean hasTimetableInCurrentSemester(Long courseId) {
        SemesterConfig currentSemester = getCurrentSemester();
        if (currentSemester == null) {
            return false;
        }
        return timetableRepository.existsByCourseIdAndSemesterConfigId(courseId, currentSemester.getId());
    }

    /**
     * Kiểm tra xem một môn học đã có lịch trong học kỳ cụ thể chưa
     */
    public boolean hasTimetableInSemester(Long courseId, Long semesterConfigId) {
        return timetableRepository.existsByCourseIdAndSemesterConfigId(courseId, semesterConfigId);
    }

    /**
     * Lấy học kỳ hiện tại (state = true)
     */
    public SemesterConfig getCurrentSemester() {
        List<SemesterConfig> activeSemesters = semesterConfigRepository.findByState(true);
        return activeSemesters.isEmpty() ? null : activeSemesters.get(0);
    }

    /**
     * Lấy danh sách các môn học chưa có lịch trong học kỳ hiện tại
     */
    public List<Course> getCoursesWithoutCurrentTimetable() {
        SemesterConfig currentSemester = getCurrentSemester();
        if (currentSemester == null) {
            return courseRepository.findAll();
        }

        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream()
                .filter(course -> !hasTimetableInCurrentSemester(course.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách các môn học chưa có lịch trong học kỳ cụ thể
     */
    public List<Course> getCoursesWithoutTimetableInSemester(Long semesterConfigId) {
        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream()
                .filter(course -> !hasTimetableInSemester(course.getId(), semesterConfigId))
                .collect(Collectors.toList());
    }

    /**
     * Tạo lịch học mới
     */
    public Timetable createTimetable(Long courseId, Long periodId, Long classId, Long semesterConfigId) {
        // Kiểm tra dữ liệu đầu vào
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học với ID: " + courseId));

        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiết học với ID: " + periodId));

        vn.ngocanh.timetable.domain.Class classRoom = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng học với ID: " + classId));

        SemesterConfig semesterConfig = semesterConfigRepository.findById(semesterConfigId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học kỳ với ID: " + semesterConfigId));

        // Kiểm tra xem môn học đã có lịch trong học kỳ này chưa
        if (hasTimetableInSemester(courseId, semesterConfigId)) {
            throw new RuntimeException(
                    "Môn học '" + course.getName() + "' đã có lịch trong học kỳ '" + semesterConfig.getName() + "'");
        }

        // Kiểm tra xung đột lịch học (cùng Period và Class)
        if (hasTimeConflict(periodId, classId, semesterConfigId)) {
            throw new RuntimeException(
                    "Đã có môn học khác trong tiết " + period.getName() + " tại phòng " + classRoom.getName()
                            + " trong học kỳ " + semesterConfig.getName());
        }

        // Tạo và lưu lịch học mới
        Timetable timetable = new Timetable(course, period, classRoom, semesterConfig);
        return timetableRepository.save(timetable);
    }

    /**
     * Kiểm tra xung đột lịch học
     */
    public boolean hasTimeConflict(Long periodId, Long classId, Long semesterConfigId) {
        return timetableRepository.existsByPeriodIdAndClassIdAndSemesterConfigId(periodId, classId, semesterConfigId);
    }

    /**
     * Cập nhật lịch học
     */
    public Timetable updateTimetable(Long timetableId, Long periodId, Long classId, String notes) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch học với ID: " + timetableId));
        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiết học với ID: " + periodId));

        vn.ngocanh.timetable.domain.Class classRoom = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng học với ID: " + classId));

        // Kiểm tra xung đột lịch học (trừ chính nó)
        List<Timetable> conflicts = timetableRepository.findByPeriodIdAndClassIdAndSemesterConfigId(
                periodId, classId, timetable.getSemesterConfig().getId());

        boolean hasConflict = conflicts.stream()
                .anyMatch(t -> t.getId() != timetableId);

        if (hasConflict) {
            throw new RuntimeException(
                    "Đã có môn học khác trong tiết " + period.getName() + " tại phòng " + classRoom.getName());
        }

        // Cập nhật thông tin
        timetable.setPeriod(period);
        timetable.setClassRoom(classRoom);
        timetable.setNotes(notes);

        return timetableRepository.save(timetable);
    }

    /**
     * Xóa lịch học
     */
    public void deleteTimetable(Long timetableId) {
        if (!timetableRepository.existsById(timetableId)) {
            throw new RuntimeException("Không tìm thấy lịch học với ID: " + timetableId);
        }
        timetableRepository.deleteById(timetableId);
    }

    /**
     * Xóa tất cả lịch học của một môn trong học kỳ cụ thể
     */
    public void deleteTimetableOfCourseInSemester(Long courseId, Long semesterConfigId) {
        timetableRepository.deleteByCourseIdAndSemesterConfigId(courseId, semesterConfigId);
    }

    /**
     * Lấy lịch học theo ID
     */
    public Optional<Timetable> getTimetableById(Long id) {
        return timetableRepository.findById(id);
    }

    /**
     * Lấy danh sách lịch học của một môn trong học kỳ cụ thể (có thể có nhiều lớp)
     */
    public List<Timetable> getTimetablesOfCourseInSemester(Long courseId, Long semesterConfigId) {
        return timetableRepository.findByCourseIdAndSemesterConfigId(courseId, semesterConfigId);
    }

    /**
     * Lấy tất cả lịch học trong học kỳ hiện tại
     */
    public List<Timetable> getTimetablesInCurrentSemester() {
        SemesterConfig currentSemester = getCurrentSemester();
        if (currentSemester == null) {
            return List.of();
        }
        return timetableRepository.findBySemesterConfigId(currentSemester.getId());
    }

    /**
     * Lấy tất cả lịch học trong học kỳ cụ thể
     */
    public List<Timetable> getTimetablesInSemester(Long semesterConfigId) {
        return timetableRepository.findBySemesterConfigId(semesterConfigId);
    }

    /**
     * Lấy tất cả lịch học của một môn học
     */
    public List<Timetable> getTimetablesOfCourse(Long courseId) {
        return timetableRepository.findByCourseId(courseId);
    }

    /**
     * Lấy lịch học đang hoạt động trong học kỳ hiện tại
     */
    public List<Timetable> getActiveTimetablesInCurrentSemester() {
        return timetableRepository.findActiveInCurrentSemester();
    }

    /**
     * Lấy lịch học theo trạng thái trong học kỳ cụ thể
     */
    public List<Timetable> getTimetablesByStatusInSemester(String status, Long semesterConfigId) {
        return timetableRepository.findByStatusAndSemesterConfigId(status, semesterConfigId);
    }

    /**
     * Thay đổi trạng thái lịch học
     */
    public Timetable changeStatus(Long timetableId, String status) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch học với ID: " + timetableId));

        timetable.setStatus(status);
        return timetableRepository.save(timetable);
    }

    /**
     * Kích hoạt lịch học
     */
    public Timetable activateTimetable(Long timetableId) {
        return changeStatus(timetableId, "active");
    }

    /**
     * Hủy lịch học
     */
    public Timetable cancelTimetable(Long timetableId) {
        return changeStatus(timetableId, "cancelled");
    }

    /**
     * Đếm số lượng lịch học trong học kỳ cụ thể
     */
    public long countTimetablesInSemester(Long semesterConfigId) {
        return timetableRepository.countBySemesterConfigId(semesterConfigId);
    }

    /**
     * Lấy danh sách Period đã được sử dụng trong một Class và học kỳ cụ thể
     */
    public List<Period> getUsedPeriodsInClassAndSemester(Long classId, Long semesterConfigId) {
        return timetableRepository.findUsedPeriodsByClassIdAndSemesterConfigId(classId, semesterConfigId);
    }

    /**
     * Lấy danh sách Class đã được sử dụng trong một Period và học kỳ cụ thể
     */
    public List<vn.ngocanh.timetable.domain.Class> getUsedClassesInPeriodAndSemester(Long periodId,
            Long semesterConfigId) {
        return timetableRepository.findUsedClassesByPeriodIdAndSemesterConfigId(periodId, semesterConfigId);
    }

    /**
     * Lấy lịch học theo ngày trong tuần trong học kỳ cụ thể
     */
    public List<Timetable> getTimetablesByDayInSemester(Long dayId, Long semesterConfigId) {
        return timetableRepository.findByDayIdAndSemesterConfigId(dayId, semesterConfigId);
    }

    /**
     * Kiểm tra khả năng tạo lịch học (validation đầy đủ)
     */
    public String validateCreateTimetable(Long courseId, Long periodId, Long classId, Long semesterConfigId) {
        try {
            // Kiểm tra tồn tại các entity
            if (!courseRepository.existsById(courseId)) {
                return "Không tìm thấy môn học";
            }
            if (!periodRepository.existsById(periodId)) {
                return "Không tìm thấy tiết học";
            }
            if (!classRepository.existsById(classId)) {
                return "Không tìm thấy phòng học";
            }
            if (!semesterConfigRepository.existsById(semesterConfigId)) {
                return "Không tìm thấy học kỳ";
            }

            // Kiểm tra môn học đã có lịch chưa
            if (hasTimetableInSemester(courseId, semesterConfigId)) {
                return "Môn học đã có lịch trong học kỳ này";
            }

            // Kiểm tra xung đột thời gian
            if (hasTimeConflict(periodId, classId, semesterConfigId)) {
                return "Đã có môn học khác trong thời gian và phòng học này";
            }

            return "OK"; // Hợp lệ

        } catch (Exception e) {
            return "Lỗi kiểm tra: " + e.getMessage();
        }
    }

    /**
     * Lấy tất cả timetable trong một học kỳ
     */
    public List<Timetable> getTimetablesBySemester(Long semesterConfigId) {
        return timetableRepository.findBySemesterConfigId(semesterConfigId);
    }

    /**
     * Xóa tất cả timetable của một môn học trong học kỳ cụ thể
     * 
     * @return số lượng bản ghi đã xóa
     */
    public int deleteTimetablesByCourseAndSemester(Long courseId, Long semesterConfigId) {
        List<Timetable> timetables = timetableRepository.findAllByCourseIdAndSemesterConfigId(courseId,
                semesterConfigId);
        timetableRepository.deleteAll(timetables);
        return timetables.size();
    }

    /**
     * Lưu kết quả từ Genetic Algorithm vào database
     */
    public List<Timetable> saveTimetableFromGAResult(
            vn.ngocanh.timetable.genetic.service.GeneticAlgorithmResult result,
            SemesterConfig semesterConfig) {

        if (!result.isSuccessful() || result.getBestChromosome() == null) {
            throw new IllegalArgumentException("GA result is not successful or has no valid chromosome");
        }

        List<Timetable> savedTimetables = new java.util.ArrayList<>();

        // Lấy chromosome tốt nhất
        var bestChromosome = result.getBestChromosome();

        // Convert từng gene thành Timetable entity
        for (var gene : bestChromosome.getGenes()) {
            // Lấy entities từ database
            Course course = courseRepository.findById(gene.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + gene.getCourseId()));
            Period period = periodRepository.findById(gene.getPeriodId())
                    .orElseThrow(() -> new RuntimeException("Period not found: " + gene.getPeriodId()));
            vn.ngocanh.timetable.domain.Class classEntity = classRepository.findById(gene.getClassId())
                    .orElseThrow(() -> new RuntimeException("Class not found: " + gene.getClassId()));
            // Tạo Timetable entity
            Timetable timetable = new Timetable();
            timetable.setCourse(course);
            timetable.setPeriod(period);
            timetable.setClassRoom(classEntity);
            timetable.setSemesterConfig(semesterConfig);

            // Lưu vào database
            Timetable savedTimetable = timetableRepository.save(timetable);
            savedTimetables.add(savedTimetable);
        }

        return savedTimetables;
    }
}
