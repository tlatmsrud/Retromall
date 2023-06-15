FROM openjdk:11-jdk
EXPOSE 9999
ARG TARGET=$TARGET
ARG JAR_FILE=build/libs/${TARGET}.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]