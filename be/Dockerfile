FROM adoptopenjdk:11-jdk-hotspot

ARG JAR_FILE=./build/libs/secondhand-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]