FROM openjdk:17
VOLUME [ "/tmp" ]
#puerto
EXPOSE 8091
ADD ./target/springboot-servicio-gateway-server-0.0.1-SNAPSHOT.jar gateway-server.jar
ENTRYPOINT [ "java", "-jar", "/gateway-server.jar" ]