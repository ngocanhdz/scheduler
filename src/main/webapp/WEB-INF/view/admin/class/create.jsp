<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <title>Tạo Phòng học mới</title>
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
                                <h1 class="mt-4">Tạo Phòng học mới</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item"><a href="/admin/class">Phòng học</a></li>
                                    <li class="breadcrumb-item active">Tạo mới</li>
                                </ol>
                                <div class="mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <div class="card">
                                                <div class="card-header">
                                                    <i class="fas fa-plus"></i> Tạo Phòng học
                                                </div>
                                                <div class="card-body">
                                                    <form:form method="post" action="/admin/class/create"
                                                        modelAttribute="newClass">
                                                        <div class="mb-3">
                                                            <label class="form-label">Tên phòng:</label>
                                                            <form:input type="text" class="form-control" path="name"
                                                                placeholder="Ví dụ: A101, B202..." />
                                                            <form:errors path="name"
                                                                cssClass="invalid-feedback d-block" />
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Sức chứa:</label>
                                                            <form:input type="number" class="form-control"
                                                                path="capacity" min="1"
                                                                placeholder="Số lượng sinh viên tối đa" />
                                                            <form:errors path="capacity"
                                                                cssClass="invalid-feedback d-block" />
                                                        </div>
                                                        <div class="mt-4">
                                                            <button type="submit" class="btn btn-primary">
                                                                <i class="fas fa-save"></i> Tạo
                                                            </button>
                                                            <a href="/admin/class" class="btn btn-secondary">
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