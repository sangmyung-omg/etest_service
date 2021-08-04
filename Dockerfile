FROM openjdk:8u111-jdk-alpine

#Dir set
WORKDIR /home/tmax

#환경변수
ENV SCRIPT_HOME /home/tmax/script

#테스트용 환경변수
#ENV HTTP_PORT 8888

RUN mkdir -p ${SCRIPT_HOME}

#Setting
COPY config/start.sh ${SCRIPT_HOME}/start.sh
COPY config/env.sh ${SCRIPT_HOME}/env.sh
COPY config/application.properties.deploy /home/tmax/application.properties

RUN chmod -R 755 /home/tmax/script

ADD /eTest/build/libs/eTest-0.0.1-SNAPSHOT.jar /home/tmax/app.jar

ENTRYPOINT ["./script/start.sh"]