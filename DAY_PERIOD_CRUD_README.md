# CRUD Day & Period Management - Hướng dẫn sử dụng

## 🎯 **Tính năng đã implement:**

### 📅 **Day Management**
- ✅ **CRUD Operations:** Create, Read, Update, Delete
- ✅ **Excel Import/Export** với validation
- ✅ **Template Download** với sample data
- ✅ **Validation:** Tên ngày unique, số thứ tự > 0

### ⏰ **Period Management**  
- ✅ **CRUD Operations:** Create, Read, Update, Delete
- ✅ **Excel Import/Export** với relationship Day
- ✅ **Template Download** với sample data
- ✅ **Day Relationship:** Sử dụng findDayByName như phong cách project
- ✅ **Validation:** Tên tiết unique trong cùng ngày

## 🔗 **URLs để truy cập:**

### Day Management:
- **Danh sách:** `http://localhost:8080/admin/day`
- **Tạo mới:** `http://localhost:8080/admin/day/create`
- **Cập nhật:** `http://localhost:8080/admin/day/update/{id}`
- **Xóa:** `http://localhost:8080/admin/day/delete/{id}`
- **Export Excel:** `http://localhost:8080/admin/day/export`
- **Template:** `http://localhost:8080/admin/day/template`

### Period Management:
- **Danh sách:** `http://localhost:8080/admin/period`
- **Tạo mới:** `http://localhost:8080/admin/period/create`
- **Cập nhật:** `http://localhost:8080/admin/period/update/{id}`
- **Xóa:** `http://localhost:8080/admin/period/delete/{id}`
- **Export Excel:** `http://localhost:8080/admin/period/export`
- **Template:** `http://localhost:8080/admin/period/template`

## 💾 **Database Schema:**

### Day Table:
```sql
CREATE TABLE `day` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `number` INT NOT NULL,
    UNIQUE KEY `unique_name` (`name`)
);
```

### Period Table:
```sql
CREATE TABLE `period` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `number` INT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `start` INT NOT NULL,
    `end` INT NOT NULL,
    `day` BIGINT,
    FOREIGN KEY (`day`) REFERENCES `day`(`id`)
);
```

## 📋 **Validation Rules:**

### Day Entity:
- **Name:** Không được để trống, phải unique
- **Number:** Phải > 0

### Period Entity:
- **Number:** Phải > 0
- **Name:** Không được để trống, unique trong cùng ngày
- **Start/End:** Format HHMM (ví dụ: 700, 1430)
- **Day:** Phải chọn ngày hợp lệ

## 🎯 **Sample Data:**

### Day Sample:
```
| ID | Tên ngày | Số thứ tự |
|----|----------|-----------|
|    | Thứ 2    | 2         |
|    | Thứ 3    | 3         |
|    | Thứ 4    | 4         |
|    | Thứ 5    | 5         |
|    | Thứ 6    | 6         |
|    | Thứ 7    | 7         |
|    | Chủ nhật | 1         |
```

### Period Sample:
```
| ID | Số tiết | Tên tiết | Giờ bắt đầu | Giờ kết thúc | Ngày    |
|----|---------|----------|-------------|--------------|---------|
|    | 1       | Tiết 1   | 700         | 750          | Thứ 2   |
|    | 2       | Tiết 2   | 800         | 850          | Thứ 2   |
|    | 3       | Tiết 3   | 900         | 950          | Thứ 2   |
|    | 4       | Tiết 4   | 1000        | 1050         | Thứ 2   |
```

## ⚡ **Key Features:**

### Day Management:
- **Simple CRUD** với validation cơ bản
- **Excel operations** với error handling
- **Responsive UI** với Bootstrap 5

### Period Management:
- **Day Relationship** bằng dropdown selection
- **findDayByName()** theo phong cách project
- **Time format** HHMM cho start/end
- **Advanced validation** cho relationship

## 🔧 **Technical Implementation:**

### Day Service Features:
- **CRUD operations** với JPA Repository
- **Excel Export** với Apache POI
- **Excel Import** với parsing validation
- **Template generation** với sample data

### Period Service Features:
- **CRUD operations** với Day relationship
- **Excel Export** bao gồm Day name
- **Excel Import** với Day lookup by name
- **Validation** cho tên trùng trong cùng ngày

### Controller Features:
- **REST endpoints** cho tất cả operations
- **Form handling** với validation
- **Excel endpoints** cho import/export/template
- **Error handling** với RedirectAttributes

### View Features:
- **Responsive tables** với Bootstrap
- **Modal dialogs** cho Excel import
- **Dropdown menus** cho Excel actions
- **Form validation** với error display
- **Day dropdown** trong Period forms

## 🚀 **Cách sử dụng:**

1. **Tạo Days trước:** Thứ 2, Thứ 3, etc.
2. **Tạo Periods:** Chọn Day từ dropdown
3. **Import Excel:** Sử dụng template có sẵn
4. **Export data:** Download Excel cho backup

## 📊 **Excel Integration:**

- **Template download** với sample data
- **Import validation** với error messages
- **Export formatting** với auto-sized columns
- **CSRF protection** cho security

**Day & Period CRUD hoàn thành với đầy đủ relationship và Excel features!** 🎉
