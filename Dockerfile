FROM openjdk:11-jdk
ARG VERSION
ARG JAR_FILE=target/*.jar
ENV TAG=${VERSION}
ADD ${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]
# RUN mkdir /app
# WORKDIR /app
# COPY ./$JAR_FILE /app