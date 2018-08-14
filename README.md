# Blog Sample App

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

```
maven 3x
java 8
```

### Installing

run the spring boot application
```
mvn spring-boot:run
```

that is it, the services are up and running

## Running the tests

```
mvn clean install
```

### Tests

Database Layer Tests
```
Identified as *RepositoryTest, just to test if the repositories are configurated and working well.
These tests were created using @DataJpaTest
```

Service Layer Tests
```
Identified as *ServiceTest, these unit tests are created to test all possibilities of *Service classes.
```

REST api Tests
```
Identified *ControllerTest, these unit tests the rest api using MockMvc in standalone mode(@WebMvcTest).

Identified by *ControllerIntegrationTests, these integration tests access since the rest services api until the database using MockMvc(@SpringBootTest).

These two kind of tests validate the REST API result httpcode and all response body information using json path.
```

Other tests
```
BlogsampleApplicationTests tests if the application configuration is starting withput errors
```

### Testing with Postman
Postman requests
```
https://www.getpostman.com/collections/38171f0a7efa715405a7
```


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Claudio Escobar Apparicio** - *Initial work* - [claudioescobar](https://github.com/claudioescobar)

## Aditional Notes

**#default jdbc-url for h2 db because spring-devtools is in use**

jdbc:h2:mem:testdb

**#spring-boot-auto-configuration auto generate db configuration**

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

**#h2 console url - available after the application is online with spring-devtools**

http://localhost:8080/h2-console

**#sonar**

docker container run --publish 9000:9000 --detach --name sonarqube sonarqube

mvn sonar:sonar -Dsonar.host.url=http://192.168.99.100:9000
  -Dsonar.login=the-generated-token

## TODO

- javadoc

- api documentation with swagger or other tool

- beans validation

- put layers into maven modules

- hateos

- configure jacoco for sonarqube code coverage

- add auto discovery with eureka

- add security

- add liquibase

- better logging
