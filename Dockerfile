FROM openjdk:11.0-jdk-slim

COPY target/catholicon.war ./catholicon.war

EXPOSE 81

ENTRYPOINT ["java", "-jar", "catholicon.war", "--BASE_URL=http://bdbl.org.uk", "--SPIDER_FORCE_REFRESH=true"]
