<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8">
                    <title>Lập lịch học</title>
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
                    <jsp:include page="../../client/layout/header.jsp" />

                    <div class="container-fluid page-header py-5 justify-content-center">
                        <h1 class="text-center text-white display-6">Lập lịch học</h1>
                        <h5 class="text-white text-center">Tạo thời khóa biểu cho các môn học đã chọn</h5>
                    </div>

                    <!-- Spinner Start -->
                    <div id="spinner"
                        class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50 d-flex align-items-center justify-content-center">
                        <div class="spinner-grow text-danger" role="status"></div>
                    </div>
                    <!-- Spinner End -->

                    <!-- Course List Start -->
                    <div class="container-fluid py-5">
                        <div class="container py-5">
                            <div class="row">
                                <div class="col-12">
                                    <!-- Back Button -->
                                    <div class="mb-4">
                                        <a href="/staff/timetable/course" class="btn btn-outline-secondary">
                                            <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách môn học
                                        </a>
                                    </div>

                                    <!-- Summary Card -->
                                    <div class="card mb-4">
                                        <div class="card-header bg-success text-white">
                                            <h5 class="mb-0 text-white">
                                                <i class="fas fa-calendar-plus me-2"></i>Thông tin lập lịch
                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <div class="d-flex align-items-center">
                                                        <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 50px; height: 50px;">
                                                            <i class="fas fa-book fa-lg"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="text-muted mb-0">Số môn học</h6>
                                                            <h3 class="mb-0 text-primary">${courseCount}</h3>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-4">
                                                    <div class="d-flex align-items-center">
                                                        <div class="bg-info text-white rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 50px; height: 50px;">
                                                            <i class="fas fa-calendar fa-lg"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="text-muted mb-0">Học kỳ</h6>
                                                            <h5 class="mb-0 text-info">${currentSemester}</h5>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-4">
                                                    <div class="d-flex align-items-center">
                                                        <div class="bg-warning text-white rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 50px; height: 50px;">
                                                            <i class="fas fa-users fa-lg"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="text-muted mb-0">Tổng sinh viên</h6>
                                                            <h3 class="mb-0 text-warning">${totalOrderDetails}</h3>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Selected Courses Table -->
                                    <div class="card">
                                        <div class="card-header bg-light">
                                            <h5 class="mb-0">
                                                <i class="fas fa-list me-2"></i>Danh sách môn học đã chọn
                                            </h5>
                                        </div>
                                        <div class="card-body p-0">
                                            <div class="table-responsive">
                                                <table class="table table-hover mb-0">
                                                    <thead class="table-success">
                                                        <tr class="align-middle" style="height: 60px;">
                                                            <th width="8%" class="text-center fs-5 fw-bold">STT</th>
                                                            <th width="12%" class="text-center fs-5 fw-bold">Mã môn</th>
                                                            <th width="30%" class="text-center fs-5 fw-bold">Tên môn học
                                                            </th>
                                                            <th width="8%" class="text-center fs-5 fw-bold">Tín chỉ</th>
                                                            <th width="12%" class="text-center fs-5 fw-bold">Đơn vị</th>
                                                            <th width="15%" class="text-center fs-5 fw-bold">Số đăng ký
                                                            </th>
                                                            <th width="15%" class="text-center fs-5 fw-bold">Trạng thái
                                                            </th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach items="${selectedCourses}" var="course"
                                                            varStatus="status">
                                                            <tr class="align-middle" style="height: 70px;">
                                                                <td class="text-center">
                                                                    <h4 class="mb-0 fw-bold text-dark">${status.index +
                                                                        1}</h4>
                                                                </td>
                                                                <td class="text-center">
                                                                    <code
                                                                        class="bg-light text-dark px-3 py-2 fw-bold fs-6 rounded">${course.code}</code>
                                                                </td>
                                                                <td class="text-start px-3">
                                                                    <h6 class="mb-1 fw-bold text-dark">${course.name}
                                                                    </h6>
                                                                    <small
                                                                        class="text-muted">${course.description}</small>
                                                                </td>
                                                                <td class="text-center">
                                                                    <h5 class="mb-0 fw-bold text-primary">
                                                                        ${course.credit}</h5>
                                                                </td>
                                                                <td class="text-center">
                                                                    <h6 class="mb-0 fw-bold text-success">
                                                                        ${course.collegeName}</h6>
                                                                </td>
                                                                <td class="text-center">
                                                                    <div class="d-flex flex-column align-items-center">
                                                                        <h4 class="mb-1 fw-bold text-primary">
                                                                            ${course.totalOrderDetailCount}</h4>
                                                                        <small class="text-muted fw-bold">sinh
                                                                            viên</small>
                                                                    </div>
                                                                </td>
                                                                <td class="text-center">
                                                                    <c:choose>
                                                                        <c:when
                                                                            test="${course.totalOrderDetailCount >= 30}">
                                                                            <span
                                                                                class="badge bg-success fs-6 px-3 py-2 fw-bold text-white">Đầy</span>
                                                                        </c:when>
                                                                        <c:when
                                                                            test="${course.totalOrderDetailCount >= 15}">
                                                                            <span
                                                                                class="badge bg-warning text-dark fs-6 px-3 py-2 fw-bold">Vừa</span>
                                                                        </c:when>
                                                                        <c:when
                                                                            test="${course.totalOrderDetailCount > 0}">
                                                                            <span
                                                                                class="badge bg-info fs-6 px-3 py-2 fw-bold text-white">Ít</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span
                                                                                class="badge bg-light text-dark fs-6 px-3 py-2 fw-bold border">Trống</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Action Buttons -->
                                    <div class="row mt-4">
                                        <div class="col-12 text-center">
                                            <button class="btn btn-success btn-lg me-3"
                                                onclick="startScheduleCreation()">
                                                <i class="fas fa-play me-2"></i>Bắt đầu lập lịch
                                            </button>
                                            <a href="/staff/timetable/course" class="btn btn-outline-secondary btn-lg">
                                                <i class="fas fa-times me-2"></i>Hủy bỏ
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Course List End -->

                    <!-- JavaScript Libraries -->
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script src="/client/lib/easing/easing.min.js"></script>
                    <script src="/client/lib/waypoints/waypoints.min.js"></script>
                    <script src="/client/lib/lightbox/js/lightbox.min.js"></script>
                    <script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>

                    <!-- Template Javascript -->
                    <script src="/client/js/main.js"></script>

                    <script>
                        $(document).ready(function () {
                            // Hide spinner when page is loaded
                            $('#spinner').removeClass('show');

                            // Add loading spinner during navigation
                            const links = document.querySelectorAll('a[href*="/staff/"]');
                            links.forEach(link => {
                                link.addEventListener('click', function () {
                                    document.getElementById('spinner').classList.add('show');
                                });
                            });
                        });

                        function startScheduleCreation() {
                            // Show confirmation dialog
                            if (confirm('Bạn có chắc chắn muốn bắt đầu lập lịch cho ${courseCount} môn học đã chọn?')) {
                                // Show loading spinner
                                document.getElementById('spinner').classList.add('show');

                                // Here you can add the actual schedule creation logic
                                alert('Chức năng lập lịch sẽ được phát triển trong phiên bản tiếp theo!');

                                // Hide spinner
                                document.getElementById('spinner').classList.remove('show');
                            }
                        }
                    </script>
                </body>

                </html>