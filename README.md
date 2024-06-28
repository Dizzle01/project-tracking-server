# project-tracking-server
This is an example API with authenticating with an API Key and using a PostgreSQL-database. You can check out the database-schema, which can be found in `src/main/resources/db/database_schema.png` of the project.

Used tech / frameworks:
- Java 21
- Spring Boot 3.2.2
- Lombok 1.18.30
- Mapstruct 1.5.5.Final
- Liquibase 4.24.0
- OpenAPI 3.0
- JUnit 5 and Mockito
- PostgreSQL
- Docker

## Prerequisites
This API needs to have a Java 21 environment and a running PostgreSQL instance. If you do not have the dependencies locally installed you can use the following commands to pull the necessary images to start the application:

- `docker pull postgres`
- `docker pull dizzle2001/project-tracking-server-app`

Clone the project to have access to the docker-compose.yml file:

- `git clone https://github.com/dizzle01/project-tracking-server.git`

## Running the API

### Example
Follow the steps to make API calls:

1. Go to the root directory of the project and run the following command to start the application:
    - `docker-compose up`

2. Run the following command to make a non-secured request to an endpoint:
    - `curl -v http://localhost:8080/api/v1/companies`

3. Run the following command to make a secured request to an endpoint:
    - `curl -v --header "ApiKey: d58829a4479bad34b0d52fc78787e756f7d8825f32fa09a7171e5f55714ed174" http://localhost:8080/api/v1/companies/1/users`

4. Run the following command to terminate the application:
    - `docker-compose down`

### Swagger
For making calls to the API you can look up the endpoints in the [API description](https://app.swaggerhub.com/apis/HANSHENRITHIEMANN_1/ProjectTrackingAPI/1).

### Postman
For easier interaction with the API you can import the postman workset, which can be found in `src/main/resources/project-tracking-server.postman_collection.json` of the project.