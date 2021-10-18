FROM openjdk:12
WORKDIR /app/
COPY ./* ./
RUN javac lab01.java
