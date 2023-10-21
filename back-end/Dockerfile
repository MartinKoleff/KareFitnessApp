FROM openjdk:17

WORKDIR /app

COPY target/back-end-1.0-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "back-end-1.0-SNAPSHOT.jar"]

#CMD ["java","-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n","-jar","back-end-1.0-SNAPSHOT.jar"]
