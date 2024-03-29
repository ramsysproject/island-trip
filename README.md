# island-trip

This application implements a reservation API for a newly formed island.
Since this is an intensively used application, optimistic locking mechanism has been applied in combination with 
HTTP ETags.

__Stack__
 
 SpringBoot, Spring Web, Spring Data Jpa, H2 in memory DB.

* **Build instructions**
1. Execute mvn clean install
2. Inside the target folder you can find the jar file.
There are a couple of ways to run the application:

* **Running instructions**

a. Using Maven Springboot plugin.


Run `mvn spring-boot:run`

b. Using jar file.

Run from project root `java -jar target/islandtrip-1.0.0-SNAPSHOT.jar`

c. Docker

1. You can also build locally and run it, for building locally execute the `build.sh` script. After this you can run the image by executing `docker run -p 9000:9000 island-trip`

* **How to run tests**

1. Run `mvn clean test` to execute unit tests
2. Run `mvn clean verify` to execute unit and integration tests

* **POSTMAN collection**

In the project root you will find a postman collection for triggering requests.

* **SWAGGER UI**

http://localhost:9000/swagger-ui.html
