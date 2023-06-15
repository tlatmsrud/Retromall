FROM openjdk:11-jdk
EXPOSE 9999
# ARG VERSION
ARG JAR_FILE=build/*.jar
# ENV TAG=${VERSION}
ADD ${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]
# RUN mkdir /app
# WORKDIR /app
# COPY ./$JAR_FILE /app
# RUN mkdir /app
# WORKDIR /app
# COPY ./$JAR_FILE /app