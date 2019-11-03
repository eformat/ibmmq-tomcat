## ibmmq-tomcat

Run IBM MQ
```
docker volume create mqm
docker run -p1414:1414 -p9443:9443 --env LICENSE=accept --env MQ_QMGR_NAME=QMGR1 --volume mqm:/mnt/mqm ibmcom/mq:9
```

Login IBM MQ WebConsole
```
https://localhost:9443/ibmmq/console/
admin / passw0rd
```

Clone this repo and build
```
cd ~/git/ibmmq-tomcat
./buildrun.sh
```

Test sending message from webconsole:
```
2019-10-28 22:30:03,710 [main] INFO  org.apache.catalina.startup.Catalina- Server startup in 11079 ms
2019-10-28 22:30:25 INFO  JMSMQListener:59 - onMessage start 
2019-10-28 22:30:25 INFO  JMSMQListener:73 - JMS Message received successfully: foobar
```

## Links

- https://github.com/ibm-messaging/mq-container/blob/master/docs/developer-config.md
- https://access.redhat.com/documentation/en-us/red_hat_jboss_web_server/3.1/html-single/red_hat_jboss_web_server_for_openshift/index


## OpenShift

Create Project
```
oc new-project ibmmq-tomcat
```

IBM MQ
```
git clone git@github.com:IBM/charts.git ibm-charts

oc create -f - <<EOF
apiVersion: v1
kind: Secret
metadata:  
    name: mq-secret
    namespace: ibmmq-tomcat
type: Opaque
data:  
    adminPassword: cGFzc3cwcmQ=
EOF

-- storage does not work in ocp4 for now
-- ~/git/ibm-charts/stable/ibm-mqadvanced-server-dev/values.yaml
# persistence section specifies persistence settings which apply to the whole chart
persistence:
  # enabled is whether to use Persistent Volumes or not
  enabled: false

-- use helm chart to install
helm install --name mqm ibm-mqadvanced-server-dev --set license=accept --set queueManager.dev.secret.name=mq-secret --set queueManager.dev.secret.adminPasswordKey=adminPassword --set queueManager.name=QMGR1 -n ibmmq-tomcat

-- create route to ibm.mq web console
oc expose svc ibmmq-tomcat-ibm-mq --port=9443
oc patch route/ibmmq-tomcat-ibm-mq --type=json -p '[{"op":"add", "path":"/spec/tls", "value":{"termination":"passthrough"}}]'
```

Application

Update `context.xml` so that `HOST` maps to OpenShift Service

```
  HOST="ibmmq-tomcat-ibm-mq"
```

Deploy (using s2i build)
```
oc new-app registry.access.redhat.com/jboss-webserver-3/webserver31-tomcat8-openshift:1.4~https://github.com/eformat/ibmmq-tomcat.git --strategy=source --build-env MAVEN_MIRROR_URL=http://nexus.nexus.svc.cluster.local:8081/repository/maven-public/
```

Deploy (using docker build and binary app)

```
oc new-build --binary --name=tomcat
oc start-build tomcat --from-dir=. --follow
oc new-app tomcat
```
