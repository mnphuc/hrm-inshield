# Hướng dẫn Sử dụng - HRM Insight

## Mục lục
1. [Giới thiệu](#giới-thiệu)
2. [Đăng nhập hệ thống](#đăng-nhập-hệ-thống)
3. [Tổng quan Dashboard](#tổng-quan-dashboard)
4. [Quản lý Nhân viên](#quản-lý-nhân-viên)
5. [Quản lý Chấm công](#quản-lý-chấm-công)
6. [Quản lý Nghỉ phép](#quản-lý-nghỉ-phép)
7. [Quản lý Lương](#quản-lý-lương)
8. [Quản lý Thiết bị](#quản-lý-thiết-bị)
9. [Quản lý Đối tác](#quản-lý-đối-tác)
10. [Quản lý Chi phí](#quản-lý-chi-phí)
11. [Quản lý Dự án](#quản-lý-dự-án)
12. [Quản lý Người dùng](#quản-lý-người-dùng)

---

## Giới thiệu

**HRM Insight** là hệ thống quản lý nhân sự toàn diện, hỗ trợ các chức năng:
- Quản lý thông tin nhân viên
- Chấm công và theo dõi thời gian làm việc
- Quản lý nghỉ phép
- Tính toán và quản lý lương
- Quản lý thiết bị công ty
- Quản lý đối tác
- Quản lý chi phí
- Quản lý dự án

### Yêu cầu trình duyệt
- Chrome/Edge: Phiên bản mới nhất
- Firefox: Phiên bản mới nhất
- Safari: Phiên bản mới nhất

### Phân quyền người dùng
- **ADMIN**: Toàn quyền quản lý hệ thống
- **HR**: Quản lý nhân sự, chấm công, nghỉ phép
- **MANAGER**: Quản lý nhóm, dự án
- **EMPLOYEE**: Xem thông tin cá nhân, đăng ký nghỉ phép

---

## Đăng nhập hệ thống

### Truy cập hệ thống
1. Mở trình duyệt và truy cập: `http://localhost:8080` (hoặc URL server của bạn)
2. Hệ thống sẽ tự động chuyển đến trang đăng nhập

### Đăng nhập
1. Nhập **Tên đăng nhập** (Username)
2. Nhập **Mật khẩu** (Password)
3. Click nút **Đăng nhập**

### Quên mật khẩu
Liên hệ quản trị viên để được reset mật khẩu.

### Đăng xuất
Click vào menu người dùng ở góc trên bên phải và chọn **Đăng xuất**.

---

## Tổng quan Dashboard

Sau khi đăng nhập, bạn sẽ thấy **Dashboard** - trang tổng quan hệ thống.

### Thông tin hiển thị
- **Thống kê tổng quan**: Số lượng nhân viên, dự án, thiết bị
- **Biểu đồ**: Thống kê theo thời gian
- **Thông báo**: Các thông báo quan trọng
- **Hoạt động gần đây**: Log các hoạt động mới nhất

### Menu điều hướng
Menu bên trái cho phép truy cập các module:
- 📊 Tổng quan
- 👥 Nhân viên
- ⏰ Chấm công
- 🏖️ Nghỉ phép
- 💰 Lương
- 💻 Thiết bị
- 🤝 Đối tác
- 💸 Chi phí
- 📁 Dự án

---

## Quản lý Nhân viên

### Xem danh sách nhân viên
1. Click menu **Nhân viên** → **Quản lý**
2. Danh sách nhân viên hiển thị với các thông tin:
   - Mã nhân viên
   - Họ tên
   - Email
   - Số điện thoại
   - Phòng ban
   - Chức vụ
   - Trạng thái

### Tìm kiếm và lọc
- Sử dụng ô **Tìm kiếm** để tìm theo tên, email, mã nhân viên
- Sử dụng bộ lọc để lọc theo:
  - Phòng ban
  - Chức vụ
  - Trạng thái (Đang làm việc/Nghỉ việc)

### Thêm nhân viên mới
1. Click nút **Thêm mới** (+)
2. Điền thông tin:
   - **Họ và tên** (bắt buộc)
   - **Email** (bắt buộc, phải hợp lệ)
   - **Số điện thoại** (bắt buộc)
   - **Ngày sinh**
   - **Địa chỉ**
   - **Phòng ban**
   - **Chức vụ**
   - **Ngày bắt đầu làm việc**
   - **Lương cơ bản**
3. Click **Lưu**

### Chỉnh sửa thông tin nhân viên
1. Tìm nhân viên cần chỉnh sửa
2. Click nút **Sửa** (biểu tượng bút chì)
3. Cập nhật thông tin
4. Click **Lưu**

### Xóa nhân viên
1. Tìm nhân viên cần xóa
2. Click nút **Xóa** (biểu tượng thùng rác)
3. Xác nhận xóa

**Lưu ý**: Chỉ có thể xóa nhân viên chưa có dữ liệu liên quan (chấm công, lương, v.v.)

### Xem chi tiết nhân viên
Click vào tên nhân viên để xem thông tin chi tiết:
- Thông tin cá nhân
- Lịch sử chấm công
- Lịch sử nghỉ phép
- Lịch sử lương
- Thiết bị được cấp

---

## Quản lý Chấm công

### Xem danh sách chấm công
1. Click menu **Chấm công** → **Quản lý**
2. Danh sách hiển thị:
   - Ngày
   - Nhân viên
   - Giờ vào
   - Giờ ra
   - Số giờ làm việc
   - Trạng thái (Đúng giờ/Muộn/Sớm)

### Lọc theo thời gian
- Chọn **Tháng/Năm** để xem chấm công theo kỳ
- Chọn **Nhân viên** để xem chấm công của một người

### Thêm bản ghi chấm công
1. Click nút **Thêm mới** (+)
2. Chọn:
   - **Nhân viên**
   - **Ngày**
   - **Giờ vào**
   - **Giờ ra**
3. Hệ thống tự động tính số giờ làm việc
4. Click **Lưu**

### Chỉnh sửa chấm công
1. Tìm bản ghi cần sửa
2. Click **Sửa**
3. Cập nhật thông tin
4. Click **Lưu**

### Xóa bản ghi chấm công
1. Tìm bản ghi cần xóa
2. Click **Xóa**
3. Xác nhận

### Xuất báo cáo
1. Chọn kỳ và nhân viên (nếu cần)
2. Click **Xuất Excel**
3. File Excel sẽ được tải về

---

## Quản lý Nghỉ phép

### Xem danh sách đơn nghỉ phép
1. Click menu **Nghỉ phép** → **Quản lý**
2. Danh sách hiển thị:
   - Nhân viên
   - Loại nghỉ phép
   - Ngày bắt đầu
   - Ngày kết thúc
   - Số ngày
   - Trạng thái (Chờ duyệt/Đã duyệt/Từ chối)

### Lọc theo trạng thái
- **Tất cả**: Xem tất cả đơn
- **Chờ duyệt**: Đơn chưa được xử lý
- **Đã duyệt**: Đơn đã được phê duyệt
- **Từ chối**: Đơn bị từ chối

### Tạo đơn nghỉ phép
1. Click nút **Tạo đơn nghỉ phép** (+)
2. Điền thông tin:
   - **Loại nghỉ phép**: Nghỉ phép năm, Nghỉ ốm, Nghỉ việc riêng, v.v.
   - **Ngày bắt đầu**
   - **Ngày kết thúc**
   - **Lý do** (nếu có)
3. Click **Gửi đơn**

### Duyệt đơn nghỉ phép (HR/Admin)
1. Tìm đơn ở trạng thái **Chờ duyệt**
2. Click **Xem chi tiết**
3. Chọn:
   - **Duyệt**: Phê duyệt đơn
   - **Từ chối**: Từ chối đơn (cần nhập lý do)
4. Click **Xác nhận**

### Xem lịch nghỉ phép
1. Click **Lịch nghỉ phép**
2. Xem lịch theo tháng với các ngày nghỉ được đánh dấu

---

## Quản lý Lương

### Xem danh sách bảng lương
1. Click menu **Lương** → **Quản lý**
2. Danh sách hiển thị:
   - Kỳ lương (Tháng/Năm)
   - Nhân viên
   - Lương cơ bản
   - Phụ cấp
   - Thưởng
   - Khấu trừ
   - Tổng lương

### Lọc theo kỳ
- Chọn **Tháng** và **Năm** để xem bảng lương theo kỳ

### Tạo bảng lương mới
1. Click nút **Tạo bảng lương** (+)
2. Chọn:
   - **Kỳ lương** (Tháng/Năm)
   - **Nhân viên**
3. Hệ thống tự động tính:
   - Lương cơ bản
   - Phụ cấp (nếu có)
   - Khấu trừ (bảo hiểm, thuế, v.v.)
4. Nhập **Thưởng** (nếu có)
5. Click **Lưu**

### Chỉnh sửa bảng lương
1. Tìm bảng lương cần sửa
2. Click **Sửa**
3. Cập nhật thông tin
4. Click **Lưu**

### Xuất bảng lương
1. Chọn kỳ lương
2. Click **Xuất Excel**
3. File Excel sẽ được tải về

### Xem chi tiết bảng lương
Click vào một bản ghi để xem:
- Chi tiết các khoản thu nhập
- Chi tiết các khoản khấu trừ
- Tổng kết cuối cùng

---

## Quản lý Thiết bị

### Xem danh sách thiết bị
1. Click menu **Thiết bị** → **Quản lý**
2. Danh sách hiển thị:
   - Mã thiết bị
   - Tên thiết bị
   - Loại thiết bị
   - Nhà cung cấp
   - Ngày mua
   - Giá trị
   - Trạng thái (Sẵn sàng/Đang sử dụng/Bảo trì/Thanh lý)

### Lọc theo trạng thái
- **Tất cả**: Xem tất cả thiết bị
- **Sẵn sàng**: Thiết bị chưa được cấp
- **Đang sử dụng**: Thiết bị đang được nhân viên sử dụng
- **Bảo trì**: Thiết bị đang bảo trì
- **Thanh lý**: Thiết bị đã thanh lý

### Thêm thiết bị mới
1. Click nút **Thêm mới** (+)
2. Điền thông tin:
   - **Tên thiết bị** (bắt buộc)
   - **Loại thiết bị**: Laptop, Máy tính, Điện thoại, v.v.
   - **Nhà cung cấp**
   - **Ngày mua**
   - **Giá trị**
   - **Mô tả**
3. Click **Lưu**

### Cấp phát thiết bị cho nhân viên
1. Tìm thiết bị ở trạng thái **Sẵn sàng**
2. Click **Cấp phát**
3. Chọn **Nhân viên**
4. Nhập **Ngày cấp**
5. Click **Xác nhận**

### Thu hồi thiết bị
1. Tìm thiết bị ở trạng thái **Đang sử dụng**
2. Click **Thu hồi**
3. Nhập **Ngày thu hồi** và **Lý do**
4. Click **Xác nhận**

### Bảo trì thiết bị
1. Tìm thiết bị cần bảo trì
2. Click **Bảo trì**
3. Nhập thông tin bảo trì
4. Click **Xác nhận**

---

## Quản lý Đối tác

### Xem danh sách đối tác
1. Click menu **Đối tác** → **Quản lý**
2. Danh sách hiển thị:
   - Mã đối tác
   - Tên công ty
   - Loại đối tác
   - Người liên hệ
   - Email
   - Số điện thoại
   - Trạng thái

### Thêm đối tác mới
1. Click nút **Thêm mới** (+)
2. Điền thông tin:
   - **Tên công ty** (bắt buộc)
   - **Loại đối tác**: Khách hàng, Nhà cung cấp, Đối tác chiến lược
   - **Mã số thuế**
   - **Địa chỉ**
   - **Người liên hệ**
   - **Email**
   - **Số điện thoại**
   - **Ghi chú**
3. Click **Lưu**

### Chỉnh sửa đối tác
1. Tìm đối tác cần sửa
2. Click **Sửa**
3. Cập nhật thông tin
4. Click **Lưu**

### Xóa đối tác
1. Tìm đối tác cần xóa
2. Click **Xóa**
3. Xác nhận

**Lưu ý**: Chỉ có thể xóa đối tác chưa có dữ liệu liên quan.

---

## Quản lý Chi phí

### Xem danh sách chi phí
1. Click menu **Chi phí** → **Quản lý**
2. Danh sách hiển thị:
   - Ngày
   - Loại chi phí
   - Mô tả
   - Số tiền
   - Người tạo
   - Trạng thái (Chờ duyệt/Đã duyệt/Từ chối)

### Lọc theo thời gian và loại
- Chọn **Tháng/Năm** để lọc theo kỳ
- Chọn **Loại chi phí** để lọc theo loại

### Tạo phiếu chi phí
1. Click nút **Tạo phiếu chi** (+)
2. Điền thông tin:
   - **Loại chi phí**: Văn phòng, Đi lại, Ăn uống, Thiết bị, v.v.
   - **Ngày**
   - **Số tiền**
   - **Mô tả**
   - **Hóa đơn/Chứng từ** (upload file nếu có)
3. Click **Gửi**

### Duyệt phiếu chi phí (Manager/Admin)
1. Tìm phiếu ở trạng thái **Chờ duyệt**
2. Click **Xem chi tiết**
3. Xem thông tin và file đính kèm
4. Chọn:
   - **Duyệt**: Phê duyệt chi phí
   - **Từ chối**: Từ chối (cần nhập lý do)
5. Click **Xác nhận**

### Xuất báo cáo chi phí
1. Chọn kỳ và loại chi phí (nếu cần)
2. Click **Xuất Excel**
3. File Excel sẽ được tải về

---

## Quản lý Dự án

### Xem danh sách dự án
1. Click menu **Dự án** → **Quản lý**
2. Danh sách hiển thị:
   - Mã dự án
   - Tên dự án
   - Khách hàng/Đối tác
   - Ngày bắt đầu
   - Ngày kết thúc
   - Trạng thái (Đang thực hiện/Hoàn thành/Tạm dừng)

### Lọc theo trạng thái
- **Tất cả**: Xem tất cả dự án
- **Đang thực hiện**: Dự án đang tiến hành
- **Hoàn thành**: Dự án đã hoàn thành
- **Tạm dừng**: Dự án tạm dừng

### Tạo dự án mới
1. Click nút **Thêm mới** (+)
2. Điền thông tin:
   - **Tên dự án** (bắt buộc)
   - **Khách hàng/Đối tác**
   - **Ngày bắt đầu**
   - **Ngày kết thúc dự kiến**
   - **Mô tả**
   - **Ngân sách**
3. Click **Lưu**

### Phân công nhân viên vào dự án
1. Tìm dự án cần phân công
2. Click **Phân công nhân viên**
3. Chọn nhân viên và vai trò
4. Nhập **Ngày bắt đầu** và **Ngày kết thúc** (nếu có)
5. Click **Lưu**

### Cập nhật tiến độ dự án
1. Tìm dự án cần cập nhật
2. Click **Cập nhật tiến độ**
3. Nhập **% hoàn thành** và **Ghi chú**
4. Click **Lưu**

### Xem chi tiết dự án
Click vào tên dự án để xem:
- Thông tin dự án
- Danh sách nhân viên tham gia
- Tiến độ
- Chi phí liên quan

---

## Quản lý Người dùng

### Xem danh sách người dùng (Admin only)
1. Click menu **Quản trị** → **Người dùng**
2. Danh sách hiển thị:
   - Tên đăng nhập
   - Email
   - Vai trò
   - Trạng thái (Hoạt động/Khóa)

### Tạo tài khoản mới (Admin only)
1. Click nút **Tạo tài khoản** (+)
2. Điền thông tin:
   - **Tên đăng nhập** (bắt buộc, duy nhất)
   - **Email** (bắt buộc, duy nhất)
   - **Mật khẩu** (bắt buộc, tối thiểu 8 ký tự)
   - **Xác nhận mật khẩu**
   - **Vai trò**: ADMIN, HR, MANAGER, EMPLOYEE
3. Click **Lưu**

### Khóa/Mở khóa tài khoản (Admin only)
1. Tìm tài khoản cần khóa/mở khóa
2. Click **Khóa** hoặc **Mở khóa**
3. Xác nhận

### Đổi mật khẩu
1. Click menu người dùng → **Đổi mật khẩu**
2. Nhập:
   - **Mật khẩu hiện tại**
   - **Mật khẩu mới**
   - **Xác nhận mật khẩu mới**
3. Click **Lưu**

---

## Mẹo sử dụng

### Phím tắt
- **Ctrl + F**: Tìm kiếm trong trang
- **Esc**: Đóng dialog/modal

### Xuất dữ liệu
Hầu hết các trang danh sách đều có nút **Xuất Excel** để xuất dữ liệu ra file Excel.

### Tìm kiếm nhanh
Sử dụng ô tìm kiếm ở đầu mỗi trang để tìm nhanh theo từ khóa.

### Lọc dữ liệu
Sử dụng các bộ lọc để thu hẹp kết quả tìm kiếm.

### Thông báo
Hệ thống sẽ hiển thị thông báo khi:
- Thao tác thành công (màu xanh)
- Có lỗi xảy ra (màu đỏ)
- Cảnh báo (màu vàng)

---

## Câu hỏi thường gặp (FAQ)

### Q: Làm sao để reset mật khẩu?
A: Liên hệ quản trị viên để được reset mật khẩu.

### Q: Tôi không thấy menu nào cả?
A: Kiểm tra quyền truy cập của tài khoản. Một số menu chỉ dành cho ADMIN hoặc HR.

### Q: Làm sao để xuất báo cáo?
A: Ở các trang danh sách, click nút **Xuất Excel** để tải file Excel.

### Q: Tôi có thể xóa nhân viên đã có dữ liệu chấm công không?
A: Không. Bạn cần xóa các dữ liệu liên quan trước (chấm công, lương, v.v.) hoặc chuyển trạng thái nhân viên sang "Nghỉ việc".

### Q: Làm sao để xem lịch sử thay đổi?
A: Một số module có tính năng xem lịch sử. Click vào chi tiết để xem.

---

## Hỗ trợ

Nếu cần hỗ trợ, vui lòng:
1. Kiểm tra tài liệu này trước
2. Liên hệ quản trị viên hệ thống
3. Tạo ticket hỗ trợ (nếu có hệ thống ticket)

---

**Phiên bản tài liệu**: 1.0  
**Cập nhật lần cuối**: 2024

