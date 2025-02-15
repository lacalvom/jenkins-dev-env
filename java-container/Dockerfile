FROM openjdk:17-jdk-slim
RUN apt update && apt install -y curl unzip git maven gradle
WORKDIR /workspace
CMD ["/bin/sh", "-c", "while sleep 1000; do :; done"]
