<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8">
                    <title>Quản lý tài khoản</title>
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
                    <style>
                        .form-control:not([disabled]),
                        .form-select:not([disabled]) {
                            font-weight: 600;
                            background-color: #fff;
                        }

                        .form-label.active {
                            font-weight: 600;
                            color: #0d6efd;
                        }
                    </style>
                </head>

                <body class="sb-nav-fixed">
                    <jsp:include page="../layout/header.jsp" />
                    <div id="layoutSidenav">
                        <div id="layoutSidenav_content">
                            <main>

                                <div class="container" style="padding-top: 150px;">

                                    <div class="row">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h3>Thông tin người dùng</h3>
                                            <div class="row mb-3">
                                                <div class="col">
                                                    <img style="max-height: 250px"
                                                        src="/images/avatar/${user.avatar}" />
                                                </div>
                                            </div>
                                            <hr />
                                            <form:form method="post" action="/client/detail/update"
                                                modelAttribute="user" enctype="multipart/form-data">
                                                <div class="mb-3" style="display: none">
                                                    <label class="form-label">ID: </label>
                                                    <form:input type="text" class="form-control" path="id" />
                                                </div>
                                                <div class="mb-3" style="display: none">
                                                    <label class="form-label">Password:</label>
                                                    <form:input type="password" class="form-control" path="password" />
                                                </div>
                                                <div class="row">
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">Email:</label>
                                                            <form:input type="email" class="form-control" path="email"
                                                                disabled="true" />
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label active">Phone number:</label>
                                                            <form:input type="text" class="form-control"
                                                                path="phoneNumber" />
                                                        </div>
                                                    </div>
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label active">Full Name:</label>
                                                            <form:input type="text" class="form-control"
                                                                path="fullName" />
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mb-3">
                                                    <label class="form-label active">Address:</label>
                                                    <form:input type="text" class="form-control" path="address" />
                                                </div>
                                                <div class="row">
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label active"
                                                                for="avatarFile">Avatar:</label>
                                                            <input class="form-control" type="file" id="avatarFile"
                                                                accept=" .png, .jpg, .jpeg" name="userFile" />
                                                        </div>
                                                    </div>
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">Role:</label>
                                                            <form:select class="form-select" path="role.name"
                                                                disabled="true">
                                                                <form:option value="ADMIN">ADMIN</form:option>
                                                                <form:option value="STUDENT">STUDENT</form:option>
                                                                <form:option value="STAFF">STAFF</form:option>
                                                            </form:select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label">Year level: </label>
                                                            <form:select class="form-select" path="yearLevel"
                                                                disabled="true">
                                                                <form:option value="1">One</form:option>
                                                                <form:option value="2">TWO</form:option>
                                                                <form:option value="3">THREE</form:option>
                                                                <form:option value="4">FOUR</form:option>
                                                                <form:option value="0">OPTION FOR ADMIN/STAFF
                                                                </form:option>
                                                            </form:select>
                                                        </div>
                                                    </div>
                                                    <div class="col">
                                                        <div class="mb-3">
                                                            <label class="form-label active">College:</label>
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
                                                <div class="row mb-3">
                                                    <div class="col">
                                                        <img style="max-height: 250px; display: none;"
                                                            alt="avatar preview" id="avatarPreview" />
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col">
                                                        <button type="submit" class="btn btn-outline-secondary">Chỉnh
                                                            sửa</button>
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
                        $(document).ready(function () {
                            const avatarFile = document.getElementById('avatarFile');
                            const avatarPreview = document.getElementById('avatarPreview');

                            avatarFile.addEventListener('change', function (e) {
                                const file = e.target.files[0];
                                if (file) {
                                    const reader = new FileReader();
                                    reader.onload = function (e) {
                                        avatarPreview.src = e.target.result;
                                        avatarPreview.style.display = 'block';
                                    }
                                    reader.readAsDataURL(file);
                                }
                            });
                        });
                    </script>
                </body>

                </html>