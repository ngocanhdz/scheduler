package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngocanh.timetable.domain.College;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {
    College findOneByName(String name);
}
