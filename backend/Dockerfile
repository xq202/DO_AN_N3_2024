# Sử dụng image cơ bản từ OpenJDK
FROM openjdk:17-jdk-alpine

# Cài đặt Maven
RUN apk add --no-cache maven

# Copy mã nguồn vào container
COPY . .

EXPOSE 8080

ENTRYPOINT ["./mvnw", "spring-boot:run"]
