# Task Management API

## 📝 Giới thiệu
Dự án **Task Management** là một ứng dụng **RESTful API** được xây dựng với **Spring Boot** nhằm quản lý người dùng, phân quyền và công việc.  
Hỗ trợ xác thực bằng **JWT** kèm **refresh token** và cơ chế **token blacklist** để quản lý phiên đăng nhập an toàn.  

Ứng dụng thích hợp làm nền tảng cho các hệ thống quản lý công việc, dự án hoặc làm mẫu nghiên cứu về bảo mật trong Spring Boot.

---

## 🚀 Công nghệ sử dụng
- Java 17
- Spring Boot 3 (Web, Security, Data JPA)
- Spring Data JPA + MySQL
- JWT (Nimbus JOSE)
- BCrypt password hashing
- OpenAPI (Swagger)
- Lombok

---

## ⚙️ Chức năng chính
- **Authentication & Authorization**
  - Đăng ký, đăng nhập, đổi mật khẩu, đổi email
  - JWT access token + refresh token lifecycle
  - Blacklist token khi logout
  - Role-based & permission-based access control
- **Quản lý người dùng**
  - Tạo, sửa, xóa, lấy danh sách, lấy thông tin chi tiết
- **Quản lý công việc (Task)**
  - Tạo, sửa, xóa task
  - Lấy danh sách tất cả task (hỗ trợ phân trang)
  - Lấy task theo user
- **Quản lý role & permission** (CRUD cơ bản)

---

## 🔑 API Endpoints (ví dụ)
- `POST   /api/auth/log-in` — Đăng nhập
- `POST   /api/auth/refresh` — Refresh token
- `POST   /api/users/register` — Đăng ký user
- `GET    /api/tasks/get-all` — Danh sách task (phân trang)
- `POST   /api/tasks/create` — Tạo task mới

> Truy cập tài liệu API qua **Swagger/OpenAPI** tại:  
`http://localhost:8080/api/swagger-ui.html`

---

## 🛠️ Cách chạy dự án

1. **Chuẩn bị cơ sở dữ liệu**
   - Cài MySQL
   - Tạo database: `task_mgmt_db`
   - Cập nhật thông tin kết nối trong `src/main/resources/application.yaml` nếu cần

2. **Chạy bằng Maven**
   ```bash
   ./mvnw spring-boot:run
   ```
 - hoặc build:
   ```bash
   mvn clean package
   java -jar target/task-management-0.0.1-SNAPSHOT.jar
   ```
3. **Mặc định server chạy tại**
  `http://localhost:8080/api`
