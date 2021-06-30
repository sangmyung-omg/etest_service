FROM openjdk:8u111-jdk-alpine

#Dir set
WORKDIR /home/tmax

#환경변수
ENV SCRIPT_HOME /home/tmax/script

#테스트용 환경변수
ENV HTTP_PORT 8888

RUN mkdir -p ${SCRIPT_HOME}

#Setting
COPY config/start.sh ${SCRIPT_HOME}/start.sh
COPY config/env.sh ${SCRIPT_HOME}/env.sh
COPY config/application.properties /home/tmax/application.properties
COPY config/triton.properties /home/tmax/triton.properties
COPY config/lrs.properties /home/tmax/lrs.properties

RUN chmod -R 755 /home/tmax/script

ADD /eTest/build/libs/*.jar /home/tmax/app.jar

#ENTRYPOINT ["/bin/sh", "--"]
ENTRYPOINT ["./script/start.sh"]