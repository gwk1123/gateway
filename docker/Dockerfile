FROM openjdk:8
VOLUME /date/gateway
ADD gateway-0.0.1-SNAPSHOT.jar gateway.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar","/gateway.jar"]
