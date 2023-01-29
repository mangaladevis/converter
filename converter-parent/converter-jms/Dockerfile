FROM openjdk:8-jre-alpine
COPY target/converter-jms-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
ENTRYPOINT java -jar -Dbroker_url=${BROKER_URL} -Dbroker_user_name=${BROKER_USERNAME} -Dbroker_user_password=${BROKER_PASSWORD} converter-jms-0.0.1-SNAPSHOT.jar