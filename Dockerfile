#FROM openjdk:12-jdk-alpine
#
#RUN apk add --no-cache bash
#
#WORKDIR /backend
#
#CMD ["sh","-c","./gradlew run && ./gradlew -t installDist"]

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/*.jar /app/backend.jar
ENTRYPOINT ["java","-jar","/app/backend.jar"]