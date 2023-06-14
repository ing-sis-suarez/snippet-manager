FROM gradle:7.6.1-jdk17
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN chmod +x gradlew
RUN ./gradlew build
EXPOSE 8081
ENTRYPOINT ["java","-jar","/home/gradle/src/build/libs/snippet-manager-0.0.1-SNAPSHOT.jar"]