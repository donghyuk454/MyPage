# My page (몽몽) api 서버

## 🖐 Introduction
해당 프로젝트는 게시판을 구현하는 데에 필요한 api 서버입니다.

단순 개발을 위한 토이 프로젝트이며, 실제로 서비스를 상용화하고 있다는 마음으로 개발하고 있습니다.

## 🔧 Stack

**Backend**
- **Language** : Java
- **Library & Framework** : Spring Boot
- **Database** : Mysql, Redis (Session)
- **ORM** : JPA
- **Build** : Gradle
- **Test** : Junit5, Mockito

**Devops**
- **Deploy** : EC2, Docker, Nginx
- **CI/CD** : Jenkins, CodeDeploy, S3
- **Code Analysis** : Sonarqube, Jacoco

**Etc**
- **IDE** : Intellij
- **Collection** : Git, Github
- **Message** : Slack


## 🔨 Backend Architecture
![image](https://user-images.githubusercontent.com/20418155/187032132-84405343-4730-4c0c-9b27-4d934af3cd13.png)

## 🔨 CI/CD FLow
![image](https://user-images.githubusercontent.com/20418155/182908688-0a0d6e75-de9e-4f03-87bc-f805f1b96545.png)

1. 개발 환경에서 github 에 push 하고, github 에서 프로젝트를 pull, merge 합니다.
2. github 에서 jenkins 에 webhook 을 전달하여 build 를 유발합니다.
3. jenkins 에서 해당 프로젝트를 build 하여 jar 파일을 만듭니다
4. 해당 프로젝트를 sonarqube 로 전달하여 분석을 요청합니다.
5. Jar 파일을 Dockerfile, shell script file, appspec.yml 등과 함께 zip 으로 압축하여 S3 에 upload 합니다.
6. Jenkins 가 CodeDeploy 에게 S3에 upload 된 압축 파일을 서버에 배포하도록 요청합니다.
7. Jenkins 가 slack 채널로 build 상태 메시지를 보냅니다.
8. CodeDeploy 가 S3에서 압축파일을 가져와 appspec.yml 에 따라서 배포를 수행하며, 배포할 서버 내에서 쉘 스크립트를 수행합니다.


이러한 과정에서 배포(8번)는 아래와 같은 무중단 배포 방식으로 진행합니다.


### 🚴 무중단 배포
1. 배포할 서버에는 reverse proxy 역할을 하는 nginx 가 실행되고 있으며, 8081포트와 8082포트에 각각 도커로 감싸져있는 스프링 프로젝트가 있습니다.
2. Nginx 는 8081포트로 실행하고 있는 스프링 프로젝트(profile=set1)를 바라보고 있습니다.
3. 이러한 상황에서, nginx 가 바라보고 있지 않은 8082포트로 스프링 프로젝트(profile=set2)를 배포합니다.
4. 배포가 끝나면 nginx 는 새롭게 배포된 8082포트를 바라보게 되며, 이러한 과정을 배포할 때마다 반복합니다.


## 📋 테스트 커버리지
**(해당 내용은 코드로 자동 작성되었습니다.)**

|Instruction, %|Branch, %|Line, %|Complexity, %|Method, %|
|---|---|---|---|---|
|89|79|93|80|83|