# syntax=docker/dockerfile:1.4

# Fase de compilación: usamos el Gradle Wrapper para garantizar la versión declarada por el proyecto
FROM gradle:7.6-jdk17-alpine AS build
# Copiamos el wrapper y los archivos de configuración primero para aprovechar el caché de Docker
COPY --chown=gradle:gradle gradlew gradle/wrapper/ build.gradle settings.gradle /home/gradle/src/
WORKDIR /home/gradle/src
# Aseguramos permisos ejecutables
RUN chmod +x ./gradlew
# Resolvemos dependencias (usa cache de BuildKit para acelerar descargas entre builds)
RUN --mount=type=cache,target=/home/gradle/.gradle ./gradlew dependencies --no-daemon || true
# Copiamos el resto del código
COPY --chown=gradle:gradle . /home/gradle/src
# Construimos el jar (salta tests para despliegue rápido); usa cache de Gradle
RUN --mount=type=cache,target=/home/gradle/.gradle ./gradlew clean build -x test --no-daemon

# Fase de ejecución: runtime minimal con Temurin JRE
FROM eclipse-temurin:17-jre-alpine AS runtime
# Copiamos el artefacto generado por la fase de build
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
WORKDIR /app
ENV PORT=8080
EXPOSE $PORT
ENTRYPOINT ["java", "-jar", "app.jar"]
