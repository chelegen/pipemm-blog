FROM java:openjdk-8u111-alpine

RUN mkdir /app

WORKDIR /app

COPY target/spring-boot-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java","-jar","spring-boot-0.0.1-SNAPSHOT.jar"]