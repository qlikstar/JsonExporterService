FROM java:8
VOLUME /tmp
EXPOSE 8080
ADD /build/libs/JsonExporterService-0.0.1-SNAPSHOT.jar spring-boot-docker-1.0.jar
ENTRYPOINT ["java","-jar","spring-boot-docker-1.0.jar"]