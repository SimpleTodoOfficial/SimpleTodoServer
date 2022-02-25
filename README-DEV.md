# SimpleTodoServer

Based on Java, Spring boot, Spring security (token-based authentication), docker, MariaDB and more.

## Requirements

### Docker

#### Installation

- Windows instructions
	- https://hub.docker.com/editions/community/docker-ce-desktop-windows/
- Linux instructions
	- https://docs.docker.com/engine/install/ubuntu/
	- https://docs.docker.com/compose/install/
	- sudo chmod 666 /var/run/docker.sock
- sudo chown -R 1001:1001 ./docker/db/data

## Build for production

### Build the SimpleTodo database image

- "docker image build -t simpletodo-db -f docker/db/Dockerfile ."
- "docker login"
- "docker tag simpletodo-db <dockerName>/simpletodo-db:<version>"
- "docker push <dockerName>/simpletodo-db:<version>"

### Build the SimpleTodo database migration image

- "docker image build -t simpletodo-flyway -f docker/flyway/Dockerfile ."
- "docker login"
- "docker tag simpletodo-flyway <dockerName>/simpletodo-flyway:<version>"
- "docker push <dockerName>/simpletodo-flyway:<version>"

### Build the SimpleTodo server

- "docker image build -t simpletodo-server -f docker/simpletodoserver/Dockerfile ."
- "docker login"
- "docker tag simpletodo-server <dockerName>/simpletodo-server:<version>"
- "docker push <dockerName>/simpletodo-server:<version>"

## Run the software

Windows: On windows the user has to add the project folder in file sharing in order to activate bind mounting into Docker containers:

- Docker - Settings - Resources - + - Select "/path/to/SimpleTodoServer"

### Local development

- Start via "docker-compose -f docker/stack-dev.yml up"
- Database UI available at http://localhost:8080
- You might want to insert some example data: "resources/database.sql" + "resources/example-data.sql"
- Set the spring profile to "dev", e.g. in Eclipse: "Run"-"Run configurations"-<ConfigName>-"Arguments"-"Program Arguments": "-Dspring.profiles.active=dev"
- Start STDApplication.java as a Java application

### Production

- Change usernames, passwords, API_URL, etc. in "docker/stack.yml"
- Change the nginx configuration at "docker/nginx/nginx.prod.conf" and copy it over to "docker/nginx/nginx.conf" on your server
- Change the flyway db migration configuration at "docker/flyway/config/flyway.conf" and copy it over to "docker/flyway/config/flyway.conf" on your server
- Generate an ssl certificate for your domain, e.g. via https://letsencrypt.org
- Make sure to have the correct permissions for the mounted database folder: "sudo chown -R 1001:1001 data/db/"
- Start via "docker-compose -f docker/stack.yml up"

### Infos

- The database is externalized and writes data to "docker/data/db" so it's not erased on container deletion.

## Stop the software

- Stop the spring application if started
- Stop docker containers (e.g. "docker-compose -f ./docker/stack-dev.yml stop" or "docker-compose -f ./docker/stack.yml stop")

## Some useful docker commands

### List images

docker images

### Delete an image

docker rmi imgId1[, imgId2, ...]

### List all containers

docker container ls -a

### Delete a container

docker container rm cId

## Authentication

When first started, an admin user is set up (local development and production) with username/password combination: Admin/password1

### 1. Sign up a user

POST http://localhost:9090/api/auth/signup
{
    "username": "NewUser",
    "email": "newuser@mail.com",
    "password": "password1"
}

### 2. Sign in the user

POST http://localhost:9090/api/auth/signin
{
    "username": "NewUser",
    "password": "password1"
}

-> Copy the authentication token <token>

### 3. Authenticate the following requests with the token

GET http://localhost:9090/api/<path>
[HEADER] Authorization: Bearer <token>

## More information

Properties and configuration in example files:

* docker/stack.yml
