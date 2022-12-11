FROM openjdk:8-jre-alpine
COPY target/converter-file-0.0.1-SNAPSHOT.jar /usr/app/
ENV INPUTDIR="data/input"
ENV OUTPUTDIR="data/output"
WORKDIR /usr/app
RUN mkdir -p data
RUN mkdir -p data/input
RUN mkdir -p data/output
ENTRYPOINT java -jar -Dinput-dir=${INPUTDIR} -Doutput-dir=${OUTPUTDIR} converter-file-0.0.1-SNAPSHOT.jar
