# Stage 1: Build
FROM amazoncorretto:21-alpine AS builder
WORKDIR /app
COPY src ./src
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# Stage 2: Test
FROM builder AS test
RUN ./gradlew test

# Stage 3: Release
FROM amazoncorretto:21-alpine AS release
WORKDIR /app
COPY --from=builder /app/build/libs/nauta-app.jar /app/nauta-app.jar
ENTRYPOINT ["java", "-jar", "/app/nauta-app.jar"]
