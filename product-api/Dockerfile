FROM maven:3.8.3-jdk-11
COPY . .
WORKDIR .
RUN mvn clean install -DskipTests
EXPOSE 8081
CMD ["mvn", "spring-boot:run"]