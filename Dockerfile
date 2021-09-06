FROM openjdk:8-jdk-alpine
RUN mkdir -p /data/inquiry
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown spring /data/inquiry
USER spring:spring
ARG JAR_FILE=eTest/build/libs/eTest-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]