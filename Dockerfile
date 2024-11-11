FROM eclipse-temurin:21-jdk-noble as jre-build
WORKDIR /app
COPY . /app
RUN ./gradlew clean build

FROM eclipse-temurin:21-jre-noble
WORKDIR /app
COPY --from=jre-build /app/build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]