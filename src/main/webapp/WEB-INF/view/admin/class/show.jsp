<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <title>Quản lý Phòng học</title>
                <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Quản lý Phòng học</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active">Phòng học</li>
                                </ol>

                                <!-- Success/Error Messages -->
                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                                        ${successMessage}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        ${errorMessage}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>

                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-12 mx-auto">
                                            <div class="d-flex justify-content-between">
                                                <h3>Danh sách Phòng học</h3>
                                                <div>
                                                    <a href="/admin/class/create" class="btn btn-primary me-2">Tạo mới
                                                        Phòng học</a>

                                                    <!-- Excel Actions -->
                                                    <div class="btn-group" role="group">
                                                        <button type="button"
                                                            class="btn btn-outline-success dropdown-toggle"
                                                            data-bs-toggle="dropdown">
                                                            <i class="fas fa-file-excel"></i> Excel
                                                        </button>
                                                        <ul class="dropdown-menu">
                                                            <li><a class="dropdown-item" href="/admin/class/export">
                                                                    <i class="fas fa-download"></i> Export Excel
                                                                </a></li>
                                                            <li><a class="dropdown-item" href="/admin/class/template">
                                                                    <i class="fas fa-file-download"></i> Tải Template
                                                                </a></li>
                                                            <li>
                                                                <hr class="dropdown-divider">
                                                            </li>
                                                            <li>
                                                                <button class="dropdown-item" data-bs-toggle="modal"
                                                                    data-bs-target="#importModal">
                                                                    <i class="fas fa-upload"></i> Import Excel
                                                                </button>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>

                                            <hr />
                                            <table class="table table-bordered table-hover">
                                                <thead class="table-dark">
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Tên phòng</th>
                                                        <th>Sức chứa</th>
                                                        <th>Hành động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="classItem" items="${classes}">
                                                        <tr>
                                                            <td>${classItem.id}</td>
                                                            <td>${classItem.name}</td>
                                                            <td>${classItem.capacity}</td>
                                                            <td>
                                                                <a href="/admin/class/update/${classItem.id}"
                                                                    class="btn btn-warning btn-sm mx-1">
                                                                    <i class="fas fa-edit"></i> Sửa
                                                                </a>
                                                                <a href="/admin/class/delete/${classItem.id}"
                                                                    class="btn btn-danger btn-sm"
                                                                    onclick="return confirm('Bạn có chắc chắn muốn xóa phòng học này?')">
                                                                    <i class="fas fa-trash"></i> Xóa
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty classes}">
                                                        <tr>
                                                            <td colspan="4" class="text-center">Không có phòng học nào
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>

                <!-- Import Modal -->
                <div class="modal fade" id="importModal" tabindex="-1" aria-labelledby="importModalLabel"
                    aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="importModalLabel">
                                    <i class="fas fa-upload"></i> Import Phòng học từ Excel
                                </h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                            </div>
                            <form action="/admin/class/import" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="excelFile" class="form-label">Chọn file Excel:</label>
                                        <input type="file" class="form-control" id="excelFile" name="file"
                                            accept=".xlsx,.xls" required>
                                        <div class="form-text">
                                            Chỉ chấp nhận file .xlsx hoặc .xls.
                                            <a href="/admin/class/template" target="_blank">Tải template mẫu</a>
                                        </div>
                                    </div>
                                    <div class="alert alert-info">
                                        <small>
                                            <i class="fas fa-info-circle"></i>
                                            <strong>Lưu ý:</strong> Hệ thống sẽ tự động bỏ qua các phòng học có tên
                                            trùng với dữ liệu hiện có.
                                        </small>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-upload"></i> Import
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>
            </body>

            </html>