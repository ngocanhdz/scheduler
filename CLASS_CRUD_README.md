# CRUD Class Management - Hướng dẫn sử dụng

## Tính năng đã implement:

### 🏢 **CRUD Operations**
- ✅ **Hiển thị danh sách** phòng học
- ✅ **Tạo mới** phòng học với validation
- ✅ **Cập nhật** thông tin phòng học  
- ✅ **Xóa** phòng học với xác nhận

### 📊 **Excel Import/Export**
- ✅ **Export** danh sách phòng học ra file Excel
- ✅ **Download Template** Excel để import
- ✅ **Import** phòng học từ file Excel
- ✅ **Validation** khi import (bỏ qua trùng tên)

## 🔗 **URLs để truy cập:**

### Admin Panel:
- **Danh sách Class:** `http://localhost:8080/admin/class`
- **Tạo Class mới:** `http://localhost:8080/admin/class/create`
- **Sửa Class:** `http://localhost:8080/admin/class/update/{id}`
- **Xóa Class:** `http://localhost:8080/admin/class/delete/{id}`

### Excel Features:
- **Export Excel:** `http://localhost:8080/admin/class/export`
- **Download Template:** `http://localhost:8080/admin/class/template`
- **Import Excel:** POST to `http://localhost:8080/admin/class/import`

## 💾 **Database Schema:**

```sql
CREATE TABLE `class` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `capacity` BIGINT NOT NULL,
    UNIQUE KEY `unique_name` (`name`)
);
```

## 📋 **Validation Rules:**

### Class Entity:
- **Name:** Không được để trống, phải unique
- **Capacity:** Phải > 0

### Excel Import:
- **Format:** .xlsx hoặc .xls
- **Columns:** ID (bỏ trống), Tên phòng, Sức chứa
- **Validation:** Tự động bỏ qua các dòng trùng tên

## 🎯 **Sample Data cho Excel:**

```
| ID | Tên phòng | Sức chứa |
|----|-----------|----------|
|    | A101      | 50       |
|    | B202      | 60       |
|    | C303      | 40       |
|    | Lab01     | 30       |
```

## 🖥️ **Giao diện:**

### Main Features:
- **Bootstrap 5** responsive design
- **FontAwesome** icons
- **Alert messages** cho success/error
- **Confirmation** khi xóa
- **Dropdown menu** cho Excel actions

### Excel Integration:
- **File upload** trong dropdown
- **Template download** 
- **Export button** 
- **Import validation** và feedback

## 🚀 **Cách sử dụng:**

1. **Khởi động app:** `.\mvnw.cmd spring-boot:run`
2. **Truy cập:** `http://localhost:8080/admin/class`
3. **Login admin** (nếu cần)
4. **Quản lý Class** với đầy đủ CRUD + Excel

## ⚡ **Features nổi bật:**

- **Validation real-time** khi tạo/sửa
- **Excel template** với sample data
- **Bulk import** từ Excel
- **Responsive design** cho mobile
- **User-friendly** interface với icons
- **Error handling** đầy đủ

## 🔧 **Technical Stack:**

- **Backend:** Spring Boot 3, JPA/Hibernate
- **Frontend:** JSP, Bootstrap 5, FontAwesome
- **Excel:** Apache POI (XSSF)
- **Database:** MySQL
- **Validation:** Bean Validation

CRUD Class hoàn thành với đầy đủ tính năng! 🎉
