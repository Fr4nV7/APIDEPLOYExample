# ========== ETAPA 1: BUILD ==========
FROM alpine:latest AS build

# Instalar Java 17 en Alpine Linux (imagen ligera)
RUN apk update
RUN apk add openjdk17

# Copiar todo el código fuente al contenedor
COPY . .

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Compilar y generar el JAR usando bootJar
RUN ./gradlew bootJar --no-daemon

# ========== ETAPA 2: RUNTIME ==========
FROM eclipse-temurin:17-jdk-alpine

# Exponer el puerto 8080 (donde corre Spring Boot)
EXPOSE 8080

# Copiar SOLO el JAR generado desde la etapa de build
COPY --from=build ./build/libs/APIDeployExample-1.0-SNAPSHOT.jar ./app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]