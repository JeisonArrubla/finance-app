FROM maven:3.8.8 AS builder

COPY . /app

RUN cd /app && mvn clean package -DskipTests

FROM amazoncorretto:17

COPY --from=builder /app/target/finance-app-0.0.1-SNAPSHOT.jar /opt/app.jar

CMD ["java", "-jar", "/opt/app.jar"]