package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ngocanh.timetable.domain.Timetable;
import vn.ngocanh.timetable.domain.Course;
import vn.ngocanh.timetable.domain.SemesterConfig;
import vn.ngocanh.timetable.domain.Period;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

        /**
         * Kiểm tra xem một môn học đã có lịch trong học kỳ hiện tại chưa
         */
        boolean existsByCourseAndSemesterConfig(Course course, SemesterConfig semesterConfig);

        /**
         * Kiểm tra xem một môn học đã có lịch trong học kỳ hiện tại chưa (theo ID)
         */
        @Query("SELECT COUNT(t) > 0 FROM Timetable t WHERE t.course.id = :courseId AND t.semesterConfig.id = :semesterConfigId")
        boolean existsByCourseIdAndSemesterConfigId(@Param("courseId") Long courseId,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Tìm lịch học của một môn trong học kỳ cụ thể (trả về danh sách vì có thể có
         * nhiều lớp)
         */
        List<Timetable> findByCourseAndSemesterConfig(Course course, SemesterConfig semesterConfig);

        /**
         * Tìm lịch học của một môn trong học kỳ cụ thể (theo ID) - trả về danh sách
         */
        @Query("SELECT t FROM Timetable t WHERE t.course.id = :courseId AND t.semesterConfig.id = :semesterConfigId")
        List<Timetable> findByCourseIdAndSemesterConfigId(@Param("courseId") Long courseId,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Tìm tất cả lịch học của một môn trong học kỳ cụ thể (có thể có nhiều lớp)
         */
        @Query("SELECT t FROM Timetable t WHERE t.course.id = :courseId AND t.semesterConfig.id = :semesterConfigId")
        List<Timetable> findAllByCourseIdAndSemesterConfigId(@Param("courseId") Long courseId,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Lấy tất cả lịch học trong một học kỳ
         */
        List<Timetable> findBySemesterConfig(SemesterConfig semesterConfig);

        /**
         * Lấy tất cả lịch học trong một học kỳ (theo ID)
         */
        @Query("SELECT t FROM Timetable t WHERE t.semesterConfig.id = :semesterConfigId ORDER BY t.period.day.id, t.period.number")
        List<Timetable> findBySemesterConfigId(@Param("semesterConfigId") Long semesterConfigId);

        /**
         * Lấy tất cả lịch học của một môn học (qua các học kỳ)
         */
        List<Timetable> findByCourse(Course course);

        /**
         * Lấy tất cả lịch học của một môn học (theo ID)
         */
        @Query("SELECT t FROM Timetable t WHERE t.course.id = :courseId ORDER BY t.semesterConfig.name DESC")
        List<Timetable> findByCourseId(@Param("courseId") Long courseId);

        /**
         * Kiểm tra xung đột lịch học: cùng Period và Class trong cùng học kỳ
         */
        @Query("SELECT COUNT(t) > 0 FROM Timetable t WHERE t.period.id = :periodId AND t.classRoom.id = :classId AND t.semesterConfig.id = :semesterConfigId")
        boolean existsByPeriodIdAndClassIdAndSemesterConfigId(
                        @Param("periodId") Long periodId,
                        @Param("classId") Long classId,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Tìm lịch học có xung đột: cùng Period và Class trong cùng học kỳ
         */
        @Query("SELECT t FROM Timetable t WHERE t.period.id = :periodId AND t.classRoom.id = :classId AND t.semesterConfig.id = :semesterConfigId")
        List<Timetable> findByPeriodIdAndClassIdAndSemesterConfigId(
                        @Param("periodId") Long periodId,
                        @Param("classId") Long classId,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Lấy lịch học theo trạng thái
         */
        @Query("SELECT t FROM Timetable t WHERE t.status = :status ORDER BY t.createdAt DESC")
        List<Timetable> findByStatus(@Param("status") String status);

        /**
         * Lấy lịch học theo trạng thái trong học kỳ cụ thể
         */
        @Query("SELECT t FROM Timetable t WHERE t.status = :status AND t.semesterConfig.id = :semesterConfigId ORDER BY t.period.day.id, t.period.number")
        List<Timetable> findByStatusAndSemesterConfigId(@Param("status") String status,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Lấy tất cả lịch học đang hoạt động trong học kỳ hiện tại
         */
        @Query("SELECT t FROM Timetable t WHERE t.status = 'active' AND t.semesterConfig.state = true ORDER BY t.period.day.id, t.period.number")
        List<Timetable> findActiveInCurrentSemester();

        /**
         * Xóa tất cả lịch học của một môn trong học kỳ cụ thể
         */
        void deleteByCourseAndSemesterConfig(Course course, SemesterConfig semesterConfig);

        /**
         * Xóa tất cả lịch học của một môn trong học kỳ cụ thể (theo ID)
         */
        @Query("DELETE FROM Timetable t WHERE t.course.id = :courseId AND t.semesterConfig.id = :semesterConfigId")
        void deleteByCourseIdAndSemesterConfigId(@Param("courseId") Long courseId,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Đếm số lượng lịch học trong một học kỳ
         */
        @Query("SELECT COUNT(t) FROM Timetable t WHERE t.semesterConfig.id = :semesterConfigId")
        long countBySemesterConfigId(@Param("semesterConfigId") Long semesterConfigId);

        /**
         * Lấy danh sách Period đã được sử dụng trong một Class và học kỳ cụ thể
         */
        @Query("SELECT t.period FROM Timetable t WHERE t.classRoom.id = :classId AND t.semesterConfig.id = :semesterConfigId")
        List<Period> findUsedPeriodsByClassIdAndSemesterConfigId(@Param("classId") Long classId,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Lấy danh sách Class đã được sử dụng trong một Period và học kỳ cụ thể
         */
        @Query("SELECT t.classRoom FROM Timetable t WHERE t.period.id = :periodId AND t.semesterConfig.id = :semesterConfigId")
        List<vn.ngocanh.timetable.domain.Class> findUsedClassesByPeriodIdAndSemesterConfigId(
                        @Param("periodId") Long periodId, @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Lấy lịch học theo ngày trong tuần trong học kỳ cụ thể
         */
        @Query("SELECT t FROM Timetable t WHERE t.period.day.id = :dayId AND t.semesterConfig.id = :semesterConfigId ORDER BY t.period.number")
        List<Timetable> findByDayIdAndSemesterConfigId(@Param("dayId") Long dayId,
                        @Param("semesterConfigId") Long semesterConfigId);

        /**
         * Thống kê số lượng lịch học theo trạng thái trong học kỳ
         */
        @Query("SELECT t.status, COUNT(t) FROM Timetable t WHERE t.semesterConfig.id = :semesterConfigId GROUP BY t.status")
        List<Object[]> countByStatusAndSemesterConfigId(@Param("semesterConfigId") Long semesterConfigId);
}
