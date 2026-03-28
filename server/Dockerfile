FROM maven:3-openjdk-18-slim AS build
WORKDIR /usr/local/salt-stars
COPY src src
COPY pom.xml .
RUN mvn -f pom.xml clean package assembly:single

FROM openjdk:18-jdk-slim-buster
RUN adduser -D appuser
USER appuser
COPY --from=build /usr/local/salt-stars/target/jfs-lab-salt-stars-7.0-SNAPSHOT-jar-with-dependencies.jar
  > /usr/local/salt-stars/salt-stars.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/salt-stars/salt-stars.jar"]