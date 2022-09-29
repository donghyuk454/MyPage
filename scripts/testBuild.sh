#!/bin/bash

LOG_PATH="C:\Users\dongh\spring-project\MyProject\scripts\log\testBuild.log"
rm ${LOG_PATH}
sleep 2

cd C:/Users/dongh/spring-project/MyProject

echo "> test 시작" >> ${LOG_PATH}
./gradlew test >> ${LOG_PATH}
sleep 5

echo "> server 구동" >> ${LOG_PATH}
nohup ./gradlew bootRun >> ${LOG_PATH} &
sleep 60

echo "> readme 작성" >> ${LOG_PATH}

curl -I http://localhost:9000/readme >> ${LOG_PATH} &

echo "> server 종료" >> ${LOG_PATH}
./gradlew --stop >> ${LOG_PATH}
rm -rf %USER_HOME%\.gradle/caches/ >> ${LOG_PATH}


echo "> github 에 업로드" >> ${LOG_PATH}
git add ./README.md

git commit -m "docs: 테스트 커버리지 수정"

git push origin release/1