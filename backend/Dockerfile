# Sử dụng image cơ bản từ OpenJDK
FROM openjdk:17-jdk-alpine

# Cài đặt Maven
RUN apk add --no-cache maven

# Tạo thư mục làm việc
WORKDIR /app

# Copy mã nguồn vào container
COPY . .

EXPOSE 8080

ENTRYPOINT ["mvn", "spring-boot:run"]