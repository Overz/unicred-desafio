FROM openjdk:17-slim

ENV WORKDIR="/app"
ENV TZ="America/Sao_Paulo"
ENV JAVA_OPTS=""
ENV APP_NAME="associado"
ENV APP_VERSION="0.0.1"
ENV APP_DESCRIPTION="Criação de associados"
ENV APP_LOGS="${WORKDIR}/logs"
ENV JAR="${APP_NAME}-${APP_VERSION}.jar"

USER root

RUN unlink /etc/localtime && ln -s /usr/share/zoneinfo/${TZ} /etc/localtime

WORKDIR ${WORKDIR}

ADD target/*.jar ${WORKDIR}/

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar ${WORKDIR}/${JAR}" ]
