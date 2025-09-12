FROM eclipse-temurin:21-jdk-alpine
# Crear el usuario appuser (y su directorio home)
RUN adduser -D appuser
VOLUME /tmp
COPY *.jar AUTENTICACION.jar
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
# Replace with a non-root user to avoid running the container with excessive privileges
USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar AUTENTICACION.jar" ]
