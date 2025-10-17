<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8">
                    <title>Quản lý học tập</title>
                    <meta content="width=device-width, initial-scale=1.0" name="viewport">
                    <meta content="" name="keywords">
                    <meta content="" name="description">

                    <!-- Google Web Fonts -->
                    <link rel="preconnect" href="https://fonts.googleapis.com">
                    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                    <link
                        href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
                        rel="stylesheet">

                    <!-- Icon Font Stylesheet -->
                    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css"
                        rel="stylesheet">

                    <!-- Libraries Stylesheet -->
                    <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
                    <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">


                    <!-- Customized Bootstrap Stylesheet -->
                    <link href="/client/css/bootstrap.min.css" rel="stylesheet">

                    <!-- Template Stylesheet -->
                    <link href="/client/css/style.css" rel="stylesheet">
                </head>

                <body>
                    <jsp:include page="../layout/header.jsp" />
                    <!-- Spinner Start -->
                    <div id="spinner"
                        class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
                        <div class="spinner-grow text-danger" role="status"></div>
                    </div>
                    <!-- Spinner End -->
                    <!-- Banner Start -->
                    <jsp:include page="../layout/banner.jsp" />
                    <!-- Banner End -->


                    <!-- Featurs Section Start -->
                    <jsp:include page="../layout/feature.jsp" />
                    <!-- feature Section End -->
                    <!--Time Table Start-->
                    <div class="container-fluid py-5">
                        <div class="container py-5">
                            <h1 class="mb-4">Chức năng chính</h1>
                            <div class="row g-4 mb-5">
                                <div class="col-lg-4 col-md-6">
                                    <div class="border border-danger rounded position-relative overflow-hidden">
                                        <div class="vesitable-img">
                                            <img src="/client/img/timetable.png" class="img-fluid w-100 rounded-top"
                                                alt="Giáo vụ">
                                        </div>
                                        <div class="text-white bg-danger px-3 py-1 rounded position-absolute"
                                            style="top: 10px; right: 10px;">Giáo vụ</div>
                                        <div class="p-4 pb-3 rounded-bottom d-flex flex-column">
                                            <h4 class="text-danger mb-3">Quản lý thời khóa biểu</h4>
                                            <p class="mb-4">Xem và Tự động tạo thời khóa biểu giúp đơn giản hóa công
                                                việc của giáo vụ </p>
                                            <div class="mt-4">
                                                <a href="/staff/timetable/course"
                                                    class="btn border border-danger rounded-pill px-3 text-danger">
                                                    <i class="fas fa-calendar-alt me-2 text-danger"></i>
                                                    Sử dụng
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-md-6">
                                    <div class="border border-danger rounded position-relative overflow-hidden">
                                        <div class="vesitable-img">
                                            <img src="/client/img/timetable.png" class="img-fluid w-100 rounded-top"
                                                alt="Giáo vụ">
                                        </div>
                                        <div class="text-white bg-danger px-3 py-1 rounded position-absolute"
                                            style="top: 10px; right: 10px;">Tất cả</div>
                                        <div class="p-4 pb-3 rounded-bottom d-flex flex-column">
                                            <h4 class="text-danger mb-3">Xem thời khóa biểu</h4>
                                            <p class="mb-4">Xem thời khóa biểu chi tiết đã được giáo vụ tạo tự động bằng
                                                hệ thống </p>
                                            <div class="mt-4">
                                                <a href="/staff/timetable/view"
                                                    class="btn border border-danger rounded-pill px-3 text-danger">
                                                    <i class="fas fa-calendar-alt me-2 text-danger"></i>
                                                    Xem ngay
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-md-6">
                                    <div class="border border-danger rounded position-relative overflow-hidden">
                                        <div class="vesitable-img">
                                            <img src="/client/img/semester.png" class="img-fluid w-100 rounded-top"
                                                alt="Giáo vụ">
                                        </div>
                                        <div class="text-white bg-danger px-3 py-1 rounded position-absolute"
                                            style="top: 10px; right: 10px;">Giáo vụ</div>
                                        <div class="p-4 pb-3 rounded-bottom d-flex flex-column">
                                            <h4 class="text-danger mb-3">Thiết lập học kì</h4>
                                            <p class="mb-4">Thiết lập học kì cho thao tác đăng ký học phần và thời gian
                                                đăng ký học phần </p>
                                            <div class="mt-4">
                                                <a href="/staff/semester-config"
                                                    class="btn border border-danger rounded-pill px-3 text-danger">
                                                    <i class="fas fa-calendar-alt me-2 text-danger"></i>
                                                    Thiết lập
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-md-6">
                                    <div class="border border-danger rounded position-relative overflow-hidden">
                                        <div class="vesitable-img">
                                            <img src="/client/img/subcribe_course.png"
                                                class="img-fluid w-100 rounded-top" alt="Sinh viên">
                                        </div>
                                        <div class="text-white bg-danger px-3 py-1 rounded position-absolute"
                                            style="top: 10px; right: 10px;">Sinh viên</div>
                                        <div class="p-4 pb-3 rounded-bottom d-flex flex-column">
                                            <h4 class="text-danger mb-3">Đăng ký học tập</h4>
                                            <p class="mb-4">Đăng ký học phần cho sinh viên để nắm bắt nguyện vọng
                                                học tập của sinh viên</p>
                                            <div class="mt-4">
                                                <a href="/student/course"
                                                    class="btn border border-danger rounded-pill px-3 text-danger">
                                                    <i class="fas fa-book me-2 text-danger"></i>
                                                    Đăng ký
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-md-6">
                                    <div class="border border-danger rounded position-relative overflow-hidden">
                                        <div class="vesitable-img">
                                            <img src="/client/img/course_list.png" class="img-fluid w-100 rounded-top"
                                                alt="Sinh viên">
                                        </div>
                                        <div class="text-white bg-danger px-3 py-1 rounded position-absolute"
                                            style="top: 10px; right: 10px;">Tất cả</div>
                                        <div class="p-4 pb-3 rounded-bottom d-flex flex-column">
                                            <h4 class="text-danger mb-3">Thông tin môn học</h4>
                                            <p class="mb-4">Xem thông tin chi tiết của môn học theo từng đơn vị hoặc
                                                từng năm học</p>
                                            <div class="mt-4">
                                                <a href="/course"
                                                    class="btn border border-danger rounded-pill px-3 text-danger">
                                                    <i class="fas fa-book me-2 text-danger"></i>
                                                    Xem ngay
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-md-6">
                                    <div class="border border-danger rounded position-relative overflow-hidden">
                                        <div class="vesitable-img">
                                            <img src="/client/img/banner_hust_demo.png"
                                                class="img-fluid w-100 rounded-top" alt="Lớp học">
                                        </div>
                                        <div class="text-white bg-danger px-3 py-1 rounded position-absolute"
                                            style="top: 10px; right: 10px;">Tất cả</div>
                                        <div class="p-4 pb-3 rounded-bottom d-flex flex-column">
                                            <h4 class="text-danger mb-3">Thông tin lớp học</h4>
                                            <p class="mb-4">Xem thông tin chi tiết về lớp học, giảng viên, phòng học và
                                                lịch học. Tra cứu dễ dàng thông tin cần thiết.</p>
                                            <div class="mt-4">
                                                <a href="/classroom"
                                                    class="btn border border-danger rounded-pill px-3 text-danger">
                                                    <i class="fas fa-info-circle me-2 text-danger"></i>
                                                    Xem chi tiết
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Time Table End -->
                    <!-- Footer Start -->
                    <jsp:include page="../layout/footer.jsp" />
                    <!-- Footer End -->
                    <!-- Back to Top -->
                    <a href="#" class="btn btn-danger border-3 border-danger text-white rounded-circle back-to-top"><i
                            class="fa fa-arrow-up"></i></a>


                    <!-- JavaScript Libraries -->
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script src="/client/lib/easing/easing.min.js"></script>
                    <script src="/client/lib/waypoints/waypoints.min.js"></script>
                    <script src="/client/lib/lightbox/js/lightbox.min.js"></script>
                    <script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>

                    <!-- Template Javascript -->
                    <script src="/client/js/main.js"></script>
                </body>

                </html>