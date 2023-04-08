FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV port ${port}

EXPOSE ${port}

ENTRYPOINT ["java", \
                "-Dspring.output.ansi.enabled=ALWAYS ", \
                "-Dspring.profiles.active=prod", \
                "-Dspring.main.allow-circular-references=true", \
                "-jar", "/app.jar", \
                "--server.port=${port}"]