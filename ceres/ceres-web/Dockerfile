FROM eclipse-temurin:17.0.12_7-jdk-alpine

LABEL authors="JinBooks <support@jinbooks.com>"

RUN mkdir -p /jinbooks

WORKDIR /jinbooks

ARG JAR_FILE=build/libs/jinbooks-boot-*-ga.jar

COPY ${JAR_FILE} jinbooks-boot.jar

EXPOSE 9526

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms1024m -Xmx4096m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java -jar jinbooks-boot.jar $JAVA_OPTS