package vn.ngocanh.timetable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ngocanh.timetable.domain.Cart;
import vn.ngocanh.timetable.domain.CartDetail;
import vn.ngocanh.timetable.domain.Course;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    CartDetail save(CartDetail cartDetail);

    CartDetail findByCartAndCourse(Cart cart, Course course);

    // delete course in cart
    void deleteById(long id);

    // findById for update sum credit
    CartDetail findOneById(long id);

    void deleteAllByCart(Cart cart);
}
