FROM maven:3.8-eclipse-temurin-17-alpine as build

WORKDIR /source

# Copy POMs to fetch and cache dependencies

COPY pom.xml pom.xml
COPY library-management/pom.xml ./library-management/pom.xml
COPY model/pom.xml ./model/pom.xml
COPY payment-gate/pom.xml ./payment-gate/pom.xml
COPY reporting/pom.xml ./reporting/pom.xml
COPY self-service-kiosk/pom.xml ./self-service-kiosk/pom.xml
COPY oauth2-client/pom.xml ./oauth2-client/pom.xml

RUN mvn dependency:go-offline

# Now copy and build sources

COPY .. .

RUN mvn clean install


FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ENV SERVER_PORT 8080

EXPOSE 8080

COPY --from=build /source/library-management/target/*.jar /app/library-management.jar
COPY --from=build /source/payment-gate/target/*.jar /app/payment-gate.jar
COPY --from=build /source/reporting/target/*.jar /app/reporting.jar
COPY --from=build /source/self-service-kiosk/target/*.jar /app/self-service-kiosk.jar
COPY --from=build /source/oauth2-client/target/*.jar /app/oauth2-client.jar

ENTRYPOINT ["java", "-jar"]
