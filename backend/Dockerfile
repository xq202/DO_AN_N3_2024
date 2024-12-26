# Sử dụng image cơ bản từ OpenJDK
FROM openjdk:17-jdk-alpine

# Cài đặt Maven
RUN apk add --no-cache maven

WORKDIR /app
# Copy mã nguồn vào container
COPY backend/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
