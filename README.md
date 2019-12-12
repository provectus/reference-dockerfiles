## Overview
This repository contains reference Dockerfiles recommended to use inside the projects. We tried to capture best practices writing these files for production usage.
There are general guidelines as well as language-specific reference files. The files are heavily documented to explain usage of particular command or option.

## General guidelines
- Minimize number of layers in resulting image while still leveraging layer caching (https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#minimize-the-number-of-layers).
- Avoid installing debugging software inside a resulting image (e.g., curl, ssh)
- Combine shell commands with `&&`
- Split long commands into multiple lines with `\`. Sort build dependencies alphabetically.
- Compose Dockerfile such that less frequently changing commands come before more frequently changing ones.
- Limit your container to a single responsibility. Run an app inside a container as PID 1 (https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#decouple-applications)
- Use minimal base images (e.g., Alpine). Consider [differences](https://wiki.musl-libc.org/functional-differences-from-glibc.html) between `libc` vs `musl`.
- Consider using multi-stage builds to minimize resulting image size (https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#use-multi-stage-builds)
- Use `.dockerignore` files to avoid copying unnecessary files into a resulting image (https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#exclude-with-dockerignore)
- Combine APT cache update and install commands into a single command to exploit "cache busting" technique (https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#apt-get):
```
RUN apt-get update && \
  apt-get -y install --no-install-recommends curl
```
- Use `EXPOSE` command to indicate what ports an application is listening on (https://docs.docker.com/develop/develop-images/dockerfile_best-practices/#expose).
- Run your application as non-root user (https://medium.com/better-programming/running-a-container-with-a-non-root-user-e35830d1f42a)
- Use labels to add metadata to resulting image

## Python reference Dockerfile
See [python/Dockerfile](python/Dockerfile)

## Java reference Dockerfile
See [java/Dockerfile](java/Dockerfile)
