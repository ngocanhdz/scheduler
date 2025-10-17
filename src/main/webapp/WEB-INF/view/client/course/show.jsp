<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8">
                    <title>Danh sách môn học</title>
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
                    <div class="container-fluid page-header py-5 justify-content-center">
                        <h1 class="text-center text-white display-6"></h1>
                        <h5 class="text-white text-center">Bạn có thể tìm kiếm và xem thông tin chi tiết các môn học
                            toàn trường tại đây</h5>
                    </div>
                    <!-- Spinner Start -->
                    <div id="spinner"
                        class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
                        <div class="spinner-grow text-danger" role="status"></div>
                    </div>
                    <!-- Spinner End -->

                    <!--Course List Start-->
                    <div class="container mt-5">
                        <div class="row mb-4">
                            <div class="col-12">
                                <form action="/course" method="get" class="row g-3">
                                    <div class="col-md-2">
                                        <label class="form-label">Mã môn học</label>
                                        <input type="text" class="form-control" name="code" value="${param.code}"
                                            placeholder="Nhập mã môn học...">
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">Tên môn học</label>
                                        <input type="text" class="form-control" name="name" value="${param.name}"
                                            placeholder="Nhập tên môn học...">
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">Đơn vị</label>
                                        <select class="form-select" name="college">
                                            <option value="">Tất cả</option>
                                            <option value="BGDTC" ${param.college=='BGDTC' ? 'selected' : '' }>BGDTC
                                            </option>
                                            <option value="KGDQP" ${param.college=='KGDQP' ? 'selected' : '' }>KGDQP
                                            </option>
                                            <option value="KKTVQL" ${param.college=='KKTVQL' ? 'selected' : '' }>KKTVQL
                                            </option>
                                            <option value="KML" ${param.college=='KML' ? 'selected' : '' }>KML</option>
                                            <option value="KNN" ${param.college=='KNN' ? 'selected' : '' }>KNN</option>
                                            <option value="KSPKT" ${param.college=='KSPKT' ? 'selected' : '' }>KSPKT
                                            </option>
                                            <option value="KTTD" ${param.college=='KTTD' ? 'selected' : '' }>KTTD
                                            </option>
                                            <option value="TCK" ${param.college=='TCK' ? 'selected' : '' }>TCK</option>
                                            <option value="TCNTT" ${param.college=='TCNTT' ? 'selected' : '' }>TCNTT
                                            </option>
                                            <option value="TDDT" ${param.college=='TDDT' ? 'selected' : '' }>TDDT
                                            </option>
                                            <option value="THKHSS" ${param.college=='THKHSS' ? 'selected' : '' }>THKHSS
                                            </option>
                                            <option value="TTNNHT" ${param.college=='TTNNHT' ? 'selected' : '' }>TTNNHT
                                            </option>
                                            <option value="TVL" ${param.college=='TVL' ? 'selected' : '' }>TVL</option>
                                            <option value="VVLKT" ${param.college=='VVLKT' ? 'selected' : '' }>VVLKT
                                            </option>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">Số tín chỉ</label>
                                        <select class="form-select" name="credit">
                                            <option value="">Tất cả</option>
                                            <option value="1" ${param.credit=='1' ? 'selected' : '' }>1</option>
                                            <option value="2" ${param.credit=='2' ? 'selected' : '' }>2</option>
                                            <option value="3" ${param.credit=='3' ? 'selected' : '' }>3</option>
                                            <option value="4" ${param.credit=='4' ? 'selected' : '' }>4</option>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">Giờ lý thuyết</label>
                                        <select class="form-select" name="lectureHour">
                                            <option value="">Tất cả</option>
                                            <option value="2" ${param.lectureHour=='2' ? 'selected' : '' }>2</option>
                                            <option value="3" ${param.lectureHour=='3' ? 'selected' : '' }>3</option>
                                            <option value="4" ${param.lectureHour=='4' ? 'selected' : '' }>4</option>
                                            <option value="5" ${param.lectureHour=='5' ? 'selected' : '' }>5</option>
                                        </select>
                                    </div>
                                    <div class="col-md-2 d-flex align-items-end">
                                        <button type="submit" class="btn btn-danger w-100">
                                            <i class="fas fa-search"></i> Tìm kiếm
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12 mx-auto">
                                <table class="table table-bordered table-hover">
                                    <thead>
                                        <tr>
                                            <th scope="col">Mã học phần</th>
                                            <th scope="col">Tên</th>
                                            <th scope="col">Giờ lý thuyết</th>
                                            <th scope="col">Số tín</th>
                                            <th scope="col">Đơn vị</th>
                                            <th scope="col">Chi tiết</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="course" items="${courses}">
                                            <tr>
                                                <td>${course.code}</td>
                                                <td>${course.name}</td>
                                                <td>${course.lectureHour}</td>
                                                <td>${course.credit}</td>
                                                <td>${course.college.name}</td>
                                                <td>
                                                    <a href="/course/${course.id}" class="btn btn-outline-secondary">Chi
                                                        tiết</a>
                                                </td>

                                            </tr>
                                        </c:forEach>

                                    </tbody>
                                </table>
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