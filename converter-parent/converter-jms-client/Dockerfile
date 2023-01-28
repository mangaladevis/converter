FROM openjdk:8-jre-alpine
COPY target/converter-jms-client-0.0.1-SNAPSHOT.jar /usr/app/
ENV INPUT_DIR="data/input"
ENV OUTPUT_DIR="data/output"
WORKDIR /usr/app
RUN mkdir -p data1
RUN mkdir -p data1/input
RUN mkdir -p data1/output
ENTRYPOINT java -jar -Dinput-dir=${INPUT_DIR} -Doutput-dir=${OUTPUT_DIR} -Dbroker_url=${BROKER_URL} -Dbroker_user_name=${BROKER_USERNAME} -Dbroker_user_password=${BROKER_PASSWORD} converter-jms-client-0.0.1-SNAPSHOT.jar
