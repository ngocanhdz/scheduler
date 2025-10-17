package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngocanh.timetable.domain.Class;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

    Class findOneById(long id);

    boolean existsByName(String name);
}
