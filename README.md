# Device Management Service
Version v1.0.0 \
This project provides endpoints for managing devices. Creating, Updating, Deleting, Listing, Searching devices.

## Building and starting the project
The project has been developed using JDK11 / Spring Boot 2.5.4 and PostgreSQL database Maven Project

In order to build the executable jar run:

```$ mvn clean package ```

The resulting jar will be located in ```./target/``` catalog.

Before running the program, we need to launch the database. We use a simple, yet powerful PostgreSQL database for this
purpose and Docker (with Testcontainers for testing). To launch the database, use `docker-compose` in the main project
directory as follows:
```$ docker-compose up --build```

This will bring up a running instance of PostgreSQL container in the background.

To shut down the container use: \
```$ docker-compose down```

To run the program execute the following command in the console:

```$ java -jar ./target/device-management-service-v1.0.0-SNAPSHOT.jar```

## Creating Docker Image
To produce docker image of device-management-service: \
``` docker build -t device-management-service:latest .```

## API definitions
To visit full API definitions go to: `http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config`

## Endpoints
* `POST /api/devices` create a device in the database, the payload structure:
```
{
    "deviceName": "....",
    "brandName": "...."
}
```
* `GET /api/devices/{deviceKey}` return a device given  `deviceKey` path variable
* `GET /api/devices?brandName={brandName}` return all device if the query parameter doesn't set, otherwise return all devices depends on search brandName
* `DELETE /api/devices/{deviceKey}` delete the device if the device is exists in db, otherwise return `NotFoudDeviceException`
* `PUT /api/devices/{deviceKey}` update the device defined by device key. If the device doesn't exist return `NotFoundDeviceException`
```
{
    "deviceName": "....",
    "brandName": "...."
}
```
* `PATCH /api/devices/{deviceKey}` partly update the device defined by device key.

## Architecture overview
MVC design pattern and stateless beans are used on the project. It means the server does not store any state about the client session on the server side. There are some very noticeable advantage for having REST APIs stateless:

Statelessness helps in scaling the APIs to millions of concurrent users by deploying it to multiple servers. Any server can handle any request because there is no session related dependency.
Being stateless makes REST APIs less complex – by removing all server-side state synchronization logic.
A stateless API is also easy to cache as well. Specific software can decide whether or not to cache the result of an HTTP request just by looking at that one request. There’s no nagging uncertainty that state from a previous request might affect the cacheability of this one. It improves the performance of applications.
The server never loses track of “where” each client is in the application because the client sends all necessary information with each request.

* We can implement caching on the GET endpoints with using one of the third party caching framework on the web app server.
* There are constraints on the POST endpoint to make the application stable.
* Spring boot global exception handler is used on the project.The advantage of global exception handling is all the code necessary for handling any kind of error in the application.
* Spring validation annotations are used in order to checks and validate user inputs.
* To manage Concurrent update on the database resource OptimisticLock mechanism is added. \
Code has been written using [Google Code Style](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)
