FROM openjdk:17

VOLUME /tmp

EXPOSE 8888

COPY target/*.jar cloud-storage-back-app.jar

ADD src/main/resources/application.properties src/main/resources/application.properties

ENTRYPOINT ["java", "-jar", "/cloud-storage-back-app.jar"]