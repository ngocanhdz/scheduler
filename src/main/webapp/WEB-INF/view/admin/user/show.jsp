<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <title>Dashboard Admin</title>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <h1 class="mt-4">Manager Users</h1>
                            <ol class="breadcrumb mb-4">
                                <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                <li class="breadcrumb-item active">Users</li>
                            </ol>
                            <div class="container mt-5">
                                <div class="row">
                                    <div class="col-12 mx-auto">
                                        <div class="d-flex justify-content-between">
                                            <h3>Table user</h3>
                                            <div>
                                                <a href="/admin/user/export" class="btn btn-success me-2">Xuất file
                                                    Excel</a>
                                                <a href="/admin/user/create" class="btn btn-primary">Tạo người dùng
                                                    mới</a>
                                            </div>
                                        </div>

                                        <!-- Search and Filter Form -->
                                        <div class="card mt-3 mb-4">
                                            <div class="card-header">
                                                <h5>Tìm kiếm và lọc</h5>
                                            </div>
                                            <div class="card-body">
                                                <form method="GET" action="/admin/user">
                                                    <div class="row">
                                                        <div class="col-md-3">
                                                            <label class="form-label">Tên đầy đủ:</label>
                                                            <input type="text" class="form-control" name="fullName"
                                                                value="${param.fullName}" placeholder="Nhập tên...">
                                                        </div>
                                                        <div class="col-md-2">
                                                            <label class="form-label">Năm học:</label>
                                                            <select class="form-select" name="yearLevel">
                                                                <option value="">Tất cả</option>
                                                                <c:forEach items="${yearLevels}" var="year">
                                                                    <option value="${year}" ${yearLevel==year
                                                                        ? 'selected' : '' }>
                                                                        ${year}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <label class="form-label">Vai trò:</label>
                                                            <select class="form-select" name="roleName">
                                                                <option value="">Tất cả</option>
                                                                <c:forEach items="${roleNames}" var="role">
                                                                    <option value="${role}" ${roleName==role
                                                                        ? 'selected' : '' }>
                                                                        ${role}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <label class="form-label">Đơn vị:</label>
                                                            <select class="form-select" name="collegeName">
                                                                <option value="">Tất cả</option>
                                                                <c:forEach items="${collegeNames}" var="college">
                                                                    <option value="${college}" ${collegeName==college
                                                                        ? 'selected' : '' }>
                                                                        ${college}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                        <div class="col-md-2">
                                                            <label class="form-label">&nbsp;</label>
                                                            <div class="d-grid">
                                                                <button type="submit" class="btn btn-primary">Tìm
                                                                    kiếm</button>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Hidden fields to maintain sorting -->
                                                    <input type="hidden" name="sortBy" value="${sortBy}">
                                                    <input type="hidden" name="sortDir" value="${sortDir}">
                                                    <input type="hidden" name="size" value="${size}">
                                                </form>
                                            </div>
                                        </div>

                                        <!-- Results Summary -->
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <span class="text-muted">
                                                Hiển thị ${currentPage * size + 1} - ${(currentPage + 1) * size >
                                                totalElements ? totalElements : (currentPage + 1) * size}
                                                trong tổng số ${totalElements} người dùng
                                            </span>
                                            <div>
                                                <label for="sizeSelect">Hiển thị:</label>
                                                <select id="sizeSelect" class="form-select d-inline-block"
                                                    style="width: auto;" onchange="changePageSize(this.value)">
                                                    <option value="10" ${size==10 ? 'selected' : '' }>10</option>
                                                    <option value="25" ${size==25 ? 'selected' : '' }>25</option>
                                                    <option value="50" ${size==50 ? 'selected' : '' }>50</option>
                                                    <option value="100" ${size==100 ? 'selected' : '' }>100</option>
                                                </select>
                                            </div>
                                        </div>

                                        <hr />
                                        <table class="table table-bordered table-hover">
                                            <thead>
                                                <tr>
                                                    <th scope="col">
                                                        <a href="?sortBy=id&sortDir=${sortBy == 'id' && sortDir == 'asc' ? 'desc' : 'asc'}&fullName=${param.fullName}&yearLevel=${yearLevel}&roleName=${roleName}&collegeName=${collegeName}&size=${size}"
                                                            class="text-decoration-none">
                                                            ID ${sortBy == 'id' ? (sortDir == 'asc' ? '↑' : '↓') : ''}
                                                        </a>
                                                    </th>
                                                    <th scope="col">
                                                        <a href="?sortBy=email&sortDir=${sortBy == 'email' && sortDir == 'asc' ? 'desc' : 'asc'}&fullName=${param.fullName}&yearLevel=${yearLevel}&roleName=${roleName}&collegeName=${collegeName}&size=${size}"
                                                            class="text-decoration-none">
                                                            Email ${sortBy == 'email' ? (sortDir == 'asc' ? '↑' : '↓') :
                                                            ''}
                                                        </a>
                                                    </th>
                                                    <th scope="col">
                                                        <a href="?sortBy=fullName&sortDir=${sortBy == 'param.fullName' && sortDir == 'asc' ? 'desc' : 'asc'}&fullName=${param.fullName}&yearLevel=${yearLevel}&roleName=${roleName}&collegeName=${collegeName}&size=${size}"
                                                            class="text-decoration-none">
                                                            Full Name ${sortBy == 'param.fullName' ? (sortDir == 'asc' ?
                                                            '↑' :
                                                            '↓') : ''}
                                                        </a>
                                                    </th>
                                                    <th scope="col">Role</th>
                                                    <th scope="col">College</th>
                                                    <th scope="col">Year Level</th>
                                                    <th scope="col">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="user" items="${users}">
                                                    <tr>
                                                        <th scope="row">${user.id}</th>
                                                        <td>${user.email}</td>
                                                        <td>${user.fullName}</td>
                                                        <td>${user.role.name}</td>
                                                        <td>${user.college.name}</td>
                                                        <td>${user.yearLevel}</td>
                                                        <td>
                                                            <a href="/admin/user/${user.id}"
                                                                class="btn btn-success btn-sm">View</a>
                                                            <a href="/admin/user/update/${user.id}"
                                                                class="btn btn-warning btn-sm mx-1">Update</a>
                                                            <a href="/admin/user/delete/${user.id}"
                                                                class="btn btn-danger btn-sm">Delete</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Pagination -->
                                        <c:if test="${totalPages > 1}">
                                            <nav aria-label="User pagination">
                                                <ul class="pagination justify-content-center">
                                                    <!-- Previous button -->
                                                    <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                                                        <a class="page-link"
                                                            href="?page=${currentPage - 1}&sortBy=${sortBy}&sortDir=${sortDir}&fullName=${fullName}&yearLevel=${yearLevel}&roleName=${roleName}&collegeName=${collegeName}&size=${size}">
                                                            Trước
                                                        </a>
                                                    </li>

                                                    <!-- Page numbers -->
                                                    <c:forEach begin="0" end="${totalPages - 1}" var="pageNum">
                                                        <c:if
                                                            test="${pageNum >= currentPage - 2 && pageNum <= currentPage + 2}">
                                                            <li
                                                                class="page-item ${pageNum == currentPage ? 'active' : ''}">
                                                                <a class="page-link"
                                                                    href="?page=${pageNum}&sortBy=${sortBy}&sortDir=${sortDir}&fullName=${fullName}&yearLevel=${yearLevel}&roleName=${roleName}&collegeName=${collegeName}&size=${size}">
                                                                    ${pageNum + 1}
                                                                </a>
                                                            </li>
                                                        </c:if>
                                                    </c:forEach>

                                                    <!-- Next button -->
                                                    <li
                                                        class="page-item ${currentPage == totalPages - 1 ? 'disabled' : ''}">
                                                        <a class="page-link"
                                                            href="?page=${currentPage + 1}&sortBy=${sortBy}&sortDir=${sortDir}&fullName=${fullName}&yearLevel=${yearLevel}&roleName=${roleName}&collegeName=${collegeName}&size=${size}">
                                                            Sau
                                                        </a>
                                                    </li>
                                                </ul>
                                            </nav>
                                        </c:if>
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
                <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
                    crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js"
                    crossorigin="anonymous"></script>

                <script>
                    function changePageSize(newSize) {
                        const urlParams = new URLSearchParams(window.location.search);
                        urlParams.set('size', newSize);
                        urlParams.set('page', '0'); // Reset to first page
                        window.location.search = urlParams.toString();
                    }
                </script>
            </body>

            </html>