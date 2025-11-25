FROM amazoncorretto:17
COPY ./target/devops.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "devops.jar", "localhost:3306", "30000"]