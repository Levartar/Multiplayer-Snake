# there currently is no jre 17
FROM maven:3.8.4-openjdk-17
COPY . /snake
WORKDIR /snake
RUN [ "mvn", "package", "-Dmaven.test.skip" ]
CMD [ "java", "-jar", "./target/snake-io-1.0-SNAPSHOT-jar-with-dependencies.jar" ]
