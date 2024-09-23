FROM openjdk:17
VOLUME [ "/tmp" ]
#puerto
EXPOSE 9411
ADD ./zipkin-server-3.4.1-exec.jar zipkin-server.jar
ENTRYPOINT [ "java", "-jar", "/zipkin-server.jar" ]