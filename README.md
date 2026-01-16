# Serialization Lab v3.2: Rigorous Performance Proof

This project is a high-performance benchmark laboratory designed to compare **Java Native Serialization** with **Apache Fory (formerly Apache Fury)** in a Spring Session (JDBC) environment. 

Version 3.2 is the most comprehensive release, providing deep JVM observability and advanced statistical analysis to prove the benefits of modern serialization.

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+

### 1. Build the Project
```powershell
mvn clean compile
```

### 2. Run the Application
```powershell
mvn spring-boot:run
```

### 3. Access the Lab
[http://localhost:8080/compare](http://localhost:8080/compare)

---

## 🎮 Rigorous Proof Features (v3.2)

The laboratory now includes seven critical areas of proof:

### 1. JVM Impact Analysis (NEW v3.2)
Capture hard evidence of system overhead beyond just latency. The **Protocol Analysis** window now surfaces:
- **Memory Delta**: Absolute heap usage change during the test.
- **GC Collections**: Number of garbage collection events triggered.
- **GC Time**: Total JVM pause time spent in garbage collection.

### 2. Advanced Statistical Metrics (NEW v3.2)
Go beyond simple averages with high-fidelity latency tracking:
- **P50 (Median)**: Typical user experience.
- **P99 (Tail)**: Captures the worst-case outliers (the "long tail").
- **StdDev**: Measures performance consistency and "jitter."

### 3. Payload Size Analytics
Fory produces significantly smaller byte arrays than Java. Smaller payloads reduce database storage costs and network bandwidth usage. The UI features a **horizontal bar chart** for visual comparison.

### 4. Throughput Mode (Ops/Sec)
Measure how many operations the system can handle per second. This illustrates the scalability benefits for high-traffic environments.

### 5. Latency Histogram with Log Scale
Visualize the distribution of samples. v3.2 adds a **Log scale toggle** to better inspect tail latency outliers when the performance gap is significant.

### 6. Circular Reference Test
Prove robustness with complex domain models. Use the **"Circular References"** toggle to verify Fory's reference tracking (e.g., Parent -> Child -> Parent).

### 7. Industrial Export (Enhanced)
Download a comprehensive **CSV Export** containing all 14 data points, including p-levels, throughput, memory deltas, and GC metrics for technical reports.

---

## 🗄️ Database Switching: MS SQL Server

By default, the lab uses an in-memory **H2 Database**. To test against **MS SQL Server**:

1.  **Configure**: Edit `src/main/resources/application-sqlserver.properties` with your SQL Server connection string and credentials.
2.  **Run with Profile**: Use the `sqlserver` Spring profile:
    ```powershell
    mvn spring-boot:run -Dspring.profiles.active=sqlserver
    ```
    *Note: The application will automatically initialize the Spring Session tables in the target database on first run.*

---

## 🔬 How to Run a Rigorous Test

1.  **Prepare Environment**: Choose your **Object Size** (e.g., 500 KB) and toggle **Circular References**. Click **"Prepare Object"**.
2.  **Heat the JIT**: Set **Warm-up Cycles** to `2000` and **Iterations** to `500`.
3.  **Execute**: Click **"Run Comparison"**. 
4.  **Analyze**: 
    - Check the **jvmImpact** block in the Protocol Analysis window for GC/Memory data.
    - Toggle **Log scale** on the Histogram to inspect performance consistency.
    - Use **🔁 Re-run Same Config** to verify results without regenerating the object.
5.  **Export**: Click **"Export CSV"** to save your evidence.

---

## 🛠 Project Structure

- `ForyConfig.java`: Configures the `ThreadSafeFory` bean (v0.14.1+).
- `QuoteFactory.java`: Industrial domain model generator supporting circular graphs.
- `CompareApiController.java`: High-concurrency benchmark engine with JVM metric capture.
- `compare.jsp`: Professional dashboard with Chart.js analytics and v3.2 UX polish.

## 📝 Troubleshooting

### JDBC Connection Errors
If using SQL Server, ensure the credentials in `application-sqlserver.properties` are correct and the database is reachable.

### H2 Console Access
The H2 console defaults to a file-based URL. Ensure you change the **JDBC URL** to:
`jdbc:h2:mem:testdb`

---
## ⚖️ License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Built with ❤️ for performance-obsessed developers.*
