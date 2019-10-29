#!/bin/bash
mvn clean package
docker build -f Dockerfile -t jws-wsch-app .
docker run --privileged --net host --rm jws-wsch-app:latest
