FROM maven:3.8.1-openjdk-17 AS build
LABEL authors="dario"

# Compile api
COPY . /webpage-api
WORKDIR /webpage-api
RUN mvn install
RUN mvn clean package

FROM arm64v8/openjdk:17-slim AS runtime

# Copy the compiled jar file from the build stage
COPY --from=build /webpage-api/target/webpage-api*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]