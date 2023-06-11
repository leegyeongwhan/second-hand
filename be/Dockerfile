FROM adoptopenjdk:11-jdk-hotspot

ARG JAR_FILE=./be/build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]