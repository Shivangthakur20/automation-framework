FROM maven:3.9.6-eclipse-temurin-11

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .

RUN apt-get update && apt-get install -y curl

CMD ["mvn", "clean", "test"]