FROM openjdk:12-jdk-alpine
RUN apk add --no-cache bash
WORKDIR /gone-backend
CMD ./gradlew run