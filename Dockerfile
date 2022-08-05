FROM openjdk:11
ARG JAR_FILE=build/libs/MyProject-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /home/ec2-user/service/app.jar
ENV ENV_IDLE_PROFILE 9000
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${ENV_IDLE_PROFILE}", "/home/ec2-user/service/app.jar"]