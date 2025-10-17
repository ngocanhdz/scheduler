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
                            .config-form {
                                max-width: 600px;
                                margin: 2rem auto;
                                padding: 2rem;
                                background: #fff;
                                border-radius: 8px;
                                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            }

                            .form-group {
                                margin-bottom: 1.5rem;
                            }

                            .btn-config {
                                width: 100%;
                                padding: 0.8rem;
                                font-size: 1.1rem;
                            }

                            .nav-tabs .nav-link {
                                color: #666;
                            }

                            .nav-tabs .nav-link.active {
                                color: #0d6efd;
                                font-weight: 600;
                            }
                        </style>
                    </head>

                    <body>
                        <jsp:include page="../../client/layout/header.jsp" />
                        <div class="container-fluid page-header py-5 justify-content-center">
                            <h1 class="text-center text-white display-6">Cấu hình đăng ký học phần</h1>
                            <h5 class="text-white text-center">Thiết lập thời gian đăng ký học phần</h5>
                        </div>

                        <!-- Config Page Start -->
                        <div class="container">
                            <div class="config-form">
                                <h4 class="text-center mb-4">Thiết lập đăng ký học phần</h4>

                                <!-- Alert Messages -->
                                <c:if test="${not empty message}">
                                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                                        ${message}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        ${error}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                <form:form method="post" action="/staff/semester-config/save"
                                    modelAttribute="semesterConfig">

                                    <!-- Hidden field để xác định mode -->
                                    <input type="hidden" id="actionMode" name="actionMode" value="select" />

                                    <div class="form-group">
                                        <label class="form-label">Học kỳ:</label>
                                        <div id="selectMode">
                                            <select class="form-select" name="semesterSelect" id="semesterSelect"
                                                onchange="handleSemesterChange()">
                                                <option value="">-- Chọn học kỳ --</option>
                                                <c:forEach items="${semesters}" var="semester">
                                                    <option value="${semester}">${semester}</option>
                                                </c:forEach>
                                                <option value="__NEW__">+ Thêm học kỳ mới</option>
                                            </select>
                                        </div>
                                        <div id="inputMode" style="display: none;">
                                            <div class="input-group">
                                                <input type="text" class="form-control" id="semesterInput"
                                                    placeholder="Nhập tên học kỳ (VD: 20251)" />
                                                <button type="button" class="btn btn-outline-secondary"
                                                    onclick="backToSelect()">
                                                    <i class="bi bi-arrow-left"></i> Quay lại
                                                </button>
                                            </div>
                                        </div>
                                        <!-- Hidden field để lưu giá trị name cuối cùng -->
                                        <form:input type="hidden" path="name" id="finalSemesterName" />
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label">Thời gian bắt đầu đăng ký:</label>
                                        <form:input type="datetime-local" class="form-control" path="start"
                                            required="true" />
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label">Thời gian kết thúc đăng ký:</label>
                                        <form:input type="datetime-local" class="form-control" path="end"
                                            required="true" />
                                    </div>

                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary btn-config" id="submitBtn">
                                            Lưu cấu hình
                                        </button>
                                    </div>
                                </form:form>

                                <div class="mt-4">
                                    <h5 class="mb-3">Cấu hình hiện tại:</h5>
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Học kỳ</th>
                                                <th>Bắt đầu</th>
                                                <th>Kết thúc</th>
                                                <th>Trạng thái</th>
                                                <th></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${currentConfigs}" var="config">
                                                <tr>
                                                    <td>${config.name}</td>
                                                    <td>
                                                        <fmt:formatDate value="${config.start}"
                                                            pattern="dd/MM/yyyy HH:mm" />
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${config.end}"
                                                            pattern="dd/MM/yyyy HH:mm" />
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${config.state == true}">
                                                                <span class="badge bg-success">Đang kích hoạt</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">Không kích hoạt</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="/staff/semester-config/delete/${config.id}"
                                                            class="btn btn-sm btn-danger"
                                                            onclick="return confirm('Bạn có chắc muốn xóa cấu hình này?')">
                                                            Xóa
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <!-- Config Page End -->

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
                        <script>
                            function handleSemesterChange() {
                                const select = document.getElementById('semesterSelect');
                                const selectedValue = select.value;

                                if (selectedValue === '__NEW__') {
                                    switchToInputMode();
                                } else if (selectedValue !== '') {
                                    // Cập nhật hidden field với giá trị được chọn
                                    document.getElementById('finalSemesterName').value = selectedValue;
                                    document.getElementById('actionMode').value = 'select';
                                    document.getElementById('submitBtn').textContent = 'Lưu cấu hình';
                                    document.getElementById('submitBtn').className = 'btn btn-primary btn-config';
                                } else {
                                    // Nếu không chọn gì, clear hidden field
                                    document.getElementById('finalSemesterName').value = '';
                                }
                            }

                            function switchToInputMode() {
                                document.getElementById('selectMode').style.display = 'none';
                                document.getElementById('inputMode').style.display = 'block';
                                document.getElementById('actionMode').value = 'create';
                                document.getElementById('submitBtn').textContent = 'Tạo học kỳ mới';
                                document.getElementById('submitBtn').className = 'btn btn-success btn-config';
                                document.getElementById('semesterInput').focus();

                                // Clear giá trị hidden field
                                document.getElementById('finalSemesterName').value = '';
                            }

                            function backToSelect() {
                                document.getElementById('selectMode').style.display = 'block';
                                document.getElementById('inputMode').style.display = 'none';
                                document.getElementById('actionMode').value = 'select';
                                document.getElementById('submitBtn').textContent = 'Lưu cấu hình';
                                document.getElementById('submitBtn').className = 'btn btn-primary btn-config';

                                // Reset các giá trị
                                document.getElementById('semesterSelect').value = '';
                                document.getElementById('finalSemesterName').value = '';
                                document.getElementById('semesterInput').value = '';
                            }                            // Function để đồng bộ giá trị từ input field vào hidden field
                            function syncInputToHidden() {
                                const inputValue = document.getElementById('semesterInput').value;
                                document.getElementById('finalSemesterName').value = inputValue;
                            }

                            // Khởi tạo trạng thái ban đầu
                            document.addEventListener('DOMContentLoaded', function () {
                                // Thêm event listener cho input field để đồng bộ giá trị
                                document.getElementById('semesterInput').addEventListener('input', syncInputToHidden);                                // Thêm event listener cho form submit để đảm bảo đồng bộ
                                document.querySelector('form').addEventListener('submit', function (e) {
                                    const actionMode = document.getElementById('actionMode').value;
                                    const nameField = document.getElementById('finalSemesterName');
                                    const startField = document.querySelector('input[path="start"]');
                                    const endField = document.querySelector('input[path="end"]');

                                    console.log('Form submit - actionMode:', actionMode);
                                    console.log('Current name field value:', nameField.value);

                                    if (actionMode === 'create') {
                                        syncInputToHidden();
                                        console.log('After sync - name field value:', nameField.value);
                                    }

                                    // Validate name field has value
                                    if (!nameField.value || nameField.value.trim() === '') {
                                        e.preventDefault();
                                        alert('Vui lòng chọn hoặc nhập tên học kỳ!');
                                        return false;
                                    }

                                    // Validate start and end dates
                                    if (!startField.value || !endField.value) {
                                        e.preventDefault();
                                        alert('Vui lòng nhập đầy đủ thời gian bắt đầu và kết thúc!');
                                        return false;
                                    }

                                    // Validate start date is before end date
                                    const startDate = new Date(startField.value);
                                    const endDate = new Date(endField.value);
                                    if (startDate >= endDate) {
                                        e.preventDefault();
                                        alert('Thời gian bắt đầu phải trước thời gian kết thúc!');
                                        return false;
                                    }
                                });
                            });
                        </script>
                    </body>

                    </html>