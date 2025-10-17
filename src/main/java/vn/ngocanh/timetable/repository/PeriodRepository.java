package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngocanh.timetable.domain.Period;
import vn.ngocanh.timetable.domain.Day;

import java.util.List;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {

    Period findOneById(long id);

    List<Period> findByDay(Day day);

    boolean existsByNameAndDay(String name, Day day);
}
