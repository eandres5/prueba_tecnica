FROM eclipse-temurin:21-jre

WORKDIR /app

RUN mkdir -p logs

COPY target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
