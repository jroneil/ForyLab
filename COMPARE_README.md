# Session Serialization Comparison Laboratory

This proof-of-concept demonstrates the performance benefits of using **Apache Fury (Fory)** for serializing complex Java objects stored in **Spring Session JDBC**.

## How to Run

1.  **Build the Project**:
    ```powershell
    mvn clean compile
    ```

2.  **Start the Application**:
    ```powershell
    mvn spring-boot:run
    ```

3.  **Access the Comparison Page**:
    Open your browser and navigate to:
    `http://localhost:8080/compare`

## Key Scenarios to Test

1.  **Small vs Large Objects**:
    - Set `Payload Size` to `50 KB` and run benchmark.
    - Set `Payload Size` to `1000 KB` (1 MB) and run benchmark. Observe how Fury generally maintains a significant lead as object complexity grows.

2.  **Spring Session Impact**:
    - The benchmark measures the deserialization time from the byte arrays stored in the `HttpSession`. 
    - In a real Spring Session JDBC setup, these bytes are retrieved from the `SPRING_SESSION_ATTRIBUTES` table.

## Implementation Details

- **Java Native**: Uses `ObjectOutputStream` / `ObjectInputStream`.
- **Apache Fory (Fury)**: Uses `ThreadSafeFury` with reference tracking enabled and class registration optional.
- **Quote Domain**: A complex nested structure consisting of Coverages, Drivers, and Vehicles to simulate real-world insurance data.

## Database Inspection
You can view the session data in the H2 console:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Query: `SELECT * FROM SPRING_SESSION_ATTRIBUTES`
