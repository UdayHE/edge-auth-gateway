FROM openjdk:21-jdk

RUN  mkdir -p /etc/edge-auth-gateway

WORKDIR /etc/edge-auth-gateway

ADD build/libs/*.jar ./

CMD java $JAVA_OPTS -Dserver.port=8080 -Dspring.environments=pp -jar *.jar

EXPOSE 8080
