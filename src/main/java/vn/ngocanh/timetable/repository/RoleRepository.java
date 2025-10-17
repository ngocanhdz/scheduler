package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngocanh.timetable.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findOneByName(String name);
}
