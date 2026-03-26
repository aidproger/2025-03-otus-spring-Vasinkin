FROM ghcr.io/carlossg/maven:azulzulu-17

ARG APP_NAME

COPY src /usr/java/${APP_NAME}/src
COPY pom.xml /usr/java/${APP_NAME}

WORKDIR /usr/java/${APP_NAME}

RUN mvn clean package

ENV APP_FILE_NAME=${APP_NAME}.jar

RUN mkdir -p /app
RUN cp /usr/java/${APP_NAME}/target/*.jar /app/${APP_FILE_NAME}
RUN rm -rf /usr/java/${APP_NAME}

WORKDIR /app

ENTRYPOINT sh -c "java -jar $APP_FILE_NAME"