#!/bin/bash
mvn clean package
cp target/*.war ocp/deployment/
docker build -f ocp/Dockerfile -t jws-wsch-app .
docker run --privileged --net host --rm jws-wsch-app:latest
