#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/service/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME"

echo "> build 파일 복사"
DEPLOY_PATH=/home/ec2-user/service/
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 현재 구동중인 profile 확인"
CURRENT_PROFILE=${curl -s http://localhost/profile}
echo "> $CURRENT_PROFILE"

if [ $CURRENT_PROFILE == set1 ]
then
  IDLE_PROFILE=set2
  IDLE_PORT=8082
  DOCKER_IMAGE="service2"
  DOCKER_CONTAINER="service2"
elif [ $CURRENT_PROFILE == set2 ]
then
  IDLE_PROFILE=set1
  IDLE_PORT=8081
  DOCKER_IMAGE="service1"
  DOCKER_CONTAINER="service1"
else
  echo "> 일치하는 Profile 이 없습니다. Profile: $CURRENT_PROFILE"
  echo "> set1을 할당합니다. IDLE_PROFILE: set1"
  IDLE_PROFILE=set1
  IDLE_PORT=8081
fi

DOCKER_IMAGE="service1"
DOCKER_CONTAINER="service1"

echo "> docker container 삭제"
docker rm -f $DOCKER_CONTAINER

echo "> docker image 삭제"
docker rmi -f $DOCKER_IMAGE

echo "> docker image 빌드"
docker build $DOCKER_IMAGE .

echo "> docker container 운영"
docker run -d -p $IDLE_PORT:$IDLE_PORT --name $DOCKER_CONTAINER $DOCKER_IMAGE --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci


DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포"

nohup java -jar $DEPLOY_JAR >> /home/ec2-user/service/deploy.log 2>/home/ec2-user/service/deploy_err.log &