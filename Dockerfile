FROM openjdk:11
LABEL verion="1.0.0"
COPY ./target/ovigia-0.0.1-SNAPSHOT.jar ovigia.jar
CMD ["java","-jar","ovigia.jar"]
