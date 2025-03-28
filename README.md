# Mongo Device Utility

This utility is used to sync device data between a MongoDB collection `DEVICE` and `DEVICE_BACKUP_OVERVIEW` collection.

## Prerequisites

Before you run the application, make sure you have the following:

- Java 21
- Maven 3.*.*
- MongoDB client set up and running

**Step 1:** Prepare the `connections.txt` File
You need to have a `connections.txt` file that contains your `MongoDB connection` details. The file should have the following format:

```
mongotemplate.username=[userName]
mongotemplate.password=[password]
mongotemplate.host=[host ip address]
mongotemplate.port=[port]
mongotemplate.db=[database name]
```
> Make sure to replace the values in the `connections.txt` file with your `actual MongoDB connection details`:

```
mongotemplate.username: MongoDB username
mongotemplate.password: MongoDB password
mongotemplate.host: MongoDB host (e.g., localhost or an IP address)
mongotemplate.port: MongoDB port (default is 27017, but make sure this is correct for your setup)
mongotemplate.db: The MongoDB database you wish to connect to
```
## Steps to Run the Application

Follow these steps to build and run the application:

### Step 1: Build the Project

First, navigate to the project directory and run the following Maven command to build the project and skip tests during the build process:
```bash
mvn clean package
```
### OR
```bash
mvn clean package -DskipTests
```
This command will compile the project, package it into a JAR file, and place it in the `target/ directory` of your project.

### Step 2: Run the Application
After the build completes, you can run the application using the following command:

```bash
java -jar target/mongo-device-utility-0.0.1.jar --connection.file.path="C:\path\to\connections.txt"
```

Make sure to replace `"C:\path\to\connections.txt"` with the correct path to your `connections.txt` file if it's located elsewhere.


# Running the `.jar` File

Run the `.jar` File with `Java` Installed
Once you have Java installed, you can run the application directly from the command line without needing to build anything. Follow these steps:

- **Step 1:** Download the `mongo-device-utility` `(mongo-device-utility-0.0.1.jar)` JAR file from the repository or ask the developer to provide it to you.

- **Step 2:** Prepare your `connections.txt` file as described in the `"Prerequisites"` section above.

- **Step 3:** Open a `command prompt (Windows)` or `terminal (macOS/Linux)` and navigate to the `folder` where the `.jar` file is located.

- Run the `application` by executing the following command:

    ```bash
    java -jar mongo-device-utility-0.0.1.jar --connection.file.path="C:\path\to\connections.txt"
    ```
> Make sure to replace the path `"C:\path\to\connections.txt"` or `"/path/to/connections.txt"` with the actual path to your `connections.txt` file.

### Troubleshooting
- If you encounter any issues, check the following:

- Connection Details: Ensure that the `MongoDB server` is running and the details in `connections.txt` are correct.

- Java Version: Make sure you're running Java 21 or later.

- Maven Version: Ensure you're using Maven 3.. or later.

### Notes:
1. You can replace the placeholders with your actual values where necessary.
2. This README includes all the steps for building, running the application, and configuring the connection details.
