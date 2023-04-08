FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV port ${port}

EXPOSE ${port}

ENTRYPOINT ["java", \
                "-Xms128M", "-Xmx128M", \
                "-Dspring.main.allow-circular-references=true", \
                "-Dspring.output.ansi.enabled=ALWAYS", \
                "-Dspring.profiles.active=prod", \
                "-jar", "/app.jar", \
                "--server.port=${port}"]