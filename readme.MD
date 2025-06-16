# 1. Tải (pull) image MySQL phiên bản 9.2.0 sử dụng hệ điều hành Oracle Linux 9 từ Docker Hub
docker pull mysql:9.2.0-oraclelinux9

# 2. Tạo và chạy một container MySQL:
#    - Đặt tên container là "mysql-9.2.0"
#    - Mở cổng 3306 trên máy host và ánh xạ đến cổng 3306 trong container
#    - Thiết lập mật khẩu cho tài khoản root là "root123"
#    - Chạy container ở chế độ nền (detached mode)
docker run --name mysql-9.2.0 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root123 -d mysql:9.2.0-oraclelinux9

# 3. Kiểm tra trạng thái của container MySQL:
docker ps -a

# 4. Kết nối đến MySQL trong container:
docker exec -it mysql-9.2.0 mysql -uroot -proot123

# 5. Tạo cơ sở dữ liệu và người dùng:
CREATE DATABASE user_management;

# 6. Thoát khỏi MySQL:
exit;

# 7. Khởi động ứng dụng Spring Boot
#    (Ứng dụng sẽ tự động kết nối đến MySQL và thực thi file data.sql để tạo bảng và dữ liệu mẫu)
mvn spring-boot:run

# 8. Truy cập giao diện Swagger để thử nghiệm các API
#    URL: http://localhost:8080/user-management/swagger-ui/index.html

