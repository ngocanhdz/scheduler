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
                    <title>Dashboard - Update course</title>
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
                                <h1 class="mt-4">Update User</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item active"><a href="/admin/course">Courses</a></li>
                                    <li class="breadcrumb-item active">Update Course</li>
                                </ol>
                                <div class="container mt-5">
                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h3>Update a course</h3>
                                            <hr />
                                            <form:form method="post" action="/admin/course/update"
                                                modelAttribute="currentCourse">
                                                <div class="mb-3" style="display: none;">
                                                    <label class="form-label">Id:</label>
                                                    <form:input type="id" class="form-control" path="id" />
                                                </div>
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
                                                        <button type="submit" class="btn btn-primary">Submit</button>
                                                    </div>
                                                </div>

                                            </form:form>
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