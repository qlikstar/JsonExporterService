FROM openjdk:8
VOLUME /tmp
EXPOSE 9000
ADD /build/libs/JsonExporterService.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]