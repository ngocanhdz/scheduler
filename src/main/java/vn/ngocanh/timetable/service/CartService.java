package vn.ngocanh.timetable.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import vn.ngocanh.timetable.domain.Cart;
import vn.ngocanh.timetable.domain.CartDetail;
import vn.ngocanh.timetable.domain.Course;
import vn.ngocanh.timetable.domain.Order;
import vn.ngocanh.timetable.domain.OrderDetail;
import vn.ngocanh.timetable.domain.SemesterConfig;
import vn.ngocanh.timetable.domain.User;
import vn.ngocanh.timetable.repository.CartDetailRepository;
import vn.ngocanh.timetable.repository.CartRepository;
import vn.ngocanh.timetable.repository.CourseRepository;
import vn.ngocanh.timetable.repository.OrderDetailRepository;
import vn.ngocanh.timetable.repository.OrderRepository;

@Service
public class CartService {

    private final SemesterConfigService semesterConfigService;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CourseRepository courseRepository;

    CartService(SemesterConfigService semesterConfigService) {
        this.semesterConfigService = semesterConfigService;
    }

    public void handleAddToCart(long id, String email, HttpSession session) {
        User user = this.userService.getUserByEmail(email);
        Course course = this.courseService.getCourseById(id);
        Cart cart = this.cartRepository.findOneByUser(user);
        if (cart == null) {
            Cart otherCart = new Cart();
            otherCart.setSum(0);
            otherCart.setUser(user);
            otherCart.setSumCredit(0);
            cart = this.cartRepository.save(otherCart);
        }
        CartDetail oldCartDetail = this.cartDetailRepository.findByCartAndCourse(cart, course);
        if (oldCartDetail == null) {
            CartDetail cartDetail = new CartDetail();
            cartDetail.setCart(cart);
            cartDetail.setCourse(course);
            this.cartDetailRepository.save(cartDetail);
            cart.setSum(cart.getSum() + 1);
            cart.setSumCredit(cart.getSumCredit() + course.getCredit());
            this.cartRepository.save(cart);
            session.setAttribute("sum", cart.getSum());
            session.setAttribute("sumCredit", cart.getSumCredit());
            session.setAttribute("message", "Thêm học phần thành công!");
        } else {
            session.setAttribute("message", "Học phần này đã có trong danh sách đăng ký!");
        }
    }

    public List<CartDetail> handleGetCartDetail(HttpSession session) {
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        Cart cart = this.cartRepository.findOneByUser(user);
        return cart.getCartDetail();
    }

    public void handleDeleteCartDetail(long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        Cart cart = this.cartRepository.findOneByUser(user);
        CartDetail cartDetail = this.cartDetailRepository.findOneById(id);
        long sumCredit = 0;
        long sum = 0;
        if (cart != null) {

            List<CartDetail> cartDetails = cart.getCartDetail();
            for (CartDetail cartDetail2 : cartDetails) {
                sumCredit += cartDetail2.getCourse().getCredit();
                sum += 1;
            }
            cart.setSum(sum - 1);
            cart.setSumCredit(sumCredit - cartDetail.getCourse().getCredit());
        }
        if (cart != null) {
            session.setAttribute("sumCredit", cart.getSumCredit());
            session.setAttribute("sum", cart.getSum());
        }
        this.cartRepository.save(cart);
        this.cartDetailRepository.deleteById(id);
    }

    @Transactional
    public void handlePlaceOrder(HttpSession session) {
        User user = this.userService.getUserByEmail((String) session.getAttribute("email"));
        Cart cart = this.cartRepository.findOneByUser(user);
        List<CartDetail> cartDetails = cart.getCartDetail();
        Order order = this.orderRepository.findOneByUser(user);
        if (order == null) {
            Order otherOrder = new Order();
            otherOrder.setUser(user);
            order = this.orderRepository.save(otherOrder);
        }
        for (CartDetail cartDetail : cartDetails) {
            OrderDetail orderDetail = this.orderDetailRepository.findOneByOrderAndCourseAndSemesterConfig(order,
                    cartDetail.getCourse(), this.semesterConfigService.getSemesterConfigByState(true));
            if (orderDetail == null) {

                OrderDetail ortherOrderDetail = new OrderDetail();
                ortherOrderDetail.setCollege(user.getCollege().getName());
                ortherOrderDetail.setYearLevel(user.getYearLevel());
                ortherOrderDetail.setCourse(cartDetail.getCourse());
                ortherOrderDetail.setOrder(order);
                ortherOrderDetail.setSemesterConfig(this.semesterConfigService.getSemesterConfigByState(true));
                orderDetail = this.orderDetailRepository.save(ortherOrderDetail);
            } else {
                continue;
            }
        }
        this.cartDetailRepository.deleteAllByCart(cart);
        cart.setSum(0);
        cart.setSumCredit(0);
        this.cartRepository.save(cart);
        session.setAttribute("sum", 0);
        session.setAttribute("sumCredit", 0);
    }

    public List<OrderDetail> getOrderDetailsBySemesterConfig(SemesterConfig semesterConfig, HttpSession session) {
        User user = this.userService.getUserByEmail((String) session.getAttribute("email"));
        Order order = this.orderRepository.findOneByUser(user);
        return this.orderDetailRepository.findBySemesterConfigAndOrder(semesterConfig, order);
    }

    @Transactional
    public String createOrdersForAllUsers() {
        try {
            List<User> allUsers = userService.getAllUser();
            int createdCount = 0;

            for (User user : allUsers) {
                Order existingOrder = orderRepository.findOneByUser(user);
                if (existingOrder == null) {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    orderRepository.save(newOrder);
                    createdCount++;
                }
            }
            return "Đã tạo " + createdCount + " orders cho " + allUsers.size() + " users";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @Transactional
    public String createOrderDetailsForAllUsers() {
        try {
            long startTime = System.currentTimeMillis();

            // Get active semester first
            SemesterConfig activeSemester = semesterConfigService.getSemesterConfigByState(true);
            if (activeSemester == null) {
                return "❌ Không có semester nào đang hoạt động (state=true)";
            }

            System.out.println("🚀 OrderDetail Creation Started");
            System.out.println(
                    "📅 Active semester: " + activeSemester.getName() + " (ID: " + activeSemester.getId() + ")");

            // Clean up existing OrderDetails for active semester
            System.out.println("🗑️ Cleaning up existing OrderDetails for active semester...");
            try {
                List<OrderDetail> existingOrderDetails = orderDetailRepository.findBySemesterConfig(activeSemester);
                int existingCount = existingOrderDetails.size();

                if (existingCount > 0) {
                    System.out.println("📊 Found " + existingCount + " existing OrderDetails to delete");
                    orderDetailRepository.deleteAllBySemesterConfig(activeSemester);
                    System.out.println("✅ Successfully deleted " + existingCount + " old OrderDetails");
                } else {
                    System.out.println("✅ No existing OrderDetails found - starting fresh");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error during cleanup: " + e.getMessage() + " - continuing anyway");
            }

            // Get all students
            List<User> allUsers = userService.getAllUser();
            List<User> students = allUsers.stream()
                    .filter(user -> "STUDENT".equals(user.getRole().getName()))
                    .collect(Collectors.toList());

            System.out.println("👥 Found " + students.size() + " students out of " + allUsers.size() + " total users");

            if (students.isEmpty()) {
                return "❌ Không tìm thấy student nào!";
            }

            // Pre-load data
            System.out.println("⚡ Pre-loading courses and orders...");
            List<Course> allCourses = courseRepository.findAll();
            List<Order> allOrders = orderRepository.findAll();
            Map<Long, Order> orderMap = allOrders.stream()
                    .collect(Collectors.toMap(order -> order.getUser().getId(), order -> order));

            System.out.println("📚 Loaded " + allCourses.size() + " courses and " + allOrders.size() + " orders");

            // Define course selection logic
            Random random = new Random(System.currentTimeMillis());

            // Helper: Check if course code has no letter suffix (MI1111 > MI1111Q)
            java.util.function.Predicate<Course> hasNoLetterSuffix = course -> {
                String code = course.getCode();
                if (code == null || code.length() < 2)
                    return false;
                char lastChar = code.charAt(code.length() - 1);
                return Character.isDigit(lastChar);
            };

            // Keywords for Year 1-2 general education courses
            String[] generalEducationKeywords = {
                    "triết học", "kinh tế chính trị", "chủ nghĩa xã hội khoa học",
                    "lịch sử đảng cộng sản việt nam", "tư tưởng hồ chí minh",
                    "đường lối quốc phòng và an ninh của đảng cộng sản việt nam",
                    "công tác quốc phòng và an ninh", "quân sự chung",
                    "kỹ thuật chiến đấu bộ binh và chiến thuật", "tin học đại cương",
                    "giải tích", "đại số", "phương pháp tính", "xác suất thống kê", "vật lý đại cương"
            };

            // Filter general courses for Year 1-2
            List<Course> generalCourses = allCourses.stream()
                    .filter(course -> {
                        String name = course.getName().toLowerCase();
                        for (String keyword : generalEducationKeywords) {
                            if (name.contains(keyword.toLowerCase())) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .sorted((c1, c2) -> {
                        boolean c1NoSuffix = hasNoLetterSuffix.test(c1);
                        boolean c2NoSuffix = hasNoLetterSuffix.test(c2);
                        if (c1NoSuffix && !c2NoSuffix)
                            return -1;
                        if (!c1NoSuffix && c2NoSuffix)
                            return 1;
                        return c1.getCode().compareTo(c2.getCode());
                    })
                    .collect(Collectors.toList());

            // Group courses by college for Year 3-4
            Map<String, List<Course>> coursesByCollegeYear3 = allCourses.stream()
                    .filter(course -> {
                        String code = course.getCode();
                        if (code == null || code.length() < 3)
                            return false;
                        char yearDigit = code.charAt(2);
                        return yearDigit == '2' || yearDigit == '3';
                    })
                    .sorted((c1, c2) -> {
                        boolean c1NoSuffix = hasNoLetterSuffix.test(c1);
                        boolean c2NoSuffix = hasNoLetterSuffix.test(c2);
                        if (c1NoSuffix && !c2NoSuffix)
                            return -1;
                        if (!c1NoSuffix && c2NoSuffix)
                            return 1;
                        return c1.getCode().compareTo(c2.getCode());
                    })
                    .collect(Collectors.groupingBy(course -> course.getCollege().getName()));

            Map<String, List<Course>> coursesByCollegeYear4 = allCourses.stream()
                    .filter(course -> {
                        String code = course.getCode();
                        if (code == null || code.length() < 3)
                            return false;
                        char yearDigit = code.charAt(2);
                        return yearDigit == '3' || yearDigit == '4';
                    })
                    .sorted((c1, c2) -> {
                        boolean c1NoSuffix = hasNoLetterSuffix.test(c1);
                        boolean c2NoSuffix = hasNoLetterSuffix.test(c2);
                        if (c1NoSuffix && !c2NoSuffix)
                            return -1;
                        if (!c1NoSuffix && c2NoSuffix)
                            return 1;
                        return c1.getCode().compareTo(c2.getCode());
                    })
                    .collect(Collectors.groupingBy(course -> course.getCollege().getName()));
            System.out.println("🎓 Found " + generalCourses.size() + " general education courses");
            System.out.println(
                    "🏫 Year 3 courses: " + coursesByCollegeYear3.values().stream().mapToInt(List::size).sum());
            System.out.println(
                    "🏫 Year 4 courses: " + coursesByCollegeYear4.values().stream().mapToInt(List::size).sum());

            // Process students
            List<OrderDetail> batchOrderDetails = new ArrayList<>();
            int totalCreated = 0;
            int processedStudents = 0;

            // Shuffle students for randomness
            Collections.shuffle(students, random);

            for (User student : students) {
                try {

                    // Get or create order
                    Order studentOrder = orderMap.get(student.getId());
                    if (studentOrder == null) {
                        studentOrder = new Order();
                        studentOrder.setUser(student);
                        studentOrder = orderRepository.save(studentOrder);
                        orderMap.put(student.getId(), studentOrder);
                    }

                    // Select courses based on year level
                    List<Course> availableCourses = new ArrayList<>();
                    List<Course> selectedCourses = new ArrayList<>();

                    if (student.getYearLevel() == 1 || student.getYearLevel() == 2) {
                        // Year 1-2: General education courses
                        availableCourses = new ArrayList<>(generalCourses);
                    } else if (student.getYearLevel() == 3) {
                        // Year 3: XX2XXX/XX3XXX courses from same college
                        String collegeName = student.getCollege().getName();
                        availableCourses = coursesByCollegeYear3.getOrDefault(collegeName, new ArrayList<>());
                    } else if (student.getYearLevel() == 4) {
                        // Year 4: XX3XXX/XX4XXX courses from same college
                        String collegeName = student.getCollege().getName();
                        availableCourses = coursesByCollegeYear4.getOrDefault(collegeName, new ArrayList<>());
                    }

                    if (availableCourses.isEmpty()) {
                        System.out.println("⚠️ No courses available for student " + student.getId() + " (Year "
                                + student.getYearLevel() + ", College: " + student.getCollege().getName() + ")");
                        continue;
                    }

                    // Random selection: 6-7 courses per student
                    int numCourses = 6 + random.nextInt(2); // 6 or 7
                    int actualCourses = Math.min(numCourses, availableCourses.size());

                    // Shuffle available courses for randomness
                    Collections.shuffle(availableCourses, random);
                    selectedCourses = availableCourses.subList(0, actualCourses); // Create OrderDetails
                    for (Course selectedCourse : selectedCourses) {
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setOrder(studentOrder);
                        orderDetail.setCourse(selectedCourse);
                        orderDetail.setSemesterConfig(activeSemester);
                        orderDetail.setYearLevel(student.getYearLevel());
                        orderDetail.setCollege(student.getCollege().getName());

                        batchOrderDetails.add(orderDetail);
                        totalCreated++;
                    }

                    processedStudents++;

                    // Batch save every 1000 records
                    if (batchOrderDetails.size() >= 1000) {
                        orderDetailRepository.saveAll(batchOrderDetails);
                        batchOrderDetails.clear();

                        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                        double progress = (processedStudents * 100.0) / students.size();
                        System.out.println(
                                String.format("⚡ Progress: %.1f%% (%d/%d students, %d OrderDetails, %ds elapsed)",
                                        progress, processedStudents, students.size(), totalCreated, elapsed));
                    }

                } catch (Exception e) {
                    System.err.println("❌ Error processing student " + student.getId() + ": " + e.getMessage());
                    continue;
                }
            }

            // Save remaining OrderDetails
            if (!batchOrderDetails.isEmpty()) {
                orderDetailRepository.saveAll(batchOrderDetails);
                System.out.println("💾 Saved final batch of " + batchOrderDetails.size() + " OrderDetails");
            } // Final statistics
            long totalTime = (System.currentTimeMillis() - startTime) / 1000;
            double rate = totalCreated / (totalTime > 0 ? totalTime : 1.0);
            String result = String.format(
                    "🎉 SUCCESS! Created %d OrderDetails for %d students in %d seconds (%.1f per second)",
                    totalCreated, processedStudents, totalTime, rate);

            System.out.println("═══════════════════════════════════════");
            System.out.println(result);
            System.out.println("📊 Average OrderDetails per student: "
                    + String.format("%.1f", totalCreated / (double) processedStudents));
            System.out.println("⚡ Performance: " + String.format("%.1f", rate) + " OrderDetails/second");
            System.out.println("🎓 Course Selection Logic:");
            System.out.println("   📚 Year 1-2: General education courses (exact keywords)");
            System.out.println("   🎯 Year 3: XX2XXX/XX3XXX courses in student's college");
            System.out.println("   🎓 Year 4: XX3XXX/XX4XXX courses in student's college");
            System.out.println("   ⭐ Priority: MI1111 > MI1111Q (no letter suffix preferred)");
            System.out.println("   🎲 Selection: Random 6-7 courses per student");
            System.out.println("   📈 Distribution: Natural & Random for all colleges");
            System.out.println("═══════════════════════════════════════");

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error: " + e.getMessage();
        }
    }
}
