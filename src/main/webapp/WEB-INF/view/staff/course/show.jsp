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
                    <jsp:include page="../../client/layout/header.jsp" />

                    <div class="container-fluid page-header py-5 justify-content-center">
                        <h1 class="text-center text-white display-6"></h1>
                        <h5 class="text-white text-center">Bạn có thể xem chi tiết số sinh viên đăng ký các môn và chọn
                            môn học theo nhu cầu sinh viên để lập lịch</h5>
                    </div>
                    <!-- Spinner Start -->
                    <div id="spinner"
                        class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
                        <div class="spinner-grow text-danger" role="status"></div>
                    </div>
                    <!-- Spinner End --> <!--Course List Start-->
                    <div class="container-fluid py-5">
                        <div class="container py-5">
                            <div class="row">
                                <div class="col-12">
                                    <!-- Search and Filter Section -->
                                    <div class="card mb-4">
                                        <div class="card-header bg-danger text-white">
                                            <h5 class="mb-0 text-white"><i class="fas fa-search me-2"></i>Tìm kiếm và
                                                lọc môn học
                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <form method="GET" action="/staff/timetable/course">
                                                <div class="row">
                                                    <div class="col-md-2">
                                                        <label class="form-label">Năm học:</label>
                                                        <select class="form-select" name="yearLevel">
                                                            <option value="">Tất cả năm</option>
                                                            <option value="1" ${param.yearLevel eq '1' ? 'selected' : ''
                                                                }>
                                                                Năm 1
                                                            </option>
                                                            <option value="2" ${param.yearLevel eq '2' ? 'selected' : ''
                                                                }>
                                                                Năm 2
                                                            </option>
                                                            <option value="3" ${param.yearLevel eq '3' ? 'selected' : ''
                                                                }>
                                                                Năm 3
                                                            </option>
                                                            <option value="4" ${param.yearLevel eq '4' ? 'selected' : ''
                                                                }>
                                                                Năm 4
                                                            </option>
                                                        </select>
                                                    </div>
                                                    <div class="col-md-2">
                                                        <label class="form-label">Đơn vị</label>
                                                        <select class="form-select" name="college">
                                                            <option value="">Tất cả</option>
                                                            <option value="BGDTC" ${param.college=='BGDTC' ? 'selected'
                                                                : '' }>BGDTC
                                                            </option>
                                                            <option value="KGDQP" ${param.college=='KGDQP' ? 'selected'
                                                                : '' }>KGDQP
                                                            </option>
                                                            <option value="KKTVQL" ${param.college=='KKTVQL'
                                                                ? 'selected' : '' }>KKTVQL
                                                            </option>
                                                            <option value="KML" ${param.college=='KML' ? 'selected' : ''
                                                                }>KML</option>
                                                            <option value="KNN" ${param.college=='KNN' ? 'selected' : ''
                                                                }>KNN</option>
                                                            <option value="KSPKT" ${param.college=='KSPKT' ? 'selected'
                                                                : '' }>KSPKT
                                                            </option>
                                                            <option value="KTTD" ${param.college=='KTTD' ? 'selected'
                                                                : '' }>KTTD
                                                            </option>
                                                            <option value="TCK" ${param.college=='TCK' ? 'selected' : ''
                                                                }>TCK</option>
                                                            <option value="TCNTT" ${param.college=='TCNTT' ? 'selected'
                                                                : '' }>TCNTT
                                                            </option>
                                                            <option value="TDDT" ${param.college=='TDDT' ? 'selected'
                                                                : '' }>TDDT
                                                            </option>
                                                            <option value="THKHSS" ${param.college=='THKHSS'
                                                                ? 'selected' : '' }>THKHSS
                                                            </option>
                                                            <option value="TTNNHT" ${param.college=='TTNNHT'
                                                                ? 'selected' : '' }>TTNNHT
                                                            </option>
                                                            <option value="TVL" ${param.college=='TVL' ? 'selected' : ''
                                                                }>TVL</option>
                                                            <option value="VVLKT" ${param.college=='VVLKT' ? 'selected'
                                                                : '' }>VVLKT
                                                            </option>
                                                        </select>
                                                    </div>
                                                    <div class="col-md-2">
                                                        <label class="form-label">&nbsp;</label>
                                                        <div class="d-grid">
                                                            <button type="submit" class="btn btn-danger">
                                                                <i class="fas fa-search me-2"></i>Lọc
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>

                                    <!-- Statistics Summary -->
                                    <div class="row mb-4">
                                        <div class="col-md-3">
                                            <div class="card bg-primary text-white">
                                                <div class="card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <div>
                                                            <h6>Tổng số môn học</h6>
                                                            <h3>${allCourses}</h3>
                                                        </div>
                                                        <div class="align-self-center">
                                                            <i class="fas fa-book fa-2x"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card bg-success text-white">
                                                <div class="card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <div>
                                                            <h6>Môn có đăng ký</h6>
                                                            <h3>${totalCourses}</h3>
                                                        </div>
                                                        <div class="align-self-center">
                                                            <i class="fas fa-check-circle fa-2x"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card bg-warning text-white">
                                                <div class="card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <div>
                                                            <h6>Tổng đăng ký</h6>
                                                            <h3>${totalOrderDetails}</h3>
                                                        </div>
                                                        <div class="align-self-center">
                                                            <i class="fas fa-users fa-2x"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="card bg-info text-white">
                                                <div class="card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <div>
                                                            <h6>Học kỳ hiện tại</h6>
                                                            <h3>${currentSemester}</h3>
                                                        </div>
                                                        <div class="align-self-center">
                                                            <i class="fas fa-calendar fa-2x"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Year Level Stats (hiển thị khi có filter) -->
                                    <c:if test="${not empty yearLevelStats}">
                                        <div class="row mb-4">
                                            <div class="col-12">
                                                <div class="card bg-light">
                                                    <div class="card-body">
                                                        <h6 class="card-title">
                                                            <i class="fas fa-chart-bar me-2"></i>Thống kê theo năm học
                                                        </h6>
                                                        <div class="d-flex flex-wrap">
                                                            <c:forEach items="${yearLevelStats}" var="stat">
                                                                <span class="badge bg-info me-2 mb-2 p-2">
                                                                    Năm ${stat.key}: ${stat.value} đăng ký
                                                                </span>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if> <!-- Course List Table -->
                                    <div class="card">
                                        <div class="card-header bg-light">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <h5 class="mb-0"><i class="fas fa-list me-2"></i>Danh sách môn học và số
                                                    lượng đăng ký</h5>
                                                <div class="d-flex gap-2">
                                                    <button id="createScheduleBtn" class="btn btn-success btn-sm"
                                                        style="display: none;">
                                                        <i class="fas fa-calendar-plus me-1"></i>Sang trang lập lịch
                                                        (<span id="selectedCount">0</span>)
                                                    </button>
                                                    <button class="btn btn-outline-danger btn-sm"
                                                        onclick="refreshData()">
                                                        <i class="fas fa-sync-alt me-1"></i>Làm mới
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-body p-0">
                                            <form id="scheduleForm" action="/staff/timetable/create" method="GET">
                                                <div class="table-responsive">
                                                    <table class="table table-hover mb-0">
                                                        <thead class="table-danger" style="height: 60px;">
                                                            <tr class="align-middle">
                                                                <th width="5%" class="text-center fs-5 fw-bold">
                                                                    <input type="checkbox" id="selectAll"
                                                                        class="form-check-input fs-5">
                                                                </th>
                                                                <th width="7%" class="text-center fs-5 fw-bold">STT</th>
                                                                <th width="12%" class="text-center">
                                                                    <a href="?sortBy=code&sortDir=${sortBy == 'code' && sortDir == 'asc' ? 'desc' : 'asc'}&yearLevel=${param.yearLevel}&college=${param.college}"
                                                                        class="text-white text-decoration-none fs-5 fw-bold">
                                                                        Mã môn ${sortBy == 'code' ? (sortDir == 'asc' ?
                                                                        '↑'
                                                                        : '↓') : ''}
                                                                    </a>
                                                                </th>
                                                                <th width="25%" class="text-center">
                                                                    <a href="?sortBy=name&sortDir=${sortBy == 'name' && sortDir == 'asc' ? 'desc' : 'asc'}&yearLevel=${param.yearLevel}&college=${param.college}"
                                                                        class="text-white text-decoration-none fs-5 fw-bold">
                                                                        Tên môn học ${sortBy == 'name' ? (sortDir ==
                                                                        'asc' ?
                                                                        '↑' : '↓') : ''}
                                                                    </a>
                                                                </th>
                                                                <th width="8%" class="text-center fs-5 fw-bold">Tín chỉ
                                                                </th>
                                                                <th width="13%" class="text-center fs-5 fw-bold">Đơn vị
                                                                </th>
                                                                <th width="15%" class="text-center">
                                                                    <a href="?sortBy=orderDetailCount&sortDir=${sortBy == 'orderDetailCount' && sortDir == 'asc' ? 'desc' : 'asc'}&yearLevel=${param.yearLevel}&college=${param.college}&courseName=${param.courseName}&courseCode=${param.courseCode}"
                                                                        class="text-white text-decoration-none fs-5 fw-bold">
                                                                        Số đăng ký ${sortBy == 'orderDetailCount' ?
                                                                        (sortDir
                                                                        == 'asc' ? '↑' : '↓') : ''}
                                                                    </a>
                                                                </th>
                                                                <th width="10%" class="text-center fs-5 fw-bold">Trạng
                                                                    thái
                                                                </th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach items="${courses}" var="course"
                                                                varStatus="status">
                                                                <tr class="align-middle" style="height: 80px;">
                                                                    <td class="text-center">
                                                                        <input type="checkbox" name="courseCodes"
                                                                            value="${course.code}"
                                                                            class="form-check-input fs-5 course-checkbox">
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <h4 class="mb-0 fw-bold text-dark">
                                                                            ${status.index +
                                                                            1}</h4>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <code
                                                                            class="bg-light text-dark px-4 py-3 fw-bold fs-5 rounded">${course.code}</code>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <h5 class="mb-2 fw-bold text-dark">
                                                                            ${course.name}
                                                                        </h5>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <h4 class="mb-0 fw-bold text-primary">
                                                                            ${course.credit}</h4>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <h5 class="mb-0 fw-bold text-success">
                                                                            ${course.collegeName}</h5>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <c:choose>
                                                                            <c:when
                                                                                test="${course.orderDetailCount > 0}">
                                                                                <div
                                                                                    class="d-flex flex-column align-items-center">
                                                                                    <h3
                                                                                        class="mb-1 fw-bold text-primary">
                                                                                        ${course.orderDetailCount}</h3>
                                                                                    <h6 class="text-muted fw-bold mb-1">
                                                                                        /
                                                                                        ${course.totalOrderDetailCount}
                                                                                    </h6>
                                                                                    <small
                                                                                        class="text-muted fw-bold">(lọc
                                                                                        /
                                                                                        tổng)</small>
                                                                                </div>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <h3 class="mb-0 fw-bold text-muted">0
                                                                                </h3>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td class="text-center">
                                                                        <c:choose>
                                                                            <c:when
                                                                                test="${course.orderDetailCount >= 30}">
                                                                                <span
                                                                                    class="badge bg-success fs-5 px-4 py-3 fw-bold text-white">Đầy</span>
                                                                            </c:when>
                                                                            <c:when
                                                                                test="${course.orderDetailCount >= 15}">
                                                                                <span
                                                                                    class="badge bg-warning text-dark fs-5 px-4 py-3 fw-bold">Vừa</span>
                                                                            </c:when>
                                                                            <c:when
                                                                                test="${course.orderDetailCount > 0}">
                                                                                <span
                                                                                    class="badge bg-info fs-5 px-4 py-3 fw-bold text-white">Ít</span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span
                                                                                    class="badge bg-light text-dark fs-5 px-4 py-3 fw-bold border">Trống</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>

                                                            <c:if test="${empty courses}">
                                                                <tr>
                                                                    <td colspan="8" class="text-center py-4">
                                                                        <i
                                                                            class="fas fa-search fa-3x text-muted mb-3"></i>
                                                                        <h5 class="text-muted">Không tìm thấy môn học
                                                                            nào
                                                                        </h5>
                                                                        <p class="text-muted">Hãy thử thay đổi từ khóa
                                                                            tìm
                                                                            kiếm</p>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </form>
                                        </div>

                                        <!-- Pagination -->
                                        <c:if test="${totalPages > 1}">
                                            <div class="card-footer">
                                                <nav aria-label="Course pagination">
                                                    <ul class="pagination justify-content-center mb-0">
                                                        <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                                                            <a class="page-link"
                                                                href="?page=${currentPage - 1}&sortBy=${sortBy}&sortDir=${sortDir}&yearLevel=${param.yearLevel}&college=${param.college}&courseName=${param.courseName}&courseCode=${param.courseCode}">
                                                                <i class="fas fa-chevron-left"></i> Trước
                                                            </a>
                                                        </li>

                                                        <c:forEach begin="0" end="${totalPages - 1}" var="pageNum">
                                                            <c:if
                                                                test="${pageNum >= currentPage - 2 && pageNum <= currentPage + 2}">
                                                                <li
                                                                    class="page-item ${pageNum == currentPage ? 'active' : ''}">
                                                                    <a class="page-link"
                                                                        href="?page=${pageNum}&sortBy=${sortBy}&sortDir=${sortDir}&yearLevel=${param.yearLevel}&college=${param.college}&courseName=${param.courseName}&courseCode=${param.courseCode}">
                                                                        ${pageNum + 1}
                                                                    </a>
                                                                </li>
                                                            </c:if>
                                                        </c:forEach>

                                                        <li
                                                            class="page-item ${currentPage == totalPages - 1 ? 'disabled' : ''}">
                                                            <a class="page-link"
                                                                href="?page=${currentPage + 1}&sortBy=${sortBy}&sortDir=${sortDir}&yearLevel=${param.yearLevel}&college=${param.college}&courseName=${param.courseName}&courseCode=${param.courseCode}">
                                                                Sau <i class="fas fa-chevron-right"></i>
                                                            </a>
                                                        </li>
                                                    </ul>
                                                </nav>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--Course List End-->
                    <!-- Footer Start -->
                    <jsp:include page="../../client/layout/footer.jsp" />

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
                    <script>                        // Function to create schedule for course
                        function createSchedule(courseId) {
                            // You can implement this to redirect to schedule creation page
                            window.location.href = '/staff/timetable/create?courseId=' + courseId;
                        }

                        // Function to export data to Excel
                        function exportToExcel() {
                            const params = new URLSearchParams(window.location.search);
                            params.set('export', 'excel');
                            window.location.href = '/staff/course/export?' + params.toString();
                        }

                        // Function to refresh data
                        function refreshData() {
                            window.location.reload();
                        }                        // Add loading spinner during navigation and event listeners
                        document.addEventListener('DOMContentLoaded', function () {
                            // Add event listeners for create schedule buttons
                            document.querySelectorAll('.create-schedule-btn').forEach(button => {
                                button.addEventListener('click', function () {
                                    const courseId = this.getAttribute('data-course-id');
                                    createSchedule(courseId);
                                });
                            });

                            // Add loading spinner during navigation
                            const links = document.querySelectorAll('a[href*="/staff/"]');
                            links.forEach(link => {
                                link.addEventListener('click', function () {
                                    document.getElementById('spinner').classList.add('show');
                                });
                            });

                            // Course selection functionality
                            const selectAllCheckbox = document.getElementById('selectAll');
                            const courseCheckboxes = document.querySelectorAll('.course-checkbox');
                            const createScheduleBtn = document.getElementById('createScheduleBtn');
                            const selectedCount = document.getElementById('selectedCount');

                            // Select/deselect all functionality
                            selectAllCheckbox.addEventListener('change', function () {
                                courseCheckboxes.forEach(checkbox => {
                                    checkbox.checked = this.checked;
                                });
                                updateSelectedCount();
                            });

                            // Individual checkbox functionality
                            courseCheckboxes.forEach(checkbox => {
                                checkbox.addEventListener('change', function () {
                                    updateSelectedCount();
                                    // Update select all checkbox
                                    const checkedCount = document.querySelectorAll('.course-checkbox:checked').length;
                                    selectAllCheckbox.checked = checkedCount === courseCheckboxes.length;
                                    selectAllCheckbox.indeterminate = checkedCount > 0 && checkedCount < courseCheckboxes.length;
                                });
                            });

                            // Update selected count and button visibility
                            function updateSelectedCount() {
                                const checkedCount = document.querySelectorAll('.course-checkbox:checked').length;
                                selectedCount.textContent = checkedCount;

                                if (checkedCount > 0) {
                                    createScheduleBtn.style.display = 'inline-block';
                                } else {
                                    createScheduleBtn.style.display = 'none';
                                }
                            }

                            // Handle create schedule button click
                            createScheduleBtn.addEventListener('click', function () {
                                const checkedBoxes = document.querySelectorAll('.course-checkbox:checked');
                                if (checkedBoxes.length === 0) {
                                    alert('Vui lòng chọn ít nhất một môn học!');
                                    return;
                                }

                                // Submit form
                                document.getElementById('scheduleForm').submit();
                            });
                        });
                    </script>
                </body>

                </html>