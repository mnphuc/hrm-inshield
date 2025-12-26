# Dữ liệu Mẫu - HRM Insight

File này mô tả dữ liệu mẫu được tự động tạo khi chạy Liquibase migration `0004-insert-sample-data.yaml`.

## Tài khoản Đăng nhập

Tất cả tài khoản mẫu có mật khẩu mặc định: **`password`**

### Tài khoản Admin
- **Username**: `admin`
- **Email**: `admin@techshield.vn`
- **Vai trò**: ADMIN
- **Mật khẩu**: `password`

### Tài khoản HR
- **Username**: `hr_manager`
- **Email**: `hr@techshield.vn`
- **Vai trò**: HR
- **Mật khẩu**: `password`

### Tài khoản Manager
- **Username**: `manager1`
- **Email**: `manager1@techshield.vn`
- **Vai trò**: MANAGER
- **Mật khẩu**: `password`

### Tài khoản Employee
- **Username**: `employee1`, `employee2`, `employee3`
- **Email**: `employee1@techshield.vn`, `employee2@techshield.vn`, `employee3@techshield.vn`
- **Vai trò**: EMPLOYEE
- **Mật khẩu**: `password`

## Dữ liệu Mẫu

### Nhân viên (8 nhân viên)
1. **EMP001** - Nguyễn Văn An - Trưởng phòng Nhân sự
2. **EMP002** - Trần Thị Bình - Trưởng nhóm Phát triển
3. **EMP003** - Lê Văn Cường - Lập trình viên
4. **EMP004** - Phạm Thị Dung - Lập trình viên
5. **EMP005** - Hoàng Văn Em - Nhân viên Kinh doanh
6. **EMP006** - Võ Thị Phương - Kế toán viên
7. **EMP007** - Đặng Văn Giang - Lập trình viên Senior
8. **EMP008** - Bùi Thị Hoa - Chuyên viên Marketing

### Dự án (4 dự án)
1. **PRJ001** - Hệ thống Quản lý Nhân sự (Đang thực hiện)
2. **PRJ002** - Website Thương mại Điện tử (Đang thực hiện)
3. **PRJ003** - Ứng dụng Mobile Banking (Đã hoàn thành)
4. **PRJ004** - Hệ thống CRM (Đang thực hiện)

### Đối tác (3 đối tác)
1. **PTN001** - Công ty Cổ phần Công nghệ ABC (Khách hàng)
2. **PTN002** - Công ty TNHH Thiết bị XYZ (Nhà cung cấp)
3. **PTN003** - Công ty Cổ phần Dịch vụ DEF (Khác)

### Thiết bị (6 thiết bị)
1. **EQP001** - Laptop Dell XPS 15 (Đã phân bổ cho EMP003)
2. **EQP002** - Laptop MacBook Pro M2 (Đã phân bổ cho EMP004)
3. **EQP003** - Laptop ThinkPad X1 Carbon (Đã phân bổ cho EMP002)
4. **EQP004** - Màn hình Dell UltraSharp 27" (Có sẵn)
5. **EQP005** - Điện thoại iPhone 15 Pro (Đã phân bổ cho EMP002)
6. **EQP006** - Bàn phím cơ Keychron K8 (Đang bảo trì)

### Chấm công
- Dữ liệu chấm công mẫu cho các nhân viên trong 30 ngày gần nhất
- Bao gồm các trường hợp: đúng giờ, muộn, tăng ca

### Nghỉ phép
- 4 đơn nghỉ phép mẫu với các trạng thái khác nhau:
  - Đã duyệt
  - Chờ duyệt
  - Từ chối

### Lương
- Bảng lương mẫu cho 3 tháng gần nhất
- Bao gồm lương cơ bản, tăng ca, thưởng, khấu trừ

### Chi phí
- 5 phiếu chi phí mẫu với các loại:
  - Đi lại (TRAVEL)
  - Ăn uống (MEALS)
  - Văn phòng phẩm (OFFICE_SUPPLIES)
  - Thiết bị (EQUIPMENT)

## Lưu ý

⚠️ **CẢNH BÁO BẢO MẬT**: 
- Dữ liệu mẫu này chỉ dùng cho môi trường development/testing
- **KHÔNG** sử dụng trong môi trường production
- Đổi mật khẩu ngay sau khi deploy lên môi trường thực tế

## Cách Xóa Dữ liệu Mẫu

Nếu muốn xóa dữ liệu mẫu, có thể:

1. **Xóa thủ công từ database**:
   ```sql
   DELETE FROM expenses WHERE employee_id IN (SELECT id FROM employees WHERE code LIKE 'EMP%');
   DELETE FROM payrolls WHERE employee_id IN (SELECT id FROM employees WHERE code LIKE 'EMP%');
   DELETE FROM leave_requests WHERE employee_id IN (SELECT id FROM employees WHERE code LIKE 'EMP%');
   DELETE FROM attendance_records WHERE employee_id IN (SELECT id FROM employees WHERE code LIKE 'EMP%');
   DELETE FROM project_assignments WHERE project_id IN (SELECT id FROM projects WHERE code LIKE 'PRJ%');
   DELETE FROM projects WHERE code LIKE 'PRJ%';
   DELETE FROM equipment WHERE code LIKE 'EQP%';
   DELETE FROM partners WHERE code LIKE 'PTN%';
   DELETE FROM employees WHERE code LIKE 'EMP%' AND code != 'EMP001';
   DELETE FROM users WHERE username IN ('hr_manager', 'manager1', 'employee1', 'employee2', 'employee3');
   ```

2. **Hoặc rollback Liquibase changeset**:
   ```bash
   # Sử dụng Liquibase rollback command
   ```

## Cập nhật

Dữ liệu mẫu được tạo tự động khi chạy migration `0004-insert-sample-data.yaml`. 
Để thêm hoặc sửa dữ liệu mẫu, chỉnh sửa file đó và chạy lại migration.

