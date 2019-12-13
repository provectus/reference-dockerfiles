# This is a Dockerfile for example Python web app. It strives to capture best practices for writing Dockerfiles described in official 
# docs as well as some other non-official sources. It is heavily commented. You can grab a comments-free version of the file from 
# Dockerfile-prod-python. It is created upon `make` execution (See Makefile)

# This is a multistage build that uses python:3.7 as a named base image
# See: https://docs.docker.com/develop/develop-images/multistage-build/
FROM python:3.7 AS build

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

# Always pin versions of dependencies in requirements.txt
# See requirements.txt for more info
COPY ./requirements.txt ./requirements.txt

# Install dependencies locally for a user. We can then easily copy a single directory from inside the following stage and use it similar
# to virtualenv. Using a virtualenv is also an option but does not make much sense since there the only 'venv' in the resulting image.
RUN pip3 install --user -r ./requirements.txt

# Second stage. We can grab whatever artifacts produced in the previous stage. Python example is a bit artificial since no binaries are 
# built due to interpreted nature of Python. The example would have been more evident for compiled languages. In that case we could have
# used first stage as environment for compilation. And then just copy resulting binaries to the second stage which is minimal environment
# without development tools (such as gcc or cmake).
FROM python:3.7

# Create a separate unprivileged user to run an app inside a container. Using 'root' user inside a container to run an application is
# strongly discouraged. Container's 'root' user 1:1 maps to 'root' user on a host system by default. Should there be any vulnerabilities
# discovered allowing to escape Docker container an app could gain root privileges on a host system.
RUN useradd --create-home --shell /bin/bash app

# Switch to a user created above
USER app

# Copy artifacts from the previous build stage. Set 'app' user as owner
COPY --from=build --chown=app:app /root/.local /home/app/.local

# Copy application files. Set 'app' user as owner. Note that the entire application is stored in a single directory. That is to avoid
# enumerating to-be-copied files in COPY command one by one or composing verbose .dockerignore file.
COPY --chown=app:app app /app

# A port an application is accepting incoming connections on (if any). Purely for informational purposes but enables to quickly capture this
# information by other maintainers.
EXPOSE 8080

# Add path with python packages to existing PATH environment variable.
ENV PATH=/home/app/.local/bin:$PATH

# Force the stdout and stderr streams to be unbuffered. This is useful for receiving timely log messages and avoiding situations where the application crashes without emitting a relevant message due to the message being "stuck" in a buffer.
# Source: https://github.com/awslabs/amazon-sagemaker-examples/issues/319#issuecomment-405749895
ENV PYTHONUNBUFFERED=1

# Switch to working directory with an app. Avoid using 'cd' when possible
WORKDIR /app

# Run application. See the difference between ENTRYPOINT vs CMD:
# https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#cmd
CMD ["python3", "main.py"]
