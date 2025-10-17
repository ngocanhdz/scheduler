<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

                    <!DOCTYPE html>
                    <html lang="en">

                    <head>
                        <meta charset="utf-8">
                        <title>Đăng ký học tập</title>
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
                        <link href="lib/lightbox/css/lightbox.min.css" rel="stylesheet">
                        <link href="lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">


                        <!-- Customized Bootstrap Stylesheet -->
                        <link href="/client/css/bootstrap.min.css" rel="stylesheet">

                        <!-- Template Stylesheet -->
                        <link href="/client/css/style.css" rel="stylesheet">

                        <style>
                            .table a {
                                color: #333333;
                                text-decoration: none;
                            }

                            .table a:hover {
                                color: #666666;
                            }
                        </style>
                    </head>

                    <body>
                        <jsp:include page="../../client/layout/header.jsp" />
                        <div class="container-fluid page-header py-5 justify-content-center">
                            <h1 class="text-center text-white display-6"></h1>
                            <h5 class="text-white text-center">Theo thông tin từ giáo vụ hiện bạn chỉ có thể đăng ký học
                                phần cho kì ${semesterConfig.name} trong thời gian từ ${semesterConfig.start} đến
                                ${semesterConfig.end}</h5>
                        </div>

                        <!-- Cart Page Start -->
                        <div class="container-fluid py-5">
                            <div class="container py-5">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th scope="col">Mã</th>
                                                <th scope="col">Tên</th>
                                                <th scope="col">Đơn vị</th>
                                                <th scope="col">Số tín</th>
                                                <th scope="col">Thao tác</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <c:forEach var="cartDetail" items="${cartDetails}">
                                                <tr>
                                                    <th scope="row">
                                                        <p class="mb-0 mt-4 text-danger">
                                                            <a href="/course/${cartDetail.course.id}"
                                                                target="_blank">${cartDetail.course.code}</a>
                                                        </p>
                                                    </th>
                                                    <td>
                                                        <p class="mb-0 mt-4">
                                                            <a href="/course/${cartDetail.course.id}"
                                                                target="_blank">${cartDetail.course.name}</a>
                                                        </p>
                                                    </td>
                                                    <td>

                                                        <p class="mb-0 mt-4">
                                                            ${cartDetail.course.college.name}
                                                        </p>
                                                    </td>
                                                    <td>
                                                        <p class="mb-0 mt-4">${cartDetail.course.credit}
                                                        </p>
                                                    </td>
                                                    <td>
                                                        <form action="/student/delete-cart-course/${cartDetail.id}"
                                                            method="post">
                                                            <input type="hidden" name="${_csrf.parameterName}"
                                                                value="${_csrf.token}" />
                                                            <button
                                                                class="btn btn-md rounded-circle bg-light border mt-4 "
                                                                type="submit">

                                                                <i class="fa fa-times text-danger"></i>
                                                            </button>
                                                        </form>

                                                    </td>

                                                </tr>
                                            </c:forEach>

                                        </tbody>
                                    </table>
                                </div>
                                <c:if test="${cartDetails.size() != 0}">
                                    <form:form action="/student/place-order" method="post">
                                        <div class="row g-4 justify-content-start">
                                            <div class="col-6">
                                                <div class="bg-light rounded">
                                                    <div class="p-4">
                                                        <h1 class="display-6 mb-4">Thông tin <span
                                                                class="fw-normal">Đăng ký</span>
                                                        </h1>
                                                        <div class="d-flex justify-content-between mb-4">
                                                            <h5 class="mb-0 me-4">Tổng số tín:</h5>
                                                            <p class="mb-0">
                                                                <fmt:formatNumber type="number"
                                                                    value="${sessionScope.sumCredit}" />
                                                            </p>
                                                        </div>
                                                        <div class="d-flex justify-content-between mb-4">
                                                            <h5 class="mb-0 me-4">Học phí dự tính:</h5>
                                                            <p class="mb-0">
                                                                chưa có thông tin học phí
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <button
                                            class="btn border-secondary rounded-pill px-4 py-3 text-secondary text-uppercase mb-4 ms-4"
                                            type="submit">Gửi đăng ký</button>
                                    </form:form>

                                </c:if>
                                <c:if test="${cartDetails.size() == 0}">Không có học phần nào được chọn</c:if>
                            </div>
                        </div>
                        <!-- Cart Page End -->
                        <!-- Order Page Start  -->
                        <!-- Order Page Start -->
                        <div class="container-fluid py-5 bg-light">
                            <div class="container py-5">
                                <h2 class="display-6 mb-4">Danh sách học phần đã đăng ký</h2>

                                <c:if test="${not empty orderDetails}">
                                    <div class="table-responsive">
                                        <table class="table table-hover">
                                            <thead>
                                                <tr>
                                                    <th scope="col">Mã học phần</th>
                                                    <th scope="col">Tên học phần</th>
                                                    <th scope="col">Đơn vị</th>
                                                    <th scope="col">Số tín chỉ</th>
                                                    <th scope="col">Học kỳ</th>
                                                    <th scope="col">Trạng thái</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="orderDetail" items="${orderDetails}">
                                                    <tr>
                                                        <td>
                                                            <p class="mb-0 text-danger">
                                                                <a href="/course/${orderDetail.course.id}"
                                                                    target="_blank">
                                                                    ${orderDetail.course.code}
                                                                </a>
                                                            </p>
                                                        </td>
                                                        <td>
                                                            <a href="/course/${orderDetail.course.id}" target="_blank">
                                                                ${orderDetail.course.name}
                                                            </a>
                                                        </td>
                                                        <td>${orderDetail.course.college.name}</td>
                                                        <td>${orderDetail.course.credit}</td>
                                                        <td>${orderDetail.semesterConfig.name}</td>
                                                        <td>
                                                            <span class="badge bg-success">Đã đăng ký</span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                    <!-- Order Summary -->
                                    <div class="row mt-4">
                                        <div class="col-md-6">
                                            <div class="card">
                                                <div class="card-body">
                                                    <h5 class="card-title">Tổng kết đăng ký</h5>
                                                    <div class="d-flex justify-content-between mt-3">
                                                        <span>Tổng số học phần:</span>
                                                        <strong>${orderDetails.size()}</strong>
                                                    </div>
                                                    <div class="d-flex justify-content-between mt-2">
                                                        <span>Tổng số tín chỉ:</span>
                                                        <strong>
                                                            <c:set var="totalCredits" value="0" />
                                                            <c:forEach var="orderDetail" items="${orderDetails}">
                                                                <c:set var="totalCredits"
                                                                    value="${totalCredits + orderDetail.course.credit}" />
                                                            </c:forEach>
                                                            ${totalCredits}
                                                        </strong>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${empty orderDetails}">
                                    <div class="alert alert-info" role="alert">
                                        Bạn chưa đăng ký học phần nào cho học kỳ này.
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <!-- Order Page End -->

                        <!-- Order Page End -->
                        <jsp:include page="../../client/layout/footer.jsp" />



                        <!-- JavaScript Libraries -->
                        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
                        <script
                            src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
                        <script src="lib/easing/easing.min.js"></script>
                        <script src="lib/waypoints/waypoints.min.js"></script>
                        <script src="lib/lightbox/js/lightbox.min.js"></script>
                        <script src="lib/owlcarousel/owl.carousel.min.js"></script>

                        <!-- Template Javascript -->
                        <script src="/client/js/main.js"></script>
                    </body>

                    </html>