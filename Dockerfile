FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ARG IDLE_PROFILE
ENV ENV_IDLE_PROFILE=&IDLE_PROFILE
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${ENV_IDLE_PROFILE}", "/app.jar"]