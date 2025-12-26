# Tài liệu Vận hành - HRM Insight

## Mục lục
1. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
2. [Cài đặt và Triển khai](#cài-đặt-và-triển-khai)
3. [Cấu hình](#cấu-hình)
4. [Vận hành](#vận-hành)
5. [Bảo trì](#bảo-trì)
6. [Xử lý sự cố](#xử-lý-sự-cố)

---

## Yêu cầu hệ thống

### Phần mềm yêu cầu
- **Java**: JDK 17 hoặc cao hơn
- **Maven**: 3.6+ (để build project)
- **Docker**: 20.10+ và Docker Compose 2.0+ (khuyến nghị)
- **MariaDB**: 11.0+ (hoặc MySQL 8.0+)

### Tài nguyên phần cứng tối thiểu
- **CPU**: 2 cores
- **RAM**: 4GB
- **Ổ cứng**: 20GB trống

### Tài nguyên phần cứng khuyến nghị
- **CPU**: 4 cores
- **RAM**: 8GB
- **Ổ cứng**: 50GB trống

---

## Cài đặt và Triển khai

### Phương pháp 1: Sử dụng Docker Compose (Khuyến nghị)

#### Bước 1: Clone repository
```bash
git clone <repository-url>
cd hrm-inshield
```

#### Bước 2: Cấu hình môi trường
Chỉnh sửa file `docker-compose.yml` nếu cần thay đổi:
- Port của ứng dụng (mặc định: 8080)
- Port của database (mặc định: 3307)
- Mật khẩu database
- Tên database

#### Bước 3: Khởi động hệ thống
```bash
docker-compose up -d
```

Lệnh này sẽ:
- Build Docker image cho ứng dụng
- Tạo và khởi động container MariaDB
- Tạo và khởi động container ứng dụng
- Tự động chạy Liquibase migrations để tạo database schema

#### Bước 4: Kiểm tra trạng thái
```bash
# Kiểm tra logs
docker-compose logs -f app

# Kiểm tra trạng thái containers
docker-compose ps
```

#### Bước 5: Truy cập ứng dụng
Mở trình duyệt và truy cập: `http://localhost:8080`

### Phương pháp 2: Cài đặt thủ công

#### Bước 1: Cài đặt MariaDB
```bash
# Trên Ubuntu/Debian
sudo apt-get update
sudo apt-get install mariadb-server

# Khởi động MariaDB
sudo systemctl start mariadb
sudo systemctl enable mariadb

# Tạo database
sudo mysql -u root -p
CREATE DATABASE techshield_insight;
CREATE USER 'techshield_user'@'localhost' IDENTIFIED BY 'techshield_pass';
GRANT ALL PRIVILEGES ON techshield_insight.* TO 'techshield_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### Bước 2: Build ứng dụng
```bash
# Build JAR file
./mvnw clean package -DskipTests

# Hoặc trên Windows
mvnw.cmd clean package -DskipTests
```

File JAR sẽ được tạo tại: `target/insight-0.0.1-SNAPSHOT.jar`

#### Bước 3: Cấu hình ứng dụng
Chỉnh sửa file `src/main/resources/application.properties`:
```properties
# Cấu hình database
spring.datasource.url=jdbc:mariadb://localhost:3306/techshield_insight
spring.datasource.username=techshield_user
spring.datasource.password=techshield_pass

# Cấu hình email (nếu cần)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

#### Bước 4: Chạy ứng dụng
```bash
java -jar target/insight-0.0.1-SNAPSHOT.jar
```

Ứng dụng sẽ tự động:
- Kết nối database
- Chạy Liquibase migrations
- Khởi động trên port 8080

---

## Cấu hình

### Cấu hình Database

File: `src/main/resources/application.properties`

```properties
# Database connection
spring.datasource.url=jdbc:mariadb://host:port/database_name
spring.datasource.username=username
spring.datasource.password=password

# JPA settings
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
```

### Cấu hình Email

```properties
# SMTP configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Lưu ý**: Với Gmail, cần sử dụng "App Password" thay vì mật khẩu thông thường.

### Cấu hình Docker

File: `docker-compose.yml`

```yaml
services:
  app:
    ports:
      - "8080:8080"  # Thay đổi port nếu cần
    environment:
      SPRING_DATASOURCE_URL: jdbc:mariadb://db:3306/techshield_insight
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: your_password
  
  db:
    ports:
      - "3307:3306"  # Port mapping cho database
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: techshield_insight
```

### Biến môi trường

Có thể override cấu hình bằng biến môi trường:

```bash
export SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3306/techshield_insight
export SPRING_DATASOURCE_USERNAME=techshield_user
export SPRING_DATASOURCE_PASSWORD=techshield_pass
```

---

## Vận hành

### Khởi động hệ thống

#### Với Docker:
```bash
docker-compose up -d
```

#### Không dùng Docker:
```bash
java -jar target/insight-0.0.1-SNAPSHOT.jar
```

### Dừng hệ thống

#### Với Docker:
```bash
docker-compose down
```

#### Không dùng Docker:
Nhấn `Ctrl+C` hoặc kill process

### Khởi động lại hệ thống

#### Với Docker:
```bash
docker-compose restart
```

#### Không dùng Docker:
Dừng và khởi động lại như trên

### Xem logs

#### Với Docker:
```bash
# Xem logs ứng dụng
docker-compose logs -f app

# Xem logs database
docker-compose logs -f db

# Xem tất cả logs
docker-compose logs -f
```

#### Không dùng Docker:
Logs được in ra console. Có thể redirect vào file:
```bash
java -jar app.jar > app.log 2>&1 &
```

### Backup Database

#### Với Docker:
```bash
# Backup
docker exec insight_db mysqldump -u root -p281003 techshield_insight > backup_$(date +%Y%m%d_%H%M%S).sql

# Restore
docker exec -i insight_db mysql -u root -p281003 techshield_insight < backup_file.sql
```

#### Không dùng Docker:
```bash
# Backup
mysqldump -u techshield_user -p techshield_insight > backup_$(date +%Y%m%d_%H%M%S).sql

# Restore
mysql -u techshield_user -p techshield_insight < backup_file.sql
```

### Kiểm tra trạng thái

#### Kiểm tra ứng dụng:
```bash
# Với Docker
curl http://localhost:8080

# Hoặc mở trình duyệt
http://localhost:8080
```

#### Kiểm tra database:
```bash
# Với Docker
docker exec -it insight_db mysql -u root -p281003

# Không dùng Docker
mysql -u techshield_user -p techshield_insight
```

---

## Bảo trì

### Cập nhật ứng dụng

#### Với Docker:
```bash
# Pull code mới
git pull

# Rebuild và restart
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

#### Không dùng Docker:
```bash
# Pull code mới
git pull

# Build lại
./mvnw clean package -DskipTests

# Dừng ứng dụng cũ và chạy JAR mới
java -jar target/insight-0.0.1-SNAPSHOT.jar
```

### Database Migrations

Liquibase tự động chạy migrations khi ứng dụng khởi động. Các file migration nằm tại:
- `src/main/resources/db/changelog/changes/`

Để thêm migration mới:
1. Tạo file YAML mới trong thư mục `changes/`
2. Thêm reference vào `db.changelog-master.yaml`
3. Khởi động lại ứng dụng

### Dọn dẹp logs

```bash
# Với Docker
docker-compose logs --tail=100 > recent_logs.txt
docker-compose down
docker-compose up -d

# Xóa logs cũ
docker system prune -f
```

### Giám sát tài nguyên

```bash
# Với Docker
docker stats

# Kiểm tra disk usage
docker system df
```

---

## Xử lý sự cố

### Ứng dụng không khởi động

**Triệu chứng**: Container/process không chạy hoặc crash ngay lập tức

**Giải pháp**:
1. Kiểm tra logs:
   ```bash
   docker-compose logs app
   ```

2. Kiểm tra port đã được sử dụng:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   
   # Linux/Mac
   lsof -i :8080
   ```

3. Kiểm tra cấu hình database:
   - Đảm bảo database đang chạy
   - Kiểm tra thông tin kết nối trong `application.properties`

### Lỗi kết nối database

**Triệu chứng**: `Connection refused` hoặc `Access denied`

**Giải pháp**:
1. Kiểm tra database đang chạy:
   ```bash
   docker-compose ps db
   ```

2. Kiểm tra thông tin đăng nhập:
   ```bash
   docker exec -it insight_db mysql -u root -p
   ```

3. Kiểm tra network (với Docker):
   ```bash
   docker network ls
   docker network inspect hrm-inshield_default
   ```

### Lỗi migration database

**Triệu chứng**: `LiquibaseException` hoặc schema không đúng

**Giải pháp**:
1. Kiểm tra logs chi tiết:
   ```bash
   docker-compose logs app | grep -i liquibase
   ```

2. Kiểm tra file migration:
   - Xem file YAML có syntax đúng không
   - Kiểm tra `db.changelog-master.yaml` có reference đúng không

3. Reset database (CẨN THẬN - mất dữ liệu):
   ```bash
   docker-compose down -v
   docker-compose up -d
   ```

### Ứng dụng chạy chậm

**Triệu chứng**: Response time cao, timeout

**Giải pháp**:
1. Kiểm tra tài nguyên:
   ```bash
   docker stats
   ```

2. Kiểm tra database:
   - Xem có query chậm không
   - Kiểm tra indexes

3. Tăng tài nguyên:
   - Tăng memory cho container
   - Scale database nếu cần

### Lỗi email không gửi được

**Triệu chứng**: Email không được gửi, lỗi SMTP

**Giải pháp**:
1. Kiểm tra cấu hình email trong `application.properties`
2. Với Gmail:
   - Sử dụng App Password thay vì mật khẩu thông thường
   - Bật "Less secure app access" (không khuyến nghị)
3. Kiểm tra firewall/network có chặn port 587 không

### Port đã được sử dụng

**Triệu chứng**: `Address already in use`

**Giải pháp**:
1. Tìm process đang dùng port:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   
   # Linux/Mac
   lsof -i :8080
   kill -9 <PID>
   ```

2. Hoặc đổi port trong `docker-compose.yml` hoặc `application.properties`

### Container không dừng được

**Triệu chứng**: `docker-compose down` không hoạt động

**Giải pháp**:
```bash
# Force stop
docker-compose kill

# Force remove
docker-compose rm -f

# Hoặc
docker stop insight_web insight_db
docker rm insight_web insight_db
```

---

## Bảo mật

### Khuyến nghị bảo mật

1. **Đổi mật khẩu mặc định**: 
   - Đổi mật khẩu database trong production
   - Sử dụng mật khẩu mạnh

2. **Cấu hình HTTPS**: 
   - Sử dụng reverse proxy (Nginx, Apache) với SSL certificate
   - Hoặc cấu hình Spring Boot với SSL

3. **Bảo vệ thông tin nhạy cảm**:
   - Không commit file `application.properties` với thông tin thật
   - Sử dụng environment variables hoặc secrets management

4. **Firewall**:
   - Chỉ mở port cần thiết
   - Giới hạn truy cập database từ bên ngoài

5. **Backup định kỳ**:
   - Tự động hóa backup database
   - Lưu backup ở nơi an toàn

---

## Liên hệ hỗ trợ

Nếu gặp vấn đề không được giải quyết trong tài liệu này, vui lòng:
1. Kiểm tra logs chi tiết
2. Tạo issue trên repository
3. Liên hệ team phát triển

---

**Phiên bản tài liệu**: 1.0  
**Cập nhật lần cuối**: 2024

