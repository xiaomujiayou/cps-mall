FROM openjdk:8-alpine

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

ADD target/*.jar target.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/target.jar","--spring.profiles.active=prod,plugin,api-prod","-Duser.timezone=GMT+08"]