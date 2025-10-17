package vn.ngocanh.timetable.controller.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.ngocanh.timetable.domain.CartDetail;
import vn.ngocanh.timetable.domain.OrderDetail;
import vn.ngocanh.timetable.domain.SemesterConfig;
import vn.ngocanh.timetable.service.CartService;
import vn.ngocanh.timetable.service.CourseService;
import vn.ngocanh.timetable.service.SemesterConfigService;

@Controller
public class CardController {
    private final CourseService courseService;
    private final CartService cartService;
    @Autowired
    private SemesterConfigService semesterConfigService;

    public CardController(CourseService courseService, CartService cartService) {
        this.courseService = courseService;
        this.cartService = cartService;

    }

    @GetMapping("/student/course")
    public String getAllCourse(Model model,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) Integer credit,
            @RequestParam(required = false) Integer lectureHour) {
        SemesterConfig semesterConfig = this.semesterConfigService.getSemesterConfigByState(true);
        model.addAttribute("semesterConfig", semesterConfig);
        model.addAttribute("courses", this.courseService.searchCourses(code, name, college, credit, lectureHour));
        return "student/course/show";
    }

    @PostMapping("/student/add-to-cart/{id}")
    public String addToCart(@PathVariable long id,
            HttpServletRequest request,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) Integer credit,
            @RequestParam(required = false) Integer lectureHour) {

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        this.cartService.handleAddToCart(id, email, session);

        // Xây dựng URL redirect với các tham số tìm kiếm
        StringBuilder redirectUrl = new StringBuilder("redirect:/student/course?");
        if (code != null && !code.isEmpty())
            redirectUrl.append("code=").append(code).append("&");
        if (name != null && !name.isEmpty())
            redirectUrl.append("name=").append(name).append("&");
        if (college != null && !college.isEmpty())
            redirectUrl.append("college=").append(college).append("&");
        if (credit != null)
            redirectUrl.append("credit=").append(credit).append("&");
        if (lectureHour != null)
            redirectUrl.append("lectureHour=").append(lectureHour);

        return redirectUrl.toString();
    }

    @GetMapping("/student/cart")
    public String showCart(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<CartDetail> cartDetails = this.cartService.handleGetCartDetail(session);
        SemesterConfig semesterConfig = this.semesterConfigService.getSemesterConfigByState(true);
        model.addAttribute("semesterConfig", semesterConfig);
        List<OrderDetail> orderDetails = this.cartService.getOrderDetailsBySemesterConfig(semesterConfig, session);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("cartDetails", cartDetails);
        return "student/cart/show";
    }

    @PostMapping("/student/delete-cart-course/{id}")
    public String deleteCourseCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        // TODO: process POST request
        this.cartService.handleDeleteCartDetail(id, session);
        return "redirect:/student/cart";
    }

    @PostMapping("/student/place-order")
    public String orderFromCart(HttpServletRequest request) {
        // TODO: process POST request
        HttpSession session = request.getSession();
        this.cartService.handlePlaceOrder(session);
        return "redirect:/student/cart";
    }

    @GetMapping("/create-orders")
    public String createOrders() {
        return this.cartService.createOrdersForAllUsers();
    }

    @GetMapping("/create-order-details")
    @ResponseBody
    public String createOrderDetails() {
        return this.cartService.createOrderDetailsForAllUsers();
    }
}
