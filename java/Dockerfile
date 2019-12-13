# This is a Dockerfile for example Java app. It strives to capture best practices for writing Dockerfiles described in official 
# docs as well as some other non-official sources. It is heavily commented. You can grab a comments-free version of the file from 
# Dockerfile-prod-java. It is created upon `make` execution (See Makefile)
FROM openjdk:8u232-jdk-slim-buster as build

# Labels are key-value pairs that capture metadata of the image. Examples are image version, maintainer, etc
# MAINTAINER instruction is deprecated. It is recommended to switch to LABEL
# See: https://docs.docker.com/engine/reference/builder/#label
LABEL maintainer="Artyom Bakhtin <a@bakhtin.net>"

# 1. Minimize number of layers by concatenating instructions with '&&'
# 2. Use '\' to split long commands into multiple lines
# 3. Always run apt-get update && apt-get install in a single layer to take advantage of 'cache busting' technique 
#   See: https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#apt-get
#   Otherwise, versions of packages will not be updated due to stale cache.
# 4. No need to explicitly clean up APT cache. Debian-/Ubuntu-based images do it automatically (NOT REQUIRED: rm -rf /var/lib/apt/lists/*)
RUN apt-get update && apt-get install -y --no-install-recommends \
    telnet

# Copy source code into build stage
COPY ./app /build

# Switch to working directory with build. Avoid using 'cd' when possible
WORKDIR /build

# Compile and package into JAR. Replace it with whatever steps are required to build your app.
RUN javac echoserver/Server.java && jar cfme app.jar META-INF/MANIFEST.MF echoserver.Server echoserver/Server.class

# Second stage. We grab whatever artifacts produced in the previous stage and just copy it to the second stage which is a minimal environment without development tools (JRE vs JDK).
FROM openjdk:8u232-jre-slim-buster

# Create a separate unprivileged user to run an app inside a container. Using 'root' user inside a container to run an application is
# strongly discouraged. Container's 'root' user 1:1 maps to 'root' user on a host system by default. Should there be any vulnerabilities
# discovered allowing to escape Docker container an app could gain root privileges on a host system.
RUN useradd --create-home --shell /bin/bash app

# Switch to a user created above
USER app

# Copy artifacts from the previous build stage. Set 'app' user as owner
COPY --from=build --chown=app:app /build/app.jar /app/

# A port an application is accepting incoming connections on (if any). Purely for informational purposes but enables to quickly capture this
# information by other maintainers.
EXPOSE 8080

# Switch to working directory with an app
WORKDIR /app

# Run application. See the difference between ENTRYPOINT vs CMD:
# https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#cmd
# Use /dev/urandom as a source of randomness since it is a non-blocking I/O device (rather than /dev/random) on Linux. The option can be omitted for JDK>=9. See: https://bugs.openjdk.java.net/browse/JDK-8143166.
# /dev/urandom is as secure as /dev/random (See: https://www.2uo.de/myths-about-urandom)
CMD ["java", "-Djava.security.egd=file:///dev/urandom", "-jar" , "app.jar"]

# As of Java version 8u212+ you no longer need to explicitly set JVM memory options (namely, -XX:+UseCGroupMemoryLimitForHeapand -XX:+UnlockExperimentalVMOptions) to make JVM aware it is running inside a Docker container.
# There are now 3 available parameters for JVM memory configuration:
# - -XX:InitialRAMPercentage
# - -XX:MaxRAMPercentage
# - -XX:MinRAMPercentage
# See SO answer explaining these options: https://stackoverflow.com/a/54297753
# Also: https://blog.softwaremill.com/docker-support-in-new-java-8-finally-fd595df0ca54