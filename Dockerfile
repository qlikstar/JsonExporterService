FROM openjdk:8
VOLUME /tmp
EXPOSE 9000
ADD /build/libs/json-exporter-service.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]