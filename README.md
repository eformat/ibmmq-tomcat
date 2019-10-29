## ibmmq-tomcat

Run IBM MQ
```
docker run --privileged -p1414:1414 -p9443:9443 --env LICENSE=accept --env MQ_QMGR_NAME=QMGR1 --volume /var/example:/mnt/mqm ibmcom/mq:9
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
