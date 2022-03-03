#!/bin/bash

PACKAGE=${PWD##*/}
echo Building $PACKAGE

mvn clean package -Dmaven.test.skip=true  && \
docker buildx build --platform linux/amd64,linux/arm64\
 -t rdomloge/catholicon-multiarch:latest . --push
