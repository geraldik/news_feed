# Stage 1 - project build to jar
FROM gradle:jdk17-jammy as gradle
WORKDIR /app
COPY . /app
RUN gradle build -x test

# Stage 2 - how to start the project instructions
FROM gradle:jdk17-jammy
WORKDIR /app
COPY --from=gradle /app/build/libs/news-0.0.1-SNAPSHOT.jar app.jar
CMD java -jar app.jar
