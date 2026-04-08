FROM eclipse-temurin:21-jdk-alpine
ENV LANG C.UTF-8

RUN mkdir -p /opt
ADD maven/opt/application.jar /opt/application.jar


EXPOSE 8080

ENTRYPOINT java -jar /opt/application.jar -Djava.net.preferIPv4Stack=true