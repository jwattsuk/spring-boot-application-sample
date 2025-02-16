# Use a base image with Java runtime environment
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Define variables for GitHub Packages repository and artifact
ARG GITHUB_USER=jwattsuk
ARG GITHUB_REPO=spring-boot-application-parent
ARG GITHUB_TOKEN
ARG PACKAGE_PATH=com/jwattsuk/sampleservice/sample-service-assembly

# Install necessary tools based on OS
RUN set -eux; \
    if command -v apt-get > /dev/null; then \
        apt-get update && apt-get install -y curl jq; \
    elif command -v apk > /dev/null; then \
        apk add --no-cache curl jq; \
    else \
        echo "Unsupported package manager" && exit 1; \
    fi

# Fetch the latest version from GitHub Packages and download the binary
RUN LATEST_VERSION=$(curl -s -u "${GITHUB_USER}:${GITHUB_TOKEN}" \
      "https://maven.pkg.github.com/${GITHUB_USER}/${GITHUB_REPO}/maven-metadata.xml" | \
      grep -oPm1 "(?<=<latest>)[^<]+") && \
    echo "Latest Version: $LATEST_VERSION" && \
    curl -L -u "${GITHUB_USER}:${GITHUB_TOKEN}" \
      -o app.tar.gz \
      "https://maven.pkg.github.com/${GITHUB_USER}/${GITHUB_REPO}/${PACKAGE_PATH}/${LATEST_VERSION}/sample-service-assembly-${LATEST_VERSION}-bin.tar.gz"

# Extract the app.tar.gz file, which contains lib/app.jar and dependencies
RUN tar -xzf app.tar.gz && rm app.tar.gz

# Expose the port the application will listen on
EXPOSE 8081
EXPOSE 5005

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-cp", "/app/lib/*", "com.jwattsuk.sample.SampleServiceApplication"]
