# Fase di build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /siw-peakpulse-1

# Copia tutto il contenuto del progetto nella directory di lavoro
COPY . .

# Esegui il comando di build Maven
RUN mvn clean package -DskipTests

# Fase di runtime
FROM openjdk:17.0.1-jdk-slim
WORKDIR /siw-peakpulse-1

# Copia il file JAR dalla fase di build
COPY --from=build /siw-peakpulse-1/target/siw-peakpulse-1-1.0.jar siw-peakpulse-1.jar

# Espone la porta su cui l'applicazione ascolterà
EXPOSE 8082

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "siw-peakpulse-1.jar"]
