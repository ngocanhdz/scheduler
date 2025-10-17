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
                    <meta name="_csrf" content="${_csrf.token}" />
                    <meta name="_csrf_header" content="${_csrf.headerName}" />

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

                            // Setup CSRF token for AJAX requests
                            const token = $("meta[name='_csrf']").attr("content");
                            const header = $("meta[name='_csrf_header']").attr("content");

                            if (token && header) {
                                $(document).ajaxSend(function (e, xhr, options) {
                                    xhr.setRequestHeader(header, token);
                                });
                            }
                        }); function startScheduleCreation() {
                            // Show confirmation dialog
                            if (confirm('Bạn có chắc chắn muốn bắt đầu lập lịch cho ${courseCount} môn học đã chọn?\n\nQuá trình này có thể mất vài phút.')) {
                                // Show loading spinner
                                document.getElementById('spinner').classList.add('show');

                                // Get course codes from server
                                const courseCodes = '${courseCodesStr}'.split(', ');                                // Get CSRF token from cookie
                                const csrfToken = getCookie('XSRF-TOKEN');
                                console.log('CSRF Token:', csrfToken);

                                // Call API to generate timetable with better error handling
                                const controller = new AbortController();
                                const timeoutId = setTimeout(() => controller.abort(), 120000); // 2 minutes timeout

                                const headers = {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                    'X-Requested-With': 'XMLHttpRequest'
                                };

                                // Add CSRF token to headers if available
                                if (csrfToken) {
                                    headers['X-XSRF-TOKEN'] = csrfToken;
                                }

                                // Also try with meta tag CSRF token
                                const metaCsrfToken = $("meta[name='_csrf']").attr("content");
                                const metaCsrfHeader = $("meta[name='_csrf_header']").attr("content");
                                console.log('Meta CSRF Token:', metaCsrfToken);
                                console.log('Meta CSRF Header:', metaCsrfHeader);

                                if (metaCsrfToken && metaCsrfHeader) {
                                    headers[metaCsrfHeader] = metaCsrfToken;
                                }

                                console.log('Request headers:', headers);

                                const requestBody = 'courseCodes=' + courseCodes.map(encodeURIComponent).join('&courseCodes=');
                                console.log('Request body:', requestBody);

                                fetch('/staff/timetable/generate', {
                                    method: 'POST',
                                    headers: headers,
                                    body: requestBody,
                                    signal: controller.signal
                                }).then(response => {
                                    clearTimeout(timeoutId);
                                    console.log('Response status:', response.status);
                                    console.log('Response headers:', [...response.headers.entries()]);

                                    if (!response.ok) {
                                        throw new Error(`HTTP error! status: ${response.status} - ${response.statusText}`);
                                    }

                                    // Try to parse JSON
                                    return response.text().then(text => {
                                        console.log('Response text:', text);
                                        try {
                                            return JSON.parse(text);
                                        } catch (e) {
                                            throw new Error('Server returned invalid JSON: ' + text);
                                        }
                                    });
                                })
                                    .then(data => {
                                        // Hide spinner
                                        document.getElementById('spinner').classList.remove('show');

                                        if (data.success) {
                                            // Success - show details and redirect
                                            let message = data.message;
                                            if (data.details) {
                                                message += '\\n\\nChi tiết:';
                                                message += '\\n• Số lớp học: ' + data.details.classes;
                                                message += '\\n• Fitness score: ' + data.details.fitness.toFixed(3);
                                                message += '\\n• Số thế hệ: ' + data.details.generations;
                                                message += '\\n• Thời gian: ' + data.details.executionTime + 'ms';
                                                if (data.details.conflicts > 0) {
                                                    message += '\\n• Xung đột: ' + data.details.conflicts;
                                                }
                                                if (data.details.violations > 0) {
                                                    message += '\\n• Vi phạm: ' + data.details.violations;
                                                }
                                            }

                                            alert(message);

                                            // Redirect to view timetable page
                                            window.location.href = '/staff/timetable/view';
                                        } else {
                                            // Error - show message with detailed info
                                            let message = 'Lỗi: ' + data.message;
                                            if (data.error_type) {
                                                message += '\\n\\nLoại lỗi: ' + data.error_type;
                                            }
                                            if (data.error_details) {
                                                message += '\\nChi tiết: ' + data.error_details;
                                            }
                                            if (data.details) {
                                                message += '\\n\\nThông tin debug:';
                                                message += '\\n• Fitness score: ' + data.details.fitness.toFixed(3);
                                                message += '\\n• Số thế hệ: ' + data.details.generations;
                                                if (data.details.conflicts > 0) {
                                                    message += '\\n• Xung đột: ' + data.details.conflicts;
                                                }
                                                if (data.details.violations > 0) {
                                                    message += '\\n• Vi phạm: ' + data.details.violations;
                                                }
                                            }
                                            alert(message);
                                        }
                                    }).catch(error => {
                                        clearTimeout(timeoutId);
                                        // Hide spinner
                                        document.getElementById('spinner').classList.remove('show');
                                        console.error('Full error object:', error);
                                        console.error('Error stack:', error.stack);

                                        if (error.name === 'AbortError') {
                                            alert('Quá trình tạo lịch học đã timeout. Vui lòng thử lại với ít môn học hơn.');
                                        } else if (error.message.includes('HTTP error')) {
                                            alert('Lỗi server: ' + error.message + '\\n\\nVui lòng kiểm tra console để xem chi tiết.');
                                        } else if (error.message.includes('Failed to fetch')) {
                                            alert('Không thể kết nối với server. Vui lòng kiểm tra:\\n- Server có đang chạy không?\\n- Đường mạng có ổn định không?');
                                        } else {
                                            alert('Có lỗi xảy ra: ' + error.message + '\\n\\nVui lòng kiểm tra console để xem chi tiết.');
                                        }
                                    });
                            }
                        }
                    </script>
                </body>

                </html>