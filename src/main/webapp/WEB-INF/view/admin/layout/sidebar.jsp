<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <div id="layoutSidenav_nav">
                <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                    <div class="sb-sidenav-menu">
                        <div class="nav">
                            <div class="sb-sidenav-menu-heading">Features</div>
                            <a class="nav-link" href="/admin">
                                <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                                Dashboard
                            </a> <a class="nav-link" href="/admin/user">
                                <div class="sb-nav-link-icon"><i class="fas fa-users"></i></div>
                                User
                            </a>
                            <a class="nav-link" href="/admin/course">
                                <div class="sb-nav-link-icon"><i class="fas fa-book"></i></div>
                                Course
                            </a>
                            <a class="nav-link" href="/admin/class">
                                <div class="sb-nav-link-icon"><i class="fas fa-door-open"></i></div>
                                Class
                            </a>
                            <a class="nav-link" href="/admin/day">
                                <div class="sb-nav-link-icon"><i class="fas fa-calendar-day"></i></div>
                                Day
                            </a>
                            <a class="nav-link" href="/admin/period">
                                <div class="sb-nav-link-icon"><i class="fas fa-clock"></i></div>
                                Period
                            </a>
                            <a class="nav-link" href="/admin/order">
                                <div class="sb-nav-link-icon"><i class="fas fa-shopping-cart"></i></div>
                                Orders
                            </a>
                        </div>
                    </div>
                    <div class="sb-sidenav-footer">
                        <div class="small">Logged in as:</div>
                        admin
                    </div>
                </nav>
            </div>