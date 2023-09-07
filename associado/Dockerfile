FROM maven:3.9.4 AS builder

ADD . .

RUN mvn clean install package

FROM openjdk:17-slim


