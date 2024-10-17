# Intuit Home Assignment
Below is the implementation corresponding to the linked [requirement](REQUIREMENTS.md).

Despite the small number of UI components and the lack of necessity for state management, I utilized ```useContext``` for managing state across components.

As well as the server, since there are a few end-points to handle, I used minimum abstraction to avoid over-engineering.

The server was built with [Java Spring Boot](https://spring.io/projects/spring-boot).

#### A few comments:
To take advantage of the capabilities of databases, I chose to dump the data from the CSV file into an in-memory database (Java h2 DB). it is transparent and still, you can access the DB through the link ```http://localhost:8080/h2-console```

```
spring.datasource.url=jdbc:h2:mem:players_db
spring.datasource.username=admin
spring.datasource.password=admin
```

<img width="754" alt="image" src="https://github.com/user-attachments/assets/a6adec9a-1b0d-4932-9c74-b2a53783df87">


I would do differently if I had more time...

Depending on the system requirements and predictions of what loads the system has to withstand or in what mode it will run (SAAS, etc.), I would do the following differently:

##### In terms of the application (server):
- Add security support, use of JWT or session, ACL, and RBAC.
- In terms of DB, I would normalize the DB with entity representation or use a no-SQL machine such as MongoDB.
- Use of various types of tests in a comprehensive manner such as unit tests, and integration tests to monitor and verify the integrity of the system in any changes.
- Use managed server API-consuming mechanisms such as GraphQL to add flexibility while consuming data from the server.
- 
##### In terms of the infrastructure:
- I suggest using cache mechanism support depending on the scale requirements.
- Consider supporting high scales (horizontal/verticle)
- Consider using a message broker to manage asynchronous communication between the services within the cluster.
- CI/CD support (pipeline, terraform, etc.).
- Use environment variables injection instead of using a hardcoded setting, for example, use environment segment in docker-compose.yml file for inject PORTS (client and server) CORS URLS (server), etc.

##### In terms of the clean code and code standards:
- Writing reusable code utilizing generics, OOP (inheritance, abstraction), etc.
- Enhance the code cohesion utilizing design patterns and S.O.L.I.D principles, etc.

<hr>

#### API End Points:
* ```GET /api/players``` - returns the list of all players.
* ```GET /api/players/{playerID}``` - returns a single player by ID

<hr>

### Running The Services:
Assume you already downloaded or cloned the repo,

#### Manually:
Assuming MAVEN  is installed on your machine, and the ```mvn``` commands are recognized in the terminal.

from ```/api-service``` folder, perform the following commands:
```bash
mvn clean package
java -jar target/players-0.0.1.jar
```

##### A snapshot of running the API service through a console
<img width="1509" alt="image" src="https://github.com/user-attachments/assets/af7143a7-ad6a-4026-986c-4b8791a0836e">

#### Using Docker (File):
From `api-service` folder run the followiing commands
```bash
docker build . -t intuit-image
```
```bash
docker run intuit-image  -p 8080:8080
```

#### Using Docker Compose:
Assume you have a docker system installed on your machine, \
from the folder root run the following commands:
```bash
  docker-compose build
```
```bash
  docker-compose up
```
##### A snapshot of running the services through Docker Desktop.
<img width="1273" alt="image" src="https://github.com/user-attachments/assets/5ac1e3eb-f09f-4845-b7fe-f7539187c185">


#### Docker multi-stage builds
I choose to use Docker multi-stage builds to shrink the container size, especially for the client service, for example:

```Dockerfile
# Use an OpenJDK 17 base image for Maven to build the application
FROM maven:3.8.5-openjdk-17 AS BUILDER

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .

COPY src ./src

# Build the application using Maven
RUN mvn clean package

# Use an OpenJDK 17 base image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=BUILDER /app/target/players-0.0.1.jar /app/players-0.0.1.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/players-0.0.1.jar"]
```
