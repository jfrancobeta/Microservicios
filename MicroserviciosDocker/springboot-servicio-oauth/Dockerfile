FROM openjdk:17
VOLUME [ "/tmp" ]
#puerto
EXPOSE 9100
ADD ./target/springboot-servicio-oauth-0.0.1-SNAPSHOT.jar oauth-server.jar
ENTRYPOINT [ "java", "-jar", "/oauth-server.jar" ]