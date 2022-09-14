FROM openjdk:11.0.12
EXPOSE 8080
ADD target/alpha-post-comments-0.0.1-SNAPSHOT.jar alpha-post-comments-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/alpha-post-comments-0.0.1-SNAPSHOT.jar"]