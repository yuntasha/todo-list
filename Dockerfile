FROM eclipse-temurin:17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=build/libs/todo-list-0.0.1-SNAPSHOT.jar
COPY --from=builder /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
