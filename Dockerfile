# there currently is no jre 17
FROM maven:3.8.4-openjdk-17
COPY . /snake-io
WORKDIR /snake-io/server
RUN [ "mvn", "package", "-Dmaven.test.skip" ]
WORKDIR /snake-io/server/target
CMD [ "java", "-jar", "./snake-io-1.0-SNAPSHOT-jar-with-dependencies.jar" ]
