FROM amazoncorretto:17
COPY ./target/devops-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "devops-jar-with-dependencies.jar", "db:33060", "30000"]