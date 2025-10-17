<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <title>Cập nhật Tiết học</title>
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
                                <h1 class="mt-4">Cập nhật Tiết học</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item"><a href="/admin/period">Tiết học</a></li>
                                    <li class="breadcrumb-item active">Cập nhật</li>
                                </ol>
                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-md-8 col-12 mx-auto">
                                            <div class="card">
                                                <div class="card-header">
                                                    <i class="fas fa-edit"></i> Cập nhật Tiết học
                                                </div>
                                                <div class="card-body">
                                                    <form:form method="post" action="/admin/period/update"
                                                        modelAttribute="currentPeriod">
                                                        <div class="mb-3" style="display: none;">
                                                            <form:input type="text" class="form-control" path="id" />
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-md-6 mb-3">
                                                                <label class="form-label">Số tiết:</label>
                                                                <form:input type="number" class="form-control"
                                                                    path="number" min="1"
                                                                    placeholder="Số thứ tự tiết học" />
                                                                <form:errors path="number"
                                                                    cssClass="invalid-feedback d-block" />
                                                            </div>
                                                            <div class="col-md-6 mb-3">
                                                                <label class="form-label">Tên tiết:</label>
                                                                <form:input type="text" class="form-control" path="name"
                                                                    placeholder="Ví dụ: Tiết 1, Tiết 2..." />
                                                                <form:errors path="name"
                                                                    cssClass="invalid-feedback d-block" />
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-md-6 mb-3">
                                                                <label class="form-label">Giờ bắt đầu:</label>
                                                                <form:input type="number" class="form-control"
                                                                    path="start" min="0" max="2359"
                                                                    placeholder="Ví dụ: 700 (7:00)" />
                                                                <form:errors path="start"
                                                                    cssClass="invalid-feedback d-block" />
                                                                <div class="form-text">Định dạng: HHMM (ví dụ: 700 =
                                                                    7:00, 1430 = 14:30)</div>
                                                            </div>
                                                            <div class="col-md-6 mb-3">
                                                                <label class="form-label">Giờ kết thúc:</label>
                                                                <form:input type="number" class="form-control"
                                                                    path="end" min="0" max="2359"
                                                                    placeholder="Ví dụ: 750 (7:50)" />
                                                                <form:errors path="end"
                                                                    cssClass="invalid-feedback d-block" />
                                                                <div class="form-text">Định dạng: HHMM (ví dụ: 750 =
                                                                    7:50, 1520 = 15:20)</div>
                                                            </div>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Ngày:</label>
                                                            <select name="dayName" class="form-select" required>
                                                                <option value="">Chọn ngày</option>
                                                                <c:forEach var="day" items="${days}">
                                                                    <option value="${day.name}"
                                                                        ${day.name==currentPeriod.day.name ? 'selected'
                                                                        : '' }>${day.name}</option>
                                                                </c:forEach>
                                                            </select>
                                                            <form:errors path="day"
                                                                cssClass="invalid-feedback d-block" />
                                                        </div>
                                                        <div class="mt-4">
                                                            <button type="submit" class="btn btn-warning">
                                                                <i class="fas fa-save"></i> Cập nhật
                                                            </button>
                                                            <a href="/admin/period" class="btn btn-secondary">
                                                                <i class="fas fa-times"></i> Hủy
                                                            </a>
                                                        </div>
                                                    </form:form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </main>
                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>
            </body>

            </html>