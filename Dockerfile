FROM arm32v7/openjdk:8-jdk-alpine

# Enable us to build ARM images in an i386 environment
COPY qemu-arm-static /usr/bin/qemu-arm-static

COPY target/catholicon.war ./catholicon.war

EXPOSE 81

ENTRYPOINT ["/usr/bin/java", "-jar", "catholicon.war", "--BASE_URL=http://bdbl.org.uk", "--SPIDER_FORCE_REFRESH=true"]
