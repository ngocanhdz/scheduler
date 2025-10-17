<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <style>
                .hero-header {
                    background: linear-gradient(rgba(255, 255, 255, 0.7), rgba(255, 255, 255, 0.7)), url('/client/img/banner_hust_demo.png') no-repeat center center;
                    background-size: cover;
                }
            </style>
            <div class="container-fluid py-5 mb-5 hero-header">
                <div class="container py-5">
                    <div class="row g-5 align-items-center">
                        <div class="col-md-12 col-lg-7">
                            <h4 class="mb-3 text-secondary">Tiện ích dành cho</h4>
                            <h1 class="mb-5 display-3 text-danger">Sinh viên và Giáo vụ</h1>
                        </div>
                        <div class="col-md-12 col-lg-5">
                            <div id="carouselId" class="carousel slide position-relative" data-bs-ride="carousel">
                                <div class="carousel-inner" role="listbox">
                                    <div class="carousel-item active rounded">
                                        <img src="/client/img/staff_banner.jpg"
                                            class="img-fluid w-100 h-100 bg-danger rounded" alt="First slide">
                                        <a href="#" class="btn px-4 py-2 text-white rounded">Năng động</a>
                                    </div>
                                    <div class="carousel-item rounded">
                                        <img src="/client/img/student_banner.jpg" class="img-fluid w-100 h-100 rounded"
                                            alt="Second slide">
                                        <a href="#" class="btn px-4 py-2 text-white rounded">Sáng tạo</a>
                                    </div>
                                </div> <button class="carousel-control-prev bg-danger" type="button"
                                    data-bs-target="#carouselId" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next bg-danger" type="button"
                                    data-bs-target="#carouselId" data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>