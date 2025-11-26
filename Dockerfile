FROM eclipse-temurin:17-jdk-alpine

COPY target/UrlShortener.jar url-shortener.jar

ENTRYPOINT ["java","-jar","/url-shortener.jar"]
