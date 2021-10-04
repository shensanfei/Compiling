FROM gcc:10
WORKDIR /app/
COPY out.c ./
RUN gcc out.c -o out
RUN chmod +x out
