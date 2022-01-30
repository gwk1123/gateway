FROM openjdk:8
VOLUME /data/gateway
ADD gateway-0.0.1-SNAPSHOT.jar gateway.jar
EXPOSE 8091
#修改Docker时区-否则里面的时间会慢8小时 通过文件链接的方式修改 时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-jar","/gateway.jar"]
