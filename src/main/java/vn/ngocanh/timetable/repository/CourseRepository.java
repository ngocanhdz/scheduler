package vn.ngocanh.timetable.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ngocanh.timetable.domain.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
        void deleteById(long id);

        Course findOneById(long id);

        Course findOneByCode(String code);

        Boolean existsByCode(String code);

        // Find courses containing specific keywords in name (for junior students)
        @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Course> findByNameContainingIgnoreCase(@Param("keyword") String keyword);

        // Find courses by college name (for senior students)
        @Query("SELECT c FROM Course c JOIN c.college col WHERE col.name = :collegeName")
        List<Course> findByCollegeName(@Param("collegeName") String collegeName); // Native query để lấy courses với số
                                                                                  // OrderDetail theo filter và sorting

        @Query(value = "SELECT c.id, c.code, c.name, c.description, c.credit, " +
                        "col.name as college_name, " +
                        "COUNT(CASE WHEN (COALESCE(:yearLevel, 0) = 0 OR od.year_level = :yearLevel) " +
                        "               AND (COALESCE(:college, '') = '' OR od.college = :college) " +
                        "               THEN od.id END) as filtered_order_detail_count, " +
                        "COUNT(od.id) as total_order_detail_count " +
                        "FROM course c " +
                        "LEFT JOIN college col ON c.college_id = col.id " +
                        "INNER JOIN order_detail od ON c.id = od.course_id " +
                        "INNER JOIN semester_configs sc ON od.semester_config_id = sc.id " +
                        "WHERE sc.state = true " +
                        "GROUP BY c.id, c.code, c.name, c.description, c.credit, col.name " +
                        "HAVING COUNT(CASE WHEN (COALESCE(:yearLevel, 0) = 0 OR od.year_level = :yearLevel) " +
                        "                      AND (COALESCE(:college, '') = '' OR od.college = :college) " +
                        "                      THEN od.id END) > 0 " +
                        "ORDER BY " +
                        "  CASE WHEN :sortBy = 'code' AND :sortDir = 'asc' THEN c.code END ASC, " +
                        "  CASE WHEN :sortBy = 'code' AND :sortDir = 'desc' THEN c.code END DESC, " +
                        "  CASE WHEN :sortBy = 'name' AND :sortDir = 'asc' THEN c.name END ASC, " +
                        "  CASE WHEN :sortBy = 'name' AND :sortDir = 'desc' THEN c.name END DESC, " +
                        "  CASE WHEN :sortBy = 'orderDetailCount' AND :sortDir = 'asc' THEN COUNT(CASE WHEN (COALESCE(:yearLevel, 0) = 0 OR od.year_level = :yearLevel) AND (COALESCE(:college, '') = '' OR od.college = :college) THEN od.id END) END ASC, "
                        +
                        "  CASE WHEN :sortBy = 'orderDetailCount' AND :sortDir = 'desc' THEN COUNT(CASE WHEN (COALESCE(:yearLevel, 0) = 0 OR od.year_level = :yearLevel) AND (COALESCE(:college, '') = '' OR od.college = :college) THEN od.id END) END DESC, "
                        +
                        "  c.name ASC", nativeQuery = true)
        List<Object[]> findCoursesWithOrderDetailCountByFilter(@Param("yearLevel") Long yearLevel,
                        @Param("college") String college,
                        @Param("sortBy") String sortBy,
                        @Param("sortDir") String sortDir);
}
