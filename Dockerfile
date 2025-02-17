# Use a base image with Java runtime environment
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Set build output directory
ARG OUTDIR=sample-service-parent/sample-service-assembly/target

# Copy the app.tar.gz file into the container
COPY ${OUTDIR}/sample-service-assembly-*-bin.tar.gz /app/app.tar.gz

# Extract the app.tar.gz file, which contains lib/app.jar and all dependencies
RUN tar -xzf app.tar.gz && rm app.tar.gz

# Expose the port the application will listen on (adjust if necessary)
EXPOSE 8081
EXPOSE 5005
# Set the entrypoint to run the main application with all dependencies in the classpath
# Also adding remote debugging options to the Java command
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-cp", "/app/lib/*", "com.jwattsuk.sample.SampleServiceApplication"]

