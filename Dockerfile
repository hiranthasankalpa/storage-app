FROM openjdk:21-jdk
WORKDIR /app
COPY target/*.jar storage-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "storage-app.jar"]