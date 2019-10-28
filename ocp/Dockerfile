FROM registry.access.redhat.com/jboss-webserver-3/webserver31-tomcat8-openshift:1.4
COPY ocp/deployment/*.war /opt/webserver/webapps/
COPY ocp/configuration/* /opt/webserver/conf/