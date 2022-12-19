FROM joengenduvel/jre17
COPY target/converter-api-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java","-jar","converter-api-0.0.1-SNAPSHOT.jar"]
