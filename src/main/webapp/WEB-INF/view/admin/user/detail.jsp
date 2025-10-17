<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <title>Detail</title>
                <link href="/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <h1 class="mt-4">User Detail</h1>
                            <ol class="breadcrumb mb-4">
                                <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                <li class="breadcrumb-item active"><a href="/admin/user">Users</a></li>
                                <li class="breadcrumb-item active">User Detail</li>
                            </ol>
                            <div class="container mt-5">
                                <div class="row">
                                    <div class="col-12 mx-auto">
                                        <div class="d-flex justify-content-between">
                                            <h3>User Detail: ${id} </h3>
                                        </div>
                                        <hr />
                                        <div class="card w-75 border-dark">
                                            <div class="card-header">
                                                ID: ${user.id}
                                            </div>
                                            <div class="row mb-3">
                                                <div class="col">
                                                    <img style="max-height: 250px"
                                                        src="/images/avatar/${user.avatar}" />
                                                </div>
                                            </div>
                                            <ul class="list-group list-group-flush">
                                                <li class="list-group-item">Email: ${user.email}</li>
                                                <li class="list-group-item">Phone Number: ${user.phoneNumber}</li>
                                                <li class="list-group-item">Full Name: ${user.fullName}</li>
                                                <li class="list-group-item">Address: ${user.address}</li>
                                                <li class="list-group-item">Role: ${user.role.name}</li>
                                                <c:if
                                                    test="${user.role.id != 1 and user.role.id != 3 and user.yearLevel != 0}">
                                                    <li class="list-group-item">Year Level: ${user.yearLevel}</li>
                                                </c:if>
                                                <li class="list-group-item">College: ${user.college.name}</li>
                                            </ul>
                                        </div>
                                        <a href="/admin/user" class="btn btn-primary mt-3">Back</a>
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
            </body>

            </html>