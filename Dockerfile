FROM eclipse-temurin:21-jre-noble
WORKDIR /app
COPY build/libs/app.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=prod
CMD ["java", "-jar", "/app/app.jar"]