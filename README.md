# Serialization Lab: Spring Session + Apache Fory

This project is a performance benchmark laboratory designed to compare **Java Native Serialization** with **Apache Fory (formerly Apache Fury)** when used within a Spring Session (JDBC) environment.

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### 1. Build the Project
Run the following command in the root directory to download dependencies and compile the code:
```powershell
mvn clean compile
```

### 2. Run the Application
Start the Spring Boot application:
```powershell
mvn spring-boot:run
```

### 3. Access the Lab
Open your browser and go to:
[http://localhost:8080/compare](http://localhost:8080/compare)

---

## 🔬 How to Test

1.  **Initialize Session**: Click **"Store in Session"**. This creates a complex `Quote` object (Insurance domain model), serializes it using both Java and Fory, and stores the resulting byte arrays in your HTTP Session (backed by H2 Database).
2.  **Run Benchmarks**: 
    - Click **"Bench Java"** to measure native deserialization speed.
    - Click **"Bench Fory"** to measure Fory's optimized deserialization speed.
    - Click **"Run Both"** for a side-by-side comparison.
3.  **Adjust Payload**: Change the **Payload Size (KB)** to see how Fory handles larger, more complex object graphs compared to Java.
4.  **Inspect Database**: Visit the H2 Console to see how the session data is stored:
    - **URL**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    - **JDBC URL**: `jdbc:h2:mem:testdb`
    - **User**: `sa`, **Password**: (blank)
    - **Query**: `SELECT * FROM SPRING_SESSION_ATTRIBUTES`

---

## 🛠 Project Structure

- `ForyConfig.java`: Configures the `ThreadSafeFory` bean for the application.
- `QuoteCodec.java`: A wrapper around Fory to handle business object serialization.
- `CompareApiController.java`: The REST back-end driving the benchmarks.
- `compare.jsp`: The modern, interactive dashboard (Glassmorphism design).

## 📝 Troubleshooting

### "ThreadLocalFory cannot be resolved"
This usually happens if the Maven dependencies aren't correctly synchronized. Ensure you have run `mvn clean install` and that your IDE is using the correct `pom.xml`.

### "Function [:formatBytes] not found"
This error occurs in JSP when client-side template literals (using `${}`) are mistaken for server-side Expression Language. We have escaped these as `\${}` to ensure they run in the browser.

---
*Built with ❤️ for performance-obsessed developers.*
