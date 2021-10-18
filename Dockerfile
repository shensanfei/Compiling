FROM openjdk:12
WORKDIR /app/
COPY lab01.java ./
COPY token.java ./
COPY Grammar.java ./
RUN javac lab01.java
