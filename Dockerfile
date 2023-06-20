FROM gradle:7.6.1-jdk17
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN chmod +x gradlew
RUN ./gradlew build
RUN docker compose -f docker-compose.dev.yml up -d
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/gradle/src/build/libs/snippet-manager-0.0.1-SNAPSHOT.jar"]