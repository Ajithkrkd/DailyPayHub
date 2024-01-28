FROM openjdk:17
WORKDIR /opt
ENV PORT 8080
EXPOSE 9000
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar