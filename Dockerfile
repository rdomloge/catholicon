FROM oracle-serverjre-8
RUN apt-get update

## UTF-8
#RUN locale-gen en_US.UTF-8
ENV LANG       en_US.UTF-8
ENV LC_ALL     en_US.UTF-8

## JAVA INSTALLATION
#RUN echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections
#RUN echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" > /etc/apt/sources.list.d/webupd8team-java-trusty.list
#RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
#RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --force-yes --no-install-recommends oracle-java8-installer && apt-get clean all

## JAVA_HOME
#ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

RUN apt-get install -y git
RUN apt-cache search maven
RUN apt-get install -y maven
RUN git clone git://github.com/rdomloge/catholicon.git
RUN mvn -f catholicon/pom.xml package
RUN mkdir -p catholicon/bin
RUN touch catholicon/bin/catholicon.sh
RUN echo java -jar catholicon/target/catholicon.war\ 
--BASE_URL=http://bdbl.org.uk \
--SPIDER_FORCE_REFRESH=true \& \
> catholicon/bin/catholicon.sh
RUN chmod a+x catholicon/bin/catholicon.sh
EXPOSE 81