package vn.ngocanh.timetable.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ngocanh.timetable.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String Email);// import // validate

    User findOneById(long id);// RUD

    User findOneByEmail(String email);

    // Phân trang và tìm kiếm
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role LEFT JOIN FETCH u.college " +
            "WHERE (:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) " +
            "AND (:yearLevel IS NULL OR u.yearLevel = :yearLevel) " +
            "AND (:roleName IS NULL OR LOWER(u.role.name) LIKE LOWER(CONCAT('%', :roleName, '%'))) " +
            "AND (:collegeName IS NULL OR LOWER(u.college.name) LIKE LOWER(CONCAT('%', :collegeName, '%')))")
    Page<User> findUsersWithFilters(@Param("fullName") String fullName,
            @Param("yearLevel") Long yearLevel,
            @Param("roleName") String roleName,
            @Param("collegeName") String collegeName,
            Pageable pageable);

    // Lấy danh sách cho filter dropdowns
    @Query("SELECT DISTINCT r.name FROM User u JOIN u.role r ORDER BY r.name")
    java.util.List<String> findAllRoleNames();

    @Query("SELECT DISTINCT c.name FROM User u JOIN u.college c ORDER BY c.name")
    java.util.List<String> findAllCollegeNames();

    @Query("SELECT DISTINCT u.yearLevel FROM User u WHERE u.yearLevel IS NOT NULL ORDER BY u.yearLevel")
    java.util.List<Long> findAllYearLevels();
}
