FROM openjdk:13-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/mainmicroservice-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} main.jar
ENTRYPOINT ["java","-jar","main.jar"]