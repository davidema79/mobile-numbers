# Mobile Numbers Subscription

Table of content
================
1. [Introduction](#introduction)
1. [How to Build](#how-to-build)
1. [How to Run](#how-to-run)
1. [Endpoints](#endpoints)



## Introduction
This project is a simple example on how to integrate SpringBoot 2 with Hibernate and by exposing several endpoints.

Hibernate works in runtime with MySql Server, and during the integration tests phase with the in-memory database H2

## How to Build
This is a maven project, therefore the usual commands can be used:

**Build package a execute Unit Tests**
```
  $ mvn clean package
```


**Build package a execute Unit Test and Integration Tests**
```
  $ mvn clean verify
```

**Build Docker image**

After building the package it is possible create the docker image from the `mvn` command line:
```
  $ mvn fabric8:build

  # a new docker image 'numbers/mobile-numbers' has been created
  # then you can push to the docker registry configured in the current machine:

  $ mvn fabric8:push

```
Please refer to [Fabric8 Maven Plugin](https://github.com/fabric8io/fabric8-maven-plugin/tree/v4.0.0) for further info about the docker maven plugin features.

## How to Run
After packaging the application it may be run as a normal SpringBoot application or docker container.

Either cases the database is expected to be up and waiting for a connection. Please refer to the `DatabaseTable.sql` file to create the table.

## Endpoints
Please for a complete description of the endpoints refer to the `API.yml` file coded in OpenAPI format and included in the main directory.

Please note that the endpoints are secured by *`Basic Authentication`* mechanism. The credentials can be found and changed from the `application.yml` file.

The following is a simplified list of the endpoints:

1. GET http://localhost:8080/api/v1/mobile-numbers (Returns the list of subscriptions in the database)
1. GET http://localhost:8080/api/v1/mobile-numbers/{id} (Returns the details of a single subscription)
1. POST http://localhost:8080/api/v1/mobile-numbers (Create a new subscription). Request Body example:
```
{
	"mobileNumber": "35699876545",
	"ownerId": 12,
	"userId": 90,
	"serviceType": "Postpaid"
}
```
1. PATCH http://localhost:8080/api/v1/mobile-numbers/{id} (Patch the details for a single subscription). Request body example:
```
{
	"ownerId": 12,
	"userId": 90,
	"serviceType": "Postpaid"
}
```
   Please note that the above body request is modifying at one the mobile number plan, the user id and the owner id. However separate several requests may be performed with a single field/value per request and the same final result will be obtained.
1. DELETE http://localhost:8080/api/v1/mobile-numbers/{id} (Remove the resource from the database)
