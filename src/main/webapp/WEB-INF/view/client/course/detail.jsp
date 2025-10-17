<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8">
                    <title>Chi tiết môn học</title>
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
                    <!--Course List Start-->
                    <div class="container mt-5" style="padding-top: 100px;">
                        <div class="row">
                            <div class="col-12 mx-auto">
                                <hr />
                                <div class="card w-75 border-dark">
                                    <div class="card-header">
                                        Name: ${course.name}
                                    </div>
                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item">Tên tiếng anh: ${course.englishName}</li>
                                        <li class="list-group-item">Mã học phần: ${course.code}</li>
                                        <li class="list-group-item">Mô tả: ${course.description}</li>
                                        <li class="list-group-item">Số giờ lý thuyết: ${course.lectureHour}</li>
                                        <li class="list-group-item">Số giờ thực hành: ${course.practicalHour}
                                        </li>
                                        <li class="list-group-item">Số tín chỉ: ${course.credit}</li>
                                        <li class="list-group-item">Đơn vị: ${course.college.name}</li>
                                    </ul>
                                </div>
                                <a href="/course" class="btn btn-primary mt-3">Back</a>
                            </div>
                        </div>
                    </div>
                    <!--Course List Start-->
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