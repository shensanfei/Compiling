FROM openjdk:12
WORKDIR /app/
COPY *.java ./
RUN javac lab01.java
