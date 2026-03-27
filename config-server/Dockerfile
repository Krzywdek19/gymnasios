FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

COPY api-gateway/pom.xml api-gateway/pom.xml
COPY config-server/pom.xml config-server/pom.xml
COPY discovery-server/pom.xml discovery-server/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY workout-service/pom.xml workout-service/pom.xml

COPY config-server/src config-server/src

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw -pl config-server -am clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/config-server/target/config-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]