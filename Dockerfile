FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
ADD ./target/device-management-*.jar ./app/device-management-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-XX:+UseG1GC","-XX:MaxRAMPercentage=80","-jar","./app/device-management-service.jar"]