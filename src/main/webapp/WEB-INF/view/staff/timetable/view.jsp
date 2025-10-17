<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8">
                    <title>Xem thời khóa biểu</title>
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
                        <h1 class="text-center text-white display-6">Thời khóa biểu</h1>
                        <h5 class="text-white text-center">Xem và quản lý thời khóa biểu đã tạo</h5>
                    </div>

                    <!-- Spinner Start -->
                    <div id="spinner"
                        class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50 d-flex align-items-center justify-content-center">
                        <div class="spinner-grow text-danger" role="status"></div>
                    </div>
                    <!-- Spinner End -->

                    <!-- Timetable View Start -->
                    <div class="container-fluid py-5">
                        <div class="container py-5">
                            <div class="row">
                                <div class="col-12">
                                    <!-- Back and Action Buttons -->
                                    <div class="mb-4 d-flex justify-content-between">
                                        <a href="/staff/timetable/course" class="btn btn-outline-secondary">
                                            <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách môn học
                                        </a>
                                        <div>
                                            <a href="/staff/timetable/course" class="btn btn-success">
                                                <i class="fas fa-plus me-2"></i>Tạo lịch mới
                                            </a>
                                        </div>
                                    </div>

                                    <!-- Summary Card -->
                                    <div class="card mb-4">
                                        <div class="card-header bg-info text-white">
                                            <h5 class="mb-0 text-white">
                                                <i class="fas fa-calendar-check me-2"></i>Thống kê thời khóa biểu -
                                                ${currentSemester.name}
                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="d-flex align-items-center">
                                                        <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 50px; height: 50px;">
                                                            <i class="fas fa-book fa-lg"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="text-muted mb-0">Tổng môn học</h6>
                                                            <h3 class="mb-0 text-primary">${totalCourses}</h3>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="d-flex align-items-center">
                                                        <div class="bg-success text-white rounded-circle d-flex align-items-center justify-content-center me-3"
                                                            style="width: 50px; height: 50px;">
                                                            <i class="fas fa-chalkboard-teacher fa-lg"></i>
                                                        </div>
                                                        <div>
                                                            <h6 class="text-muted mb-0">Tổng lớp học</h6>
                                                            <h3 class="mb-0 text-success">${totalClasses}</h3>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Error Message -->
                                    <c:if test="${not empty error}">
                                        <div class="alert alert-warning">
                                            <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                        </div>
                                    </c:if>

                                    <!-- Timetable Content -->
                                    <c:choose>
                                        <c:when test="${empty timetablesByCode}">
                                            <!-- No Timetable -->
                                            <div class="card">
                                                <div class="card-body text-center py-5">
                                                    <i class="fas fa-calendar-times text-muted"
                                                        style="font-size: 4rem;"></i>
                                                    <h4 class="text-muted mt-3">Chưa có thời khóa biểu</h4>
                                                    <p class="text-muted">Bạn chưa tạo thời khóa biểu nào cho học kỳ
                                                        này.</p>
                                                    <a href="/staff/timetable/course" class="btn btn-success btn-lg">
                                                        <i class="fas fa-plus me-2"></i>Tạo thời khóa biểu mới
                                                    </a>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Timetable List -->
                                            <div class="card">
                                                <div class="card-header bg-light">
                                                    <h5 class="mb-0">
                                                        <i class="fas fa-table me-2"></i>Danh sách thời khóa biểu
                                                    </h5>
                                                </div>
                                                <div class="card-body p-0">
                                                    <div class="accordion" id="timetableAccordion">
                                                        <c:forEach items="${timetablesByCode}" var="entry"
                                                            varStatus="status">
                                                            <div class="accordion-item">
                                                                <h2 class="accordion-header"
                                                                    id="heading${status.index}">
                                                                    <button class="accordion-button" type="button"
                                                                        data-bs-toggle="collapse"
                                                                        data-bs-target="#collapse${status.index}"
                                                                        aria-expanded="true"
                                                                        aria-controls="collapse${status.index}">
                                                                        <div
                                                                            class="d-flex justify-content-between w-100 me-3">
                                                                            <div>
                                                                                <strong>${entry.key}</strong> -
                                                                                ${entry.value[0].course.name}
                                                                            </div>
                                                                            <div>
                                                                                <span
                                                                                    class="badge bg-primary">${entry.value.size()}
                                                                                    lớp</span>
                                                                            </div>
                                                                        </div>
                                                                    </button>
                                                                </h2>
                                                                <div id="collapse${status.index}"
                                                                    class="accordion-collapse collapse show"
                                                                    aria-labelledby="heading${status.index}"
                                                                    data-bs-parent="#timetableAccordion">
                                                                    <div class="accordion-body">
                                                                        <!-- Course Info -->
                                                                        <div class="row mb-3">
                                                                            <div class="col-md-8">
                                                                                <h6><strong>Thông tin môn học</strong>
                                                                                </h6>
                                                                                <p class="mb-1"><strong>Tên:</strong>
                                                                                    ${entry.value[0].course.name}</p>
                                                                                <p class="mb-1"><strong>Tín
                                                                                        chỉ:</strong>
                                                                                    ${entry.value[0].course.credit}</p>
                                                                                <p class="mb-1"><strong>Số
                                                                                        tiết:</strong>
                                                                                    ${entry.value[0].course.lectureHour}
                                                                                </p>
                                                                            </div>
                                                                            <div class="col-md-4 text-end">
                                                                                <button
                                                                                    class="btn btn-outline-danger btn-sm"
                                                                                    onclick="deleteTimetable('${entry.key}')">
                                                                                    <i class="fas fa-trash me-1"></i>Xóa
                                                                                    lịch
                                                                                </button>
                                                                            </div>
                                                                        </div>

                                                                        <!-- Class Schedule Table -->
                                                                        <div class="table-responsive">
                                                                            <table
                                                                                class="table table-striped table-hover">
                                                                                <thead class="table-dark">
                                                                                    <tr>
                                                                                        <th>Lớp học</th>
                                                                                        <th>Thứ</th>
                                                                                        <th>Tiết</th>
                                                                                        <th>Phòng học</th>
                                                                                        <th>Sức chứa</th>
                                                                                        <th>Trạng thái</th>
                                                                                    </tr>
                                                                                </thead>
                                                                                <tbody>
                                                                                    <c:forEach items="${entry.value}"
                                                                                        var="timetable"
                                                                                        varStatus="classStatus">
                                                                                        <tr>
                                                                                            <td>
                                                                                                <span
                                                                                                    class="badge bg-secondary">Lớp
                                                                                                    ${classStatus.index
                                                                                                    + 1}</span>
                                                                                            </td>
                                                                                            <td>
                                                                                                <strong>${timetable.period.day.name}</strong>
                                                                                            </td>
                                                                                            <td>
                                                                                                <span
                                                                                                    class="badge bg-info">Tiết
                                                                                                    ${timetable.period.number}</span>
                                                                                            </td>
                                                                                            <td>
                                                                                                <strong>${timetable.classRoom.name}</strong>
                                                                                            </td>
                                                                                            <td>
                                                                                                <span
                                                                                                    class="text-muted">${timetable.classRoom.capacity}
                                                                                                    chỗ</span>
                                                                                            </td>
                                                                                            <td>
                                                                                                <c:choose>
                                                                                                    <c:when
                                                                                                        test="${timetable.status == 'active'}">
                                                                                                        <span
                                                                                                            class="badge bg-success">Hoạt
                                                                                                            động</span>
                                                                                                    </c:when>
                                                                                                    <c:when
                                                                                                        test="${timetable.status == 'pending'}">
                                                                                                        <span
                                                                                                            class="badge bg-warning">Chờ
                                                                                                            duyệt</span>
                                                                                                    </c:when>
                                                                                                    <c:otherwise>
                                                                                                        <span
                                                                                                            class="badge bg-danger">Đã
                                                                                                            hủy</span>
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
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Timetable View End -->

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
                        // Function to get cookie value
                        function getCookie(name) {
                            let cookieValue = null;
                            if (document.cookie && document.cookie !== '') {
                                const cookies = document.cookie.split(';');
                                for (let i = 0; i < cookies.length; i++) {
                                    const cookie = cookies[i].trim();
                                    if (cookie.substring(0, name.length + 1) === (name + '=')) {
                                        cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                                        break;
                                    }
                                }
                            }
                            return cookieValue;
                        }

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

                        function deleteTimetable(courseCode) {
                            if (confirm('Bạn có chắc chắn muốn xóa lịch học cho môn ' + courseCode + '?\n\nHành động này không thể hoàn tác.')) {                                // Show loading spinner
                                document.getElementById('spinner').classList.add('show');

                                // Get CSRF token from cookie
                                const csrfToken = getCookie('XSRF-TOKEN');

                                const headers = {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                };

                                // Add CSRF token to headers if available
                                if (csrfToken) {
                                    headers['X-XSRF-TOKEN'] = csrfToken;
                                }

                                // Call API to delete timetable  
                                fetch('/staff/timetable/delete', {
                                    method: 'POST',
                                    headers: headers,
                                    body: 'courseCode=' + encodeURIComponent(courseCode)
                                })
                                    .then(response => response.json())
                                    .then(data => {
                                        // Hide spinner
                                        document.getElementById('spinner').classList.remove('show');

                                        if (data.success) {
                                            alert(data.message);
                                            // Reload page to refresh data
                                            window.location.reload();
                                        } else {
                                            alert('Lỗi: ' + data.message);
                                        }
                                    })
                                    .catch(error => {
                                        // Hide spinner
                                        document.getElementById('spinner').classList.remove('show');
                                        console.error('Error:', error);
                                        alert('Có lỗi xảy ra khi kết nối với server. Vui lòng thử lại.');
                                    });
                            }
                        }
                    </script>
                </body>

                </html>