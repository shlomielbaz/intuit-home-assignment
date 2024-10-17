# Intuit Home Assignment
Below is the implementation corresponding to the linked [requirement](REQUIREMENTS.md).

Despite the small number of UI components and the lack of necessity for state management, I utilized ```useContext``` for managing state across components.

As well as the server, since there are a few end-points to handle, I used minimum abstraction to avoid over-engineering.

The server was built with [Java Spring Boot](https://spring.io/projects/spring-boot).

For the client, I use [React 18.3.1](https://react.dev/) with the awesome [PrimeReact](https://primereact.org/) UI kit and [Vite](https://vitejs.dev/) as a build tool.

#### A few comments:
To take advantage of the capabilities of databases, I chose to dump the data from the JSON file into an in-memory database (Java h2 DB). it is transparent and still, you can access the DB through the link ```http://<THE DOMAIN>/h2-console```

Although the server supports sorting, the sorting is performed on the client side utilizing Javascript.

I delivered the code with a known bug related to the scrolling pagination, I left a TODO comment where it should be fixed, I just don't want to spend much time resolving it.

My answer to the question of what I would do differently if I had more time...

Depending on the system requirements and predictions of what loads the system has to withstand or in what mode it will run (SAAS, etc.), I would do the following differently:

##### In terms of the application (server):
- Add security support, use of JWT or session, ACL, and RBAC.
- In terms of DB, I would normalize the DB with entity representation or use a no-SQL machine such as MongoDB.
- Use of various types of tests in a comprehensive manner such as unit tests, and integration tests to monitor and verify the integrity of the system in any changes.
- Use managed server API-consuming mechanisms such as GraphQL to add flexibility while consuming data from the server.

##### In terms of the application (UI):
- Use more rich UI components.
- Add a responsive layout that spreads out all parts of the screen.
- Separate the UI components into small pieces for reuse purposes.
- Use state management mechanisms such as Redux, MobX, etc.

##### In terms of the application (UX):
- Add search box.
- Add navigations.

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
Assume that MAVEN and NPM are installed on your machine, and the ```mvn``` and ```npm``` commands are recognized in the terminal.

from ```/api-service``` folder, perform the following commands:
```bash
mvn clean package
java -jar target/players-0.0.1.jar
```
from ```/ui-service``` folder, perform the following commands:
```bash
npm install
npm start
```
##### A snapshot of running the services through a console
<img width="1459" alt="image" src="https://github.com/user-attachments/assets/243f0ca9-7dba-4210-b8f9-b143649533d5">


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
FROM node:20-alpine AS BUILDER

WORKDIR /app

COPY package.json .

RUN npm install

COPY . .

RUN npm run build

FROM node:20-alpine AS RUNNER

WORKDIR /app

RUN mkdir -p dist/assets

COPY --from=BUILDER /app/dist/index.html /app/dist

COPY --from=BUILDER /app/dist/* /app/dist/assets

COPY package.json /app/

RUN npm i -g serve

RUN npm install --omit=dev

EXPOSE 3000

CMD [ "serve", "-s", "dist" ]
```

The following snapshot shows how the React app is packed, you can see that the source code (src) wasn't packed:

<img width="1271" alt="image" src="https://github.com/user-attachments/assets/ab87da93-bc30-4522-8322-81076c9bb5bc">

<hr>

#### Some UI screen-shots:
Grid View:
<img width="1512" alt="image" src="https://github.com/user-attachments/assets/527c4d82-bbec-4c3a-a65e-3386e8f895c7">

List View:
<img width="1500" alt="image" src="https://github.com/user-attachments/assets/57fbf7b6-cd25-4a80-b7cc-549ec0d27108">

Player Details View:
<img width="1510" alt="image" src="https://github.com/user-attachments/assets/95b81e65-c128-4f30-ae47-9ed459f6cdb9">

<hr>

### The Project Folder Tree:
tree -P '*.java|*.properties|*.tsx|*.ts|*.html|*.css|*.scss' -I 'node_modules|cache|dist|*.spec.ts'
```bash
├── api-service
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── players
│   │   │   │           ├── Main.java
│   │   │   │           ├── config
│   │   │   │           │   └── AppConfig.java
│   │   │   │           ├── controller
│   │   │   │           │   └── PlayerController.java
│   │   │   │           ├── domain
│   │   │   │           │   ├── base
│   │   │   │           │   │   └── BaseEntity.java
│   │   │   │           │   ├── dto
│   │   │   │           │   │   ├── PageDTO.java
│   │   │   │           │   │   └── PlayerDTO.java
│   │   │   │           │   └── entity
│   │   │   │           │       ├── Player.java
│   │   │   │           │       └── embeddable
│   │   │   │           │           └── player
│   │   │   │           │               ├── Address.java
│   │   │   │           │               ├── Coordinates.java
│   │   │   │           │               ├── CreditCard.java
│   │   │   │           │               ├── EventType.java
│   │   │   │           │               └── Subscription.java
│   │   │   │           ├── repository
│   │   │   │           │   ├── PlayerRepository.java
│   │   │   │           │   └── generic
│   │   │   │           │       └── CrudRepository.java
│   │   │   │           └── service
│   │   │   │               └── PlayerService.java
│   │   │   └── resources
│   │   │       ├── application.properties
│   │   │       ├── json
│   │   │       ├── static
│   │   │       └── templates
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── players
│   │                   └── MainTests.java
│   └── target
│       ├── classes
│       │   ├── application.properties
│       │   ├── com
│       │   │   └── players
│       │   │       ├── config
│       │   │       ├── controller
│       │   │       ├── domain
│       │   │       │   ├── base
│       │   │       │   ├── dto
│       │   │       │   └── entity
│       │   │       │       └── embeddable
│       │   │       │           └── player
│       │   │       ├── repository
│       │   │       │   └── generic
│       │   │       └── service
│       │   └── json
│       ├── generated-sources
│       │   └── annotations
│       ├── generated-test-sources
│       │   └── test-annotations
│       ├── maven-archiver
│       │   └── pom.properties
│       ├── maven-status
│       │   └── maven-compiler-plugin
│       │       ├── compile
│       │       │   └── default-compile
│       │       └── testCompile
│       │           └── default-testCompile
│       ├── surefire-reports
│       └── test-classes
│           └── com
│               └── players
├── docker-compose.yml
└── ui-service
    ├── index.html
    ├── src
    │   ├── App.tsx
    │   ├── assets
    │   ├── components
    │   │   ├── PlayerDetails.tsx
    │   │   ├── PlayerGridItem.tsx
    │   │   ├── PlayerListItem.tsx
    │   │   └── ThemeSwitcher.tsx
    │   ├── context
    │   │   └── PlayerContext.tsx
    │   ├── domain
    │   │   ├── enums
    │   │   │   └── SortType.ts
    │   │   └── interfaces
    │   │       ├── IAddress.ts
    │   │       ├── ICoordinates.ts
    │   │       ├── ICreditCard.ts
    │   │       ├── IEventType.ts
    │   │       ├── IPage.ts
    │   │       ├── IPlayer.ts
    │   │       └── ISubscription.ts
    │   ├── flags.css
    │   ├── index.css
    │   ├── main.tsx
    │   ├── service
    │   │   └── PlayerService.ts
    │   └── vite-env.d.ts
    └── vite.config.ts
```



