FROM gradle:jdk8 as cache
RUN mkdir -p /home/gradle/cache
ENV GRADLE_USER_HOME /home/gradle/cache
COPY build.gradle /home/gradle/src/
WORKDIR /home/gradle/src
RUN gradle clean dependencies -i --stacktrace

FROM gradle:jdk8 AS builder
COPY --from=cache /home/gradle/cache /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN gradle clean build
RUN mkdir  -p target
WORKDIR /home/gradle/src/target
RUN jar -xf ../build/libs/*.jar



FROM openjdk:8-jre-slim
COPY --from=builder /home/gradle/src/build/distributions/ckmirror.tar /app/
WORKDIR /app
RUN ls -al
RUN tar -xvf ckmirror.tar
WORKDIR /app/ckmirror

RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list
RUN apt-get update && apt-get install -y cron && apt-get install -y vim

RUN touch /var/spool/cron/crontabs/root
RUN chmod 600 /var/spool/cron/crontabs/root
RUN echo "EDITOR=vi; EXPORT EDITOR" >> /root/.profile
RUN echo "*/1 * * * * date >> /var/log/cron.log\n" >> /var/spool/cron/crontabs/root
RUN echo "1,31 * * * * export JAVA_HOME=/usr/local/openjdk-8; /app/ckmirror/bin/ckmirror >> /var/log/cron.log\n" >> /var/spool/cron/crontabs/root

RUN touch /var/log/cron.log


#启动cron服务，会自动调度“/var/spool/cron/crontabs/root”，显示“/var/log/cron.log”
CMD service cron start && tail -f /var/log/cron.log