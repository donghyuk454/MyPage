# My page (����) api ����

## ? Introduction
�ش� ������Ʈ�� �Խ����� �����ϴ� ���� �ʿ��� api �����Դϴ�.

�ܼ� ������ ���� ���� ������Ʈ�̸�, ������ ���񽺸� ���ȭ�ϰ� �ִٴ� �������� �����ϰ� �ֽ��ϴ�.

## ? Stack

**Backend**
- **Language** : Java
- **Library & Framework** : Spring Boot
- **Database** : Mysql
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


## ? Backend Architecture
![image](https://user-images.githubusercontent.com/20418155/182908730-3404c7ff-e968-4a66-9d13-1aaa21981b1d.png)

## ? CI/CD FLow
![image](https://user-images.githubusercontent.com/20418155/182908688-0a0d6e75-de9e-4f03-87bc-f805f1b96545.png)

1. ���� ȯ�濡�� github�� push�ϰ�, github���� ������Ʈ�� pull, merge �մϴ�.
2. github���� jenkins�� webhook�� �����Ͽ� build�� �����մϴ�.
3. jenkins���� �ش� ������Ʈ�� build�Ͽ� jar ������ ����ϴ�
4. �ش� ������Ʈ�� sonarqube�� �����Ͽ� �м��� ��û�մϴ�.
5. Jar ������ Dockerfile, shell script file, appspec.yml ��� �Բ� zip���� �����Ͽ� S3�� upload �մϴ�.
6. Jenkins�� CodeDeploy���� S3�� upload�� ���� ������ ������ �����ϵ��� ��û�մϴ�.
7. Jenkins�� slack ä�η� build ���� �޽����� �����ϴ�.
8. CodeDeploy�� S3���� ���������� ������ appspec.yml�� ���� ������ �����ϸ�, ������ ���� ������ �� ��ũ��Ʈ�� �����մϴ�.


�̷��� �������� ����(8��)�� �Ʒ��� ���� ���ߴ� ���� ������� �����մϴ�.


### ? ���ߴ� ����
1. ������ �������� reverse proxy ������ �ϴ� nginx�� ����ǰ� ������, 8081��Ʈ�� 8082��Ʈ�� ���� ��Ŀ�� �������ִ� ������ ������Ʈ�� �ֽ��ϴ�.
2. Nginx�� 8081��Ʈ�� �����ϰ� �ִ� ������ ������Ʈ(profile=set1)�� �ٶ󺸰� �ֽ��ϴ�.
3. �̷��� ��Ȳ����, nginx�� �ٶ󺸰� ���� ���� 8082��Ʈ�� ������ ������Ʈ(profile=set2)�� �����մϴ�.
4. ������ ������ nginx�� ���Ӱ� ������ 8082��Ʈ�� �ٶ󺸰� �Ǹ�, �̷��� ������ ������ ������ �ݺ��մϴ�.


## �׽�Ʈ Ŀ������
**(�ش� ������ �ڵ�� �ڵ� �ۼ��Ǿ����ϴ�.)**

|Instruction, %|Branch, %|Line, %|Complexity, %|Method, %|
|---|---|---|---|---|
|0|100|0|100|0|