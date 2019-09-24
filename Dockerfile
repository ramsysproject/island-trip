FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM openjdk:8-jdk-alpine
ADD target/island*.jar islandtrip.jar
EXPOSE 8080 9000
CMD java -jar islandtrip.jar