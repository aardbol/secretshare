FROM openjdk:11-jre-slim-buster
RUN groupadd --system spring && useradd --system spring -g spring
USER spring:spring
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ${JAR_FILE} /app/secretshareapi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "/app/secretshareapi.jar"]
