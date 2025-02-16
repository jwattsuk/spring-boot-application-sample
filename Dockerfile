FROM openjdk:17-jdk-slim  # Ensure we use a Debian-based image

WORKDIR /app

ARG GITHUB_USER=jwattsuk
ARG GITHUB_REPO=spring-boot-application-parent
ARG GITHUB_TOKEN
ARG PACKAGE_PATH=com/jwattsuk/sampleservice/sample-service-assembly

RUN apt-get update && apt-get install -y curl jq

# Fetch the latest version from GitHub Packages
RUN LATEST_VERSION=$(curl -s -u "${GITHUB_USER}:${GITHUB_TOKEN}" \
      "https://maven.pkg.github.com/${GITHUB_USER}/${GITHUB_REPO}/maven-metadata.xml" | \
      grep -oPm1 "(?<=<latest>)[^<]+") && \
    echo "Latest Version: $LATEST_VERSION" && \
    curl -L -u "${GITHUB_USER}:${GITHUB_TOKEN}" \
      -o app.tar.gz \
      "https://maven.pkg.github.com/${GITHUB_USER}/${GITHUB_REPO}/${PACKAGE_PATH}/${LATEST_VERSION}/sample-service-assembly-${LATEST_VERSION}-bin.tar.gz"

RUN tar -xzf app.tar.gz && rm app.tar.gz

EXPOSE 8081
EXPOSE 5005

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-cp", "/app/lib/*", "com.jwattsuk.sample.SampleServiceApplication"]
