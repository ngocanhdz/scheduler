package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ngocanh.timetable.domain.Course;
import vn.ngocanh.timetable.domain.Order;
import vn.ngocanh.timetable.domain.OrderDetail;
import vn.ngocanh.timetable.domain.SemesterConfig;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
        OrderDetail save(OrderDetail orderDetail);

        OrderDetail findOneByOrderAndCourseAndSemesterConfig(Order order, Course course, SemesterConfig semesterConfig);

        List<OrderDetail> findBySemesterConfigAndOrder(SemesterConfig semesterConfig, Order order); // Add method to
                                                                                                    // find all
                                                                                                    // OrderDetails by
                                                                                                    // SemesterConfig
                                                                                                    // for cleanup

        List<OrderDetail> findBySemesterConfig(SemesterConfig semesterConfig); // Bulk delete method for better
                                                                               // performance

        @Modifying
        @Query("DELETE FROM OrderDetail od WHERE od.semesterConfig = :semesterConfig")
        void deleteAllBySemesterConfig(@Param("semesterConfig") SemesterConfig semesterConfig);

        @Query("SELECT od.yearLevel, COUNT(od) FROM OrderDetail od " +
                        "LEFT JOIN od.semesterConfig sc WHERE sc.state = true " +
                        "AND (:yearLevel IS NULL OR od.yearLevel = :yearLevel) " +
                        "AND (:college IS NULL OR od.college = :college) " +
                        "GROUP BY od.yearLevel")
        List<Object[]> getYearLevelStats(@Param("yearLevel") Long yearLevel,
                        @Param("college") String college);

        /**
         * Đếm số sinh viên đăng ký môn học trong học kỳ cụ thể
         */
        @Query("SELECT COUNT(od) FROM OrderDetail od WHERE od.course.id = :courseId AND od.semesterConfig.id = :semesterConfigId")
        Long countByCourseIdAndSemesterConfigId(@Param("courseId") Long courseId,
                        @Param("semesterConfigId") Long semesterConfigId);
}
