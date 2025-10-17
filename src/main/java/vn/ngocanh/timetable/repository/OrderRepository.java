package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngocanh.timetable.domain.Order;
import vn.ngocanh.timetable.domain.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order Order);

    Order findOneByUser(User user);
}
