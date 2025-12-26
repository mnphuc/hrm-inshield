# Sử dụng JDK để build project (nếu muốn build từ source)
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Build arguments để force rebuild khi version thay đổi
# Thay đổi BUILD_VERSION trong docker-compose.yml để force rebuild
ARG BUILD_VERSION=1.0.0
ENV BUILD_VERSION=${BUILD_VERSION}

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Tạo image chạy từ file jar đã build
FROM eclipse-temurin:17-jre

# Build arguments
ARG BUILD_VERSION=1.0.0
ENV BUILD_VERSION=${BUILD_VERSION}

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose port (sửa lại nếu app chạy port khác 8080)
EXPOSE 8080

# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
