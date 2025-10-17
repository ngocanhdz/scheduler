package vn.ngocanh.timetable.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ngocanh.timetable.domain.SemesterConfig;

public interface SemesterConfigRepository extends JpaRepository<SemesterConfig, Long> {
    SemesterConfig findOneById(long id);

    List<SemesterConfig> findAll();

    List<SemesterConfig> findByState(boolean state);

    SemesterConfig findOneByName(String name);

    SemesterConfig findOneByState(Boolean state);
}
