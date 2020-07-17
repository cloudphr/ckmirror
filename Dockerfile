FROM gradle:jdk8 as cache
RUN mkdir -p /home/gradle/cache
ENV GRADLE_USER_HOME /home/gradle/cache
COPY build.gradle /home/gradle/src/
WORKDIR /home/gradle/src
RUN gradle clean dependencies -i --stacktrace

FROM gradle:jdk8 AS builder
COPY --from=cache /home/gradle/cache /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/src

# Add crontab file in the cron directory
COPY --chown=gradle:gradle Crontabfile /etc/cron.d/hello-cron

WORKDIR /home/gradle/src
RUN gradle clean build
RUN mkdir  -p target
WORKDIR /home/gradle/src/target
RUN jar -xf ../build/libs/*.jar



FROM openjdk:8-jre-slim
COPY --from=builder /home/gradle/src/build/distributions/ckmirror.tar /app/
WORKDIR /app
RUN tar -xvf ckmirror.tar
WORKDIR /app/ckmirror

RUN apt-get update && apt-get install -y cron

COPY --from=builder /etc/cron.d/hello-cron /etc/cron.d/hello-cron
RUN cat /etc/cron.d/hello-cron
# Give execution rights on the cron job
RUN chmod 0644 /etc/cron.d/hello-cron
# Create the log file to be able to run tail
RUN touch /var/log/cron.log

RUN crontab /etc/cron.d/hello-cron

CMD cron && tail -f /var/log/cron.log