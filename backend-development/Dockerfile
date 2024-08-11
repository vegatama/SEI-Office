FROM amazoncorretto:21-alpine

ENV TZ="Asia/Jakarta"

ARG JAR_FILE=backendapi-0.0.1.jar
WORKDIR /opt/app
COPY ./target/${JAR_FILE} /opt/app/

# Install Microsoft TrueType Core Fonts
RUN apk add --nocache msttcorefonts-installer fontconfig
RUN update-ms-fonts

ENTRYPOINT ["java","-jar","backendapi-0.0.1.jar"]