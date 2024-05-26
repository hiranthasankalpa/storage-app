#FROM openjdk:21-jdk
#WORKDIR /app
#COPY /*.jar storage-app.jar
#EXPOSE 8080
#ENTRYPOINT ["ls"]

FROM maven:3.9.6-eclipse-temurin-21-alpine
WORKDIR /storage-app
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run