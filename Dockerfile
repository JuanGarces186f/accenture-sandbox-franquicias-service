# Fase de compilación
FROM gradle:7.6-jdk17-alpine AS build
# Copiamos solo los archivos de dependencias primero para aprovechar el caché de Docker
COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/src/
WORKDIR /home/gradle/src
RUN gradle dependencies --no-daemon || true
# Ahora copiamos el resto del código
COPY --chown=gradle:gradle . /home/gradle/src
# Construimos el .jar saltando los tests para acelerar el despliegue inicial
RUN gradle build -x test --no-daemon

# Fase de ejecución
FROM eclipse-temurin:17-jre-alpine
# Gradle por defecto deja el jar en build/libs/
# Si hay más de un .jar, solo tomamos el primero (mejor práctica: tener solo uno)
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
WORKDIR /app
# Permitir configurar el puerto por variable de entorno
ENV PORT=8080
EXPOSE $PORT
ENTRYPOINT ["java", "-jar", "app.jar"]
