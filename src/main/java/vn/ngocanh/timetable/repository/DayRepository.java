package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngocanh.timetable.domain.Day;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

    Day findOneById(long id);

    Day findOneByName(String name);

    boolean existsByName(String name);
}
