# Spring Boot MongoDB CRUD Application



## Overview

This project is about building a **Spring Boot REST API** using **Spring Data** and **MongoDB** to make CRUD operations to a MongoDB database that is a **Docker** image.

### Application Architecture Diagram

![Application-Architecture-Diagram](https://user-images.githubusercontent.com/25921121/141672227-2069aa84-0156-4590-aa77-4d1bd7ad5364.png)

### Built With

- Java 17
- Spring Boot 2.5.6
  - Spring Web
  - Spring Data MongoDB

### Project Structure

![Project Structure](https://user-images.githubusercontent.com/25921121/142077163-29d816ac-5dd3-4671-896b-51cf2d424066.png)

- `Student` data model class corresponds to the entity and the table student
- `StudentRepository` is an interface that extends `MongoRepository` interface for CRUD operations and it is where we define custom methods
- `StudentController` is where all the CRUD endpoints are defined
- `application.properties` is where we configure application parameters such as the connection to our MongoDB database
- `pom.xml` contains the configuration of the required dependencies of the applicaition

## Getting Started

### Setup Spring Boot project

Use [Spring web tool](https://start.spring.io/) or your development tool ([Spring Tool Suite](https://spring.io/tools), Eclipse, [Intellij](https://www.jetbrains.com/idea/download/)) to create a Spring Boot project.

Generate the project with the following dependencies:

- Spring Web
- Spring Data MongoDB
- Lombok

Or by manually adding this dependencies in our `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.6</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.medinar.practice</groupId>
    <artifactId>spring-boot-mongodb</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-boot-mongodb</name>
    <description>spring-boot-mongodb</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

### Configuring MongoDB and MongoExpress

To run a docker image from a container:
- Download Docker Desktop from their site:

[Get Started with Docker](https://www.docker.com/get-started)

- Create file inside the root directory of the application:
docker-compose.yaml
```yaml
version: "3.8"
services:
  mongodb:
    image: mongo
    container_name: mongodb
    ports:
      - 27017:27017
    volumes:
      - data:/data
    environment:
      - MONGO_INITDB_ROOT_USERNAME=rootuser
      - MONGO_INITDB_ROOT_PASSWORD=rootpass
  mongo-express:
    image: mongo-express
    container_name: mongo-express
    restart: always
    ports:
      - 8081:8081
    environment:
      - ME_CONFIG_MONGODB_ADMINUSERNAME=rootuser
      - ME_CONFIG_MONGODB_ADMINPASSWORD=rootpass
      - ME_CONFIG_MONGODB_SERVER=mongodb
volumes:
  data: {}
networks:
  default:
    name: mongodb_network
```

- Run it from IntelliJ by right clicking on the file then clicking on run docker-compose.yaml or via terminal:
 ```shell
~/git   
❯ cd practice/spring-boot-mongodb 
                                                                                                                                                                                 
spring-boot-mongodb git/main*  
❯ docker-compose -f docker-compose.yaml up -d

 ```

- Mongo Express

![Mongo Express](https://user-images.githubusercontent.com/25921121/141672229-b2dffc56-59fa-4ea6-bc2b-b99fa71e4d7e.png)

### Connecting to the database

- Add the following configurations inside the `application.properties`:
```properties
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.username=rootuser
spring.data.mongodb.password=rootpass
spring.data.mongodb.database=medinardb
spring.data.mongodb.port=27017
spring.data.mongodb.host=localhost
```

- Rerun the application.

### Creating the Model

`Address.java`

```java
package com.medinar.practice.springbootmongodb.student;

import lombok.Data;

@Data
public class Address {
    private String country;
    private String city;
    private String postCode;
}
```

`Gender.java`

```java
package com.medinar.practice.springbootmongodb.student;

public enum Gender {
    MALE, FEMALE
}
```

`Student.java`

```java
package com.medinar.practice.springbootmongodb.student;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Student {
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private Address address;
    private List<String> favouriteSubjects;
    private BigDecimal totalSpentInBooks;
    private LocalDateTime created;
}
```

### Annotating the Model classed with `@Document` and `@Id`

Here's the Student class after adding the required annotations:
```java
package com.medinar.practice.springbootmongodb.student;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Student {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private Address address;
    private List<String> favouriteSubjects;
    private BigDecimal totalSpentInBooks;
    private LocalDateTime created;
}
```

### Mongo Repository

Create an interface called StudentRepository and extend the MongoRepository interface

```java
package com.medinar.practice.springbootmongodb.student;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
}

```

Insert Student to the database using the repository
```java
package com.medinar.practice.springbootmongodb;

import com.medinar.practice.springbootmongodb.student.Address;
import com.medinar.practice.springbootmongodb.student.Gender;
import com.medinar.practice.springbootmongodb.student.Student;
import com.medinar.practice.springbootmongodb.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class SpringBootMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMongodbApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository) {
        return args -> {
            Address address = new Address(
                    "Makati City",
                    "Philippines",
                    "1200"
            );

            Student student = new Student(
                    "Juan",
                    "Dela Cruz",
                    "juan.delacruz@medinar.com",
                    Gender.MALE,
                    address,
                    List.of("Computer Science", "English"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

            repository.insert(student);
        };
    }

}
```

### Adding indexes
This is to speed up when searching for student by email and to guarantee that no duplicate email is inserted to the database
- Let's index the email field by adding `@Indexed` annotation:

``` java
public class Student {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private Gender gender;
    private Address address;
    private List<String> favouriteSubjects;
    private BigDecimal totalSpentInBooks;
    private LocalDateTime created;
    ...
```

- We need to enable the indexing by adding the `spring.data.mongodb.auto-index-creation=true` inside the `application.properties`

```properties
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.username=rootuser
spring.data.mongodb.password=rootpass
spring.data.mongodb.database=medinardb
spring.data.mongodb.port=27017
spring.data.mongodb.host=localhost
spring.data.mongodb.auto-index-creation=true
```

Run the application to test.
This should be the error, because the student with email juan.delacruz@medinar.com already exists

``` java
Caused by: com.mongodb.DuplicateKeyException: Write failed with error code 11000 and error message 'Index build failed: 236de8ca-eea3-4aff-92b0-bca0244daffb: Collection medinardb.student ( 25cdc450-7b32-4472-a982-19038243d3d6 ) :: caused by :: E11000 duplicate key error collection: medinardb.student index: email dup key: { email: "juan.delacruz@medinar.com" }'
	at com.mongodb.internal.operation.CreateIndexesOperation.checkForDuplicateKeyError(CreateIndexesOperation.java:338) ~[mongodb-driver-core-4.2.3.jar:na]
	at com.mongodb.internal.operation.CreateIndexesOperation.access$300(CreateIndexesOperation.java:72) ~[mongodb-driver-core-4.2.3.jar:na]
	at com.mongodb.internal.operation.CreateIndexesOperation$1.call(CreateIndexesOperation.java:200) ~[mongodb-driver-core-4.2.3.jar:na]
	at com.mongodb.internal.operation.CreateIndexesOperation$1.call(CreateIndexesOperation.java:192) ~[mongodb-driver-core-4.2.3.jar:na]
	at com.mongodb.internal.operation.OperationHelper.withConnectionSource(OperationHelper.java:650) ~[mongodb-driver-core-4.2.3.jar:na]
	at com.mongodb.internal.operation.OperationHelper.withConnection(OperationHelper.java:612) ~[mongodb-driver-core-4.2.3.jar:na]
	at com.mongodb.internal.operation.CreateIndexesOperation.execute(CreateIndexesOperation.java:192) ~[mongodb-driver-core-4.2.3.jar:na]
	at com.mongodb.internal.operation.CreateIndexesOperation.execute(CreateIndexesOperation.java:72) ~[mongodb-driver-core-4.2.3.jar:na]
	at com.mongodb.client.internal.MongoClientDelegate$DelegateOperationExecutor.execute(MongoClientDelegate.java:187) ~[mongodb-driver-sync-4.2.3.jar:na]
	at com.mongodb.client.internal.MongoCollectionImpl.executeCreateIndexes(MongoCollectionImpl.java:847) ~[mongodb-driver-sync-4.2.3.jar:na]
	at com.mongodb.client.internal.MongoCollectionImpl.createIndexes(MongoCollectionImpl.java:830) ~[mongodb-driver-sync-4.2.3.jar:na]
	at com.mongodb.client.internal.MongoCollectionImpl.createIndexes(MongoCollectionImpl.java:825) ~[mongodb-driver-sync-4.2.3.jar:na]
	at com.mongodb.client.internal.MongoCollectionImpl.createIndex(MongoCollectionImpl.java:810) ~[mongodb-driver-sync-4.2.3.jar:na]
	at org.springframework.data.mongodb.core.DefaultIndexOperations.lambda$ensureIndex$0(DefaultIndexOperations.java:131) ~[spring-data-mongodb-3.2.6.jar:3.2.6]
	at org.springframework.data.mongodb.core.MongoTemplate.execute(MongoTemplate.java:553) ~[spring-data-mongodb-3.2.6.jar:3.2.6]
	... 58 common frames omitted
```

### MongoTemplate and Queries

- Finding Student by email using MongoDB Core's `Query`

`SpringBootMongodbApplication.java`

``` java
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(email));
```

- Executing the query using `MongoTemplate`

``` java
List<Student> students = mongoTemplate.find(query, Student.class);
```

- Handling the duplicate exception

``` java
            if (students.size() > 1) {
                throw new IllegalStateException("found many students with email " + email);
            }

            if (students.isEmpty()) {
                System.out.println("Inserting student " + student);
                repository.insert(student);
            } else {
                System.out.println(student + " already exists");
            }
```

### Queries and Method Names

- Defining additional method in the `StudentRepository` class
```java
package com.medinar.practice.springbootmongodb.student;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findStudentByEmail(String email);
}
```

- Calling the newly defined method
``` java
repository.findStudentByEmail(email)
        .ifPresentOrElse(s -> {
            System.out.println(s + " already exists");
        }, () -> {
            System.out.println("Inserting student " + student);
            repository.insert(student);
        });
```

### CRUD Example with MongoTemplate

https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb-template-update

```java
// https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb-template-update

package org.spring.example;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import com.mongodb.client.MongoClients;

public class MongoApp {

  private static final Log log = LogFactory.getLog(MongoApp.class);

  public static void main(String[] args) {

    MongoOperations mongoOps = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "database"));

    Person p = new Person("Joe", 34);

    // Insert is used to initially store the object into the database.
    mongoOps.insert(p);
    log.info("Insert: " + p);

    // Find
    p = mongoOps.findById(p.getId(), Person.class);
    log.info("Found: " + p);

    // Update
    mongoOps.updateFirst(query(where("name").is("Joe")), update("age", 35), Person.class);
    p = mongoOps.findOne(query(where("name").is("Joe")), Person.class);
    log.info("Updated: " + p);

    // Delete
    mongoOps.remove(p);

    // Check that deletion worked
    List<Person> people =  mongoOps.findAll(Person.class);
    log.info("Number of people = : " + people.size());


    mongoOps.dropCollection(Person.class);
  }
}
```

### Building the API

- Create `StudentController` class

  ```java
  package com.medinar.practice.springbootmongodb.student;
  
  import lombok.AllArgsConstructor;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;
  
  import java.util.List;
  
  @RestController
  @RequestMapping("api/v1/students")
  @AllArgsConstructor
  public class StudentController {
  
      private final StudentService studentService;
  
      @GetMapping
      public List<Student> fetchAllStudents() {
          return studentService.getAllStudents();
      }
  }
  ```

- Create the `StudentService` class

  ```java
  package com.medinar.practice.springbootmongodb.student;
  
  import lombok.AllArgsConstructor;
  import org.springframework.stereotype.Service;
  
  import java.util.List;
  
  @AllArgsConstructor
  @Service
  public class StudentService {
  
      private final StudentRepository studentRepository;
  
      public List<Student> getAllStudents() {
          return studentRepository.findAll();
      }
  }
  ```

  

#### Create Operation

We define a method called `addStudent(@RequestBody Student student)` with `@PostMapping` annotation to handle POST requests from clients. This will invoke the `addStudent(student)` method of `StudentService` and then call the `save(student)` method of the `StudentRepository` class. This will create a new Student entry in the database if the operation is successful.

`StudentController.java`

```java
@PostMapping
public ResponseEntity<Student> addStudent(@RequestBody Student student) throws BadRequestException {
  try {
    Student _student = studentService.addStudent(student);
    return new ResponseEntity<>(_student, CREATED);
  }
  catch (Exception ex) {
    return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
  }
}
```

`StudentService.java`

```java
    public Student addStudent(Student student) throws BadRequestException {
        Boolean emailExists = studentRepository.existsByEmail(student.getEmail());
        if (emailExists) {
            throw new BadRequestException(String.format(
                    "Email `%s` already exist",
                    student.getEmail()
            ));
        }
        return studentRepository.insert(student);
    }
```

#### Retrieve Operation

We define a method called `fetchAllStudents()` with `@GetMapping` annotation to handle Http Get requests from clients. This will invoke the `getAllStudents()` method of `StudentService` and then call the `findAll()` method of the `StudentRepository` class. This will return all Student entries in the database if the operation is successful.

`StudentController.java`

```java
      @GetMapping
      public ResponseEntity<List<Student>> fetchAllStudents() {
          List<Student> students = studentService.getAllStudents();
          if (students.isEmpty()) {
              return new ResponseEntity<>(students, NO_CONTENT);
          } else {
              return new ResponseEntity<>(students, OK);
          }
      }
```

`StudentService.java`

```java
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
```

#### Update Operation

We define a method called `updateStudent(@RequestBody Student student, @PathVariable String studentId)` with `@PutMapping` annotation to handle Http Put requests from clients.

- `updateStudent()` accepts `studentId` and `student` payload as parameters
- using the `studentId` we retrieve the Student entry from the database
- the student entry will be updated with the information coming from the payload and then saved to the database

 `StudentController.java`

```java
      @PutMapping("/{studentId}")
      public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable String studentId) {
          try {
              Student _student = studentService.updateStudent(student, studentId);
              return new ResponseEntity<>(_student, OK);
          } catch (Exception ex) {
              return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
          }
  
      }
```

`StudentService.java`

```java
public Student updateStudent(Student student, String studentId) throws IllegalStateException {
        Student _student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("student with id %s does not exists", studentId)
                ));

        if (student.getFirstName() != null
                && student.getFirstName().length() > 0 &&
                !Objects.equals(_student.getFirstName(), student.getFirstName())
        ) {
            _student.setFirstName(student.getFirstName());
        }

        if (student.getLastName() != null
                && student.getLastName().length() > 0 &&
                !Objects.equals(_student.getLastName(), student.getLastName())
        ) {
            _student.setLastName(student.getLastName());
        }

        if (student.getEmail() != null &&
                student.getEmail().length() > 0 &&
                !Objects.equals(_student.getEmail(), student.getEmail())
        ) {
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new IllegalStateException("email taken");
            }
            _student.setEmail(student.getEmail());
        }

        if (student.getGender() != null &&
                student.getEmail().length() > 0 &&
                !Objects.equals(_student.getGender(), student.getGender())
        ) {
            _student.setGender(student.getGender());
        }

        if (student.getAddress() != null && student.getAddress().getCountry() != null &&
                student.getAddress().getCountry().length() > 0 &&
                !Objects.equals(_student.getAddress().getCountry(), student.getAddress().getCountry())
        ) {
            _student.getAddress().setCountry(student.getAddress().getCountry());
        }

        if (student.getAddress() != null && student.getAddress().getCity() != null &&
                student.getAddress().getCity().length() > 0 &&
                !Objects.equals(_student.getAddress().getCity(), student.getAddress().getCity())
        ) {
            _student.getAddress().setCity(student.getAddress().getCity());
        }

        if (student.getAddress() != null && student.getAddress().getPostCode() != null &&
                student.getAddress().getPostCode().length() > 0 &&
                !Objects.equals(_student.getAddress().getPostCode(), student.getAddress().getPostCode())
        ) {
            _student.getAddress().setPostCode(student.getAddress().getPostCode());
        }

        if (student.getFavouriteSubjects() != null && !student.getFavouriteSubjects().isEmpty()) {
            _student.setFavouriteSubjects(student.getFavouriteSubjects());
        }

        if (student.getTotalSpentInBooks() != null &&
                !student.getTotalSpentInBooks().equals(_student.getTotalSpentInBooks())
        ) {
            _student.setTotalSpentInBooks(student.getTotalSpentInBooks());
        }

        if (student.getCreated() != null &&
                !student.getCreated().equals(_student.getCreated())
        ) {
            _student.setCreated(student.getCreated());
        }
        return studentRepository.save(_student);
    }
```

#### Delete Operation

We define a method called `deleteStudent(@PathVariable String studentId)` with `@DeleteMapping` annotation to handle Http DELETE requests from clients. This will invoke the `deleteStudent(studentId)` method of `StudentService` and then call the `deleteById(studentId)` method of the `StudentRepository` class. If the student with the given `studentId` exists and the delete operation is successful, the Student entry will be removed from the database.

`StudentController.java`

```java
      @DeleteMapping("/{studentId}")
      public ResponseEntity<HttpStatus> deleteStudent(@PathVariable String studentId) {
          try {
              studentService.deleteStudent(studentId);
              return new ResponseEntity<>(OK);
          } catch (Exception ex) {
              return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
          }
      }
```

`StudentService.java`

```java
    public void deleteStudent(String studentId) throws NotFoundException {
        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException(String.format(
                    "Student with id %s does not exists",
                    studentId
            ));
        }
        studentRepository.deleteById(studentId);
    }
```

## Testing

### Using the Intellij Generated Requests

By clicking on the globe icon beside each of the maaping annotations a test file which contains the http requests will be generated. 

![Intellij Generated Http Request](https://user-images.githubusercontent.com/25921121/142082166-953e7835-4e3d-4cb8-add5-c9cc7422a237.png)

Below is the sample file generated`generated-requests.http`:

```
###
POST http://localhost:8080/api/v1/students
Content-Type: application/json

{
   "firstName": "Juan",
   "lastName": "Dela Cruz",
   "email": "juan.delacruz-2@medinar.com",
   "gender": "MALE",
   "address": {
     "country": "Makati City",
     "city": "Philippines",
     "postCode": "1200"
   },
   "favouriteSubjects": [
      "Computer Science",
      "English"
   ],
   "totalSpentInBooks": 10,
   "created": "2021-11-13T22:09:22.249"
}

###
DELETE http://localhost:8080/api/v1/students/{{studentId}}

###
DELETE http://localhost:8080/api/v1/students/6190da1118243d7111ecdbce

###
PUT http://localhost:8080/api/v1/students/{{studentId}}

###
PUT http://localhost:8080/api/v1/students/6190e7c50e8f96294479bfa2
Content-Type: application/json

{
   "firstName": "Maria",
   "lastName": "Dela Cruz",
   "email": "maria.delacruz@medinar.com",
   "gender": "FEMALE",
   "address": {
     "country": "Makati City",
     "city": "Philippines",
     "postCode": "1200"
   },
   "favouriteSubjects": [
      "Computer Science"
   ],
   "totalSpentInBooks": 15,
   "created": "2021-11-13T22:09:22.249"
}

```

### Using an API Client such as Postman

## License

Distributed under the MIT License. See LICENSE.txt for more information.

## Contact

Rommel Medina - rommel.d.medina@gmail.com

## Acknowledgments

- Hat tip to anyone whose code was used
- Inspiration
- etc.
