FROM openjdk:15
WORKDIR /app/
COPY ./* ./
RUN javac lab01.java
