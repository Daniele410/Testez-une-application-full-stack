# Yoga App

Yoga App is an application that empowers users to easily subscribe or unsubscribe from yoga classes, while the administrator efficiently organizes sessions based on teacher availability.

## Technologies
- NodeJS v18+ ![NodeJS](https://img.shields.io/badge/NodeJS-v18+-brightgreen)
- Angular CLI v14+ ![Angular](https://img.shields.io/badge/Angular_CLI-v14+-red)
- Java 11+ ![Java](https://img.shields.io/badge/Java-11+-blue)
- SpringBoot 2.6.1 ![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.6.1-brightgreen)
- jjwt 0.9.1 ![JWT](https://img.shields.io/badge/JWT-0.9.1-orange)
- MySQL v8.0 ![MySQL](https://img.shields.io/badge/MySQL-v8.0-blue)
- H2 Database ![H2](https://img.shields.io/badge/H2-Supported-brightgreen)
- Maven 3.8.6 ![Maven](https://img.shields.io/badge/Maven-Latest-orange)
- Jest 29.7.0 ![Jest](https://img.shields.io/badge/Jest-Supported-brightgreen)
- Cypress ![Cypress](https://img.shields.io/badge/Cypress-Supported-brightgreen)
- JUnit ![JUnit](https://img.shields.io/badge/JUnit-Supported-brightgreen)
- Mockito ![Mockito](https://img.shields.io/badge/Mockito-Supported-brightgreen)
- jacoco 0.8.5 ![jacoco](https://img.shields.io/badge/jacoco-0.8.5.Supported-brightgreen)

## Database Installation

1. Ensure you have a database instance (MySQL) installed and properly configured on your machine.
2. Execute the provided database creation script located in the `resources/sql` folder to establish the necessary database structure.
3. Provide the database connection information in the `application.properties` configuration file.

### Required Environment Variables for MySQL

- `MYSQL_URL`: The url of your MySQL database. For example, `jdbc:mysql://localhost:3306/`.
- `MYSQL_DATABASE`: The name of your MySQL database. For example, `db_yoga`.
- `MYSQL_USER`: Your MySQL username.mysql_database
- `MYSQL_PASSWORD`: Your MySQL password.

## Application Installation

1. Ensure you have the necessary dependencies installed: Java, Node.js, Maven.
2. Clone this repository to your local machine.
3. Navigate to the back-end project directory and execute the command `mvn clean install` to download dependencies and compile the project.
4. Navigate to the front-end project directory and run the command `npm install` to install front-end dependencies.

## Running the Application

1. Navigate to the back-end project directory and execute the command `mvn spring-boot:run` to start the back-end application.
2. Navigate to the front-end project directory and execute the command `npm run start` to launch the front-end application.

## Running Tests

### Front-end Unit and Integration Tests (Jest)

1. Navigate to the front-end project directory and run the command `npm run test` to execute front-end unit tests using Jest.
2. To view the coverage report, run the command `npm run test:coverage`. 
3. The report `index.html` will be generated in the `front/coverage/jest/lcov-report/index.html` directory.

### End-to-End Tests (Cypress)

1. Navigate to the front-end project directory and run the command `npm run e2e` to perform end-to-end tests with Cypress.
2. To view the coverage report, run the command `npm run e2e:coverage`. 
3. The report `index.html` will be generated in the `front/coverage/lcov-report/index.html` directory.

### Back-end Unit and Integration Tests (JUnit and Mockito)

1. Ensure you run the provided database script in the `resources/sql` folder before executing the tests.
2. Navigate to the back-end project directory and execute the command `mvn clean test` to run back-end unit and integration tests using JUnit and Mockito.
3. The coverage report will be generated in the `back/target/site/jacoco/index.html` directory.


Ensure you have the required versions of Java, Node.js, Maven, and Angular CLI (version 14.1.0) installed to ensure compatibility with the project.