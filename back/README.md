## Introduction

This application serves as a straightforward tool for managing yoga classes. It streamlines scheduling, attendance
tracking, and student progress monitoring. The application is developed using Java 8.

## Java Version Configuration

For Windows users, modifying the Java version utilized by the application involves the following steps:

1. Open the Command Prompt.

2. Execute the following command to display the current Java version:
    ```cmd
    echo %JAVA_HOME%
    ```

3. Set the JAVA_HOME environment variable to the path of your desired Java version.

   For instance, to use Java 11, execute the following command:
    ```cmd
    set JAVA_HOME=C:\Program Files\Java\jdk-11.0.12
    ```
   Replace `jdk-11.0.12` with the actual version number of your Java installation.

4. Close the Command Prompt.

## Generating Code Coverage Report

To generate the Jacoco code coverage report, follow these steps:

1. Ensure Maven is installed on your system.

2. Open a terminal and navigate to the project directory.

3. Execute the following command:
    ```cmd
    mvn clean test
    ```
   This command compiles the project, executes the unit tests, and generates the Jacoco code coverage report. The report
   is located in the `target` directory under the name `jacoco-it.report`.