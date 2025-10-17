package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngocanh.timetable.domain.Cart;
import vn.ngocanh.timetable.domain.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart save(Cart cart);

    Cart findOneByUser(User user);
}
