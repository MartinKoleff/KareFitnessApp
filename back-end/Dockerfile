FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app
RUN mvn clean package

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/back-end-1.0-SNAPSHOT.jar /app

RUN useradd -m myuser
USER myuser

EXPOSE 8080
CMD ["java", "-jar", "back-end-1.0-SNAPSHOT.jar"]

#CMD ["java","-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n","-jar","back-end-1.0-SNAPSHOT.jar"]
