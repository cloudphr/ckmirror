FROM gradle:jdk11 as cache
RUN mkdir -p /home/gradle/cache
ENV GRADLE_USER_HOME /home/gradle/cache
COPY build.gradle /home/gradle/ckmirror/
WORKDIR /home/gradle/ckmirror
RUN gradle clean dependencies -i --stacktrace

FROM gradle:jdk11 AS builder
COPY --from=cache /home/gradle/cache /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/ckmirror
WORKDIR /home/gradle/ckmirror
RUN gradle clean build
RUN mkdir -p target
WORKDIR /home/gradle/ckmirror/target
RUN tar -xvf ./build/distributions/ckmirror.tar

FROM openjdk:11-jre-slim AS ckmirror
COPY --from=builder /home/gradle/ckmirror/target/ckmirror /app/
WORKDIR /app/ckmirror
RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list
RUN apt-get update && apt-get install -y cron
RUN touch /var/spool/cron/crontabs/ckmirror
RUN chmod 600 /var/spool/cron/crontabs/ckmirror
RUN echo "*/1 * * * * JAVA_HOME=/usr/local/openjdk-11 /app/ckmirror/bin/ckmirror --update >> /var/log/cron.log\n" >> /var/spool/cron/crontabs/ckmirror
RUN touch /var/log/cron.log
CMD service cron start
RUN JAVA_HOME=/usr/local/openjdk-11 /app/ckmirror/bin/ckmirror