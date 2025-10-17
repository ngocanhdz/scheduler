<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8" />
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                    <title>Dashboard - Create course</title>
                    <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css"
                        rel="stylesheet" />
                    <link href="/css/styles.css" rel="stylesheet" />
                    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
                        crossorigin="anonymous"></script>
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                </head>

                <body class="sb-nav-fixed">
                    <jsp:include page="../layout/header.jsp" />
                    <div id="layoutSidenav">
                        <jsp:include page="../layout/sidebar.jsp" />
                        <div id="layoutSidenav_content">
                            <main>
                                <h1 class="mt-4">Create Course</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active"><a href="/admin/course">Courses</a></li>
                                    <li class="breadcrumb-item active">Create Course</li>
                                </ol>
                                <div class="container mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h3>Create a course</h3>
                                            <hr />
                                            <form:form method="post" action="/admin/course/create"
                                                modelAttribute="newCourse">
                                                <div class="row">
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">Name:</label>

                                                            <form:input type="text" class="form-control" path="name" />
                                                        </div>
                                                    </div>
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">English Name:</label>
                                                            <form:input type="text" class="form-control"
                                                                path="englishName" />
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">Lecture hour:</label>
                                                            <form:input type="number" class="form-control"
                                                                path="lectureHour" />
                                                        </div>
                                                    </div>
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">Practical hour:</label>
                                                            <form:input type="number" class="form-control"
                                                                path="practicalHour" />
                                                        </div>
                                                    </div>
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">Credit:</label>
                                                            <form:input type="number" class="form-control"
                                                                path="credit" />
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mb-3">
                                                    <label class="form-label">Description:</label>
                                                    <form:input type="text" class="form-control" path="description" />
                                                </div>
                                                <div class="row">
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">Code:</label>
                                                            <form:input type="text" class="form-control" path="code" />
                                                        </div>
                                                    </div>
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">College:</label>
                                                            <form:select class="form-select" path="college.name">
                                                                <form:option value="BGDTC">BGDTC</form:option>
                                                                <form:option value="KGDQP">KGDQP</form:option>
                                                                <form:option value="KKTVQL">KKTVQL</form:option>
                                                                <form:option value="KML">KML</form:option>
                                                                <form:option value="KNN">KNN</form:option>
                                                                <form:option value="KSPKT">KSPKT</form:option>
                                                                <form:option value="KTTD">KTTD</form:option>
                                                                <form:option value="TCK">TCK</form:option>
                                                                <form:option value="TCNTT">TCNTT</form:option>
                                                                <form:option value="TDDT">TDDT</form:option>
                                                                <form:option value="THKHSS">THKHSS</form:option>
                                                                <form:option value="TTNNHT">TTNNHT</form:option>
                                                                <form:option value="TVL">TVL</form:option>
                                                                <form:option value="VVLKT">VVLKT</form:option>
                                                            </form:select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col">
                                                        <button type="submit" class="btn btn-primary">Create</button>
                                                    </div>
                                                </div>

                                            </form:form>
                                        </div>
                                    </div>
                                    <div class="row mt-4">
                                        <div class="col-12 col-md-6 mx-auto">
                                            <div class="card shadow-sm">
                                                <div class="card-header bg-success text-white">
                                                    <h5 class="mb-0">
                                                        <i class="fas fa-file-excel me-2"></i>Import Courses from
                                                        Excel
                                                    </h5>
                                                </div>
                                                <div class="card-body">
                                                    <form:form method="post" action="/admin/course/import"
                                                        enctype="multipart/form-data">
                                                        <div class="row">
                                                            <div class="col">
                                                                <div class="mb-3">
                                                                    <label for="file" class="form-label">Select
                                                                        Excel
                                                                        File:</label>
                                                                    <input type="file" name="file" id="file"
                                                                        accept=".xlsx,.xls" required
                                                                        class="form-control" />
                                                                    <div class="form-text">Supported formats:
                                                                        .xlsx,
                                                                        .xls</div>
                                                                </div>
                                                            </div>
                                                            <div class="col">
                                                                <div class="d-flex gap-2 mt-4">
                                                                    <button type="submit" class="btn btn-success">
                                                                        <i class="fas fa-upload me-2"></i>Import
                                                                        Courses
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </form:form>
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
                    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
                        crossorigin="anonymous"></script>
                    <script src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js"
                        crossorigin="anonymous"></script>
                </body>

                </html>