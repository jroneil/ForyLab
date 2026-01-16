# Serialization Lab v3.0: Rigorous Performance Proof

This project is a high-performance benchmark laboratory designed to compare **Java Native Serialization** with **Apache Fory (formerly Apache Fury)** in a Spring Session (JDBC) environment. 

Version 3.0 is the most rigorous release, providing industrial-grade analytics to prove the benefits of modern serialization.

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

## 🎮 Rigorous Proof Features (v3.0)

The laboratory now includes five critical areas of proof:

### 1. 📦 Payload Size Analytics
Fory doesn't just run faster; it produces significantly smaller byte arrays. Smaller payloads reduce database storage costs and network bandwidth usage. The UI now features a **horizontal bar chart** comparing actual bytes stored.

### 2. 📈 Throughput Mode (Ops/Sec)
Beyond individual latency, we now measure **Throughput (Operations per Second)**. This illustrates how many more concurrent users a system can handle when switching to Fory.

### 3. 📊 Latency Histogram
Averages can be misleading. The v3.0 dashboard provides a **line chart distribution** of your samples. This allows you to visualize "jitter" and see Fory's consistent high performance compared to Java's overhead.

### 4. 🔄 Circular Reference Test
Real-world objects are often circular (e.g., Parent -> Child -> Parent). Use the **"Circular References"** toggle to prove that Fory's reference tracking is as robust and production-ready as native Java serialization.

### 5. 📥 Industrial Export
Finished your benchmarks? Click **"Export CSV"** to download all raw benchmark data, including averages, throughput, and P95 metrics, for inclusion in technical reports or architectural reviews.

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
3.  **Execute**: Click **"🔥 Run Comparison"**.
4.  **Analyze**: 
    - Check the **Speedup** multiplier in the Protocol Analysis window.
    - View the **Histogram** to see Fory's tight latency distribution.
    - Check the **Payload Size** chart to see the storage savings.

---

## 🛠 Project Structure

- `ForyConfig.java`: Configures the `ThreadSafeFory` bean (v0.14.1+).
- `QuoteFactory.java`: Industrial domain model generator supporting circular graphs.
- `CompareApiController.java`: High-concurrency benchmark engine.
- `compare.jsp`: Professional dashboard with Chart.js analytics.

## 📝 Troubleshooting

### "C:/Users/.../test" not found (H2 Console)
The H2 console defaults to a file-based URL. Ensure you change the **JDBC URL** to:
`jdbc:h2:mem:testdb`

---
*Built with ❤️ for performance-obsessed developers.*
