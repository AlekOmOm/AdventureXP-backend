FROM openjdk:latest

RUN mkdir /app

COPY . /app

WORKDIR /app

RUN ./mvnw package

COPY target/AdventureXP-Backend-0.0.1-SNAPSHOT.jar /app/target/

CMD ["java", "-cp", "target/AdventureXP-Backend-0.0.1-SNAPSHOT.jar", "org.example.adventurexpbackend.AdventureXpBackendApplication"]