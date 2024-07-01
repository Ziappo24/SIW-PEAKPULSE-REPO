FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/siw-peakpulse-1-0.0.1-SNAPSHOT.jar siw-peakpulse-1.jar
EXPOSE 8082
ENTRYPOINT [ "java", "-jar", "siw-peakpulse-1.jar" ]