FROM gradle:jdk8 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:8-jre-slim
COPY --from=builder /home/gradle/src/ckmirror/build/distributions/ckmirror.tar /app/
WORKDIR /app
RUN tar -xvf ckmirror.tar
WORKDIR /app/ckmirror
CMD bin/ckmirror