FROM openjdk:17
COPY target/estapar.jar app.jar
EXPOSE 3003
ENTRYPOINT ["java", "-jar", "app.jar"]
