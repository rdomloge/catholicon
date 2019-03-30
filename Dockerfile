FROM openjdk:8-jdk-alpine
RUN apk update

## UTF-8
#RUN locale-gen en_US.UTF-8
ENV LANG       en_US.UTF-8
ENV LC_ALL     en_US.UTF-8

ARG ws
COPY $ws/target/catholicon.war ./catholicon.war
RUN echo java -jar catholicon/target/catholicon.war\ 
--BASE_URL=http://bdbl.org.uk \
--SPIDER_FORCE_REFRESH=true \& \
> catholicon.sh
RUN chmod a+x catholicon.sh

EXPOSE 81

ENTRYPOINT ["sh", "catholicon.sh"]
