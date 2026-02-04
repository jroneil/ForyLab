# Serialization Lab v5.0: Rigorous Performance Proof
This project is a high-performance benchmark laboratory designed to compare **Java Native Serialization** with **Apache Fory** in various Spring Session environments (JDBC, Redis, or In-Memory). 

Version 5.0 introduces **End-to-End I/O Comparison** across multiple backends and deep **Network Efficiency** analytics.
![Serialization Lab v5.0 Dashboard](lab.png)

## Why this exists

Serialization is often blamed for performance problems without evidence.
This lab exists to measure, compare, and understand the real impact of
different Java serialization approaches before prematurely optimizing systems.

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
# Standard mode (Memory + JDBC)
mvn spring-boot:run "-Dspring-boot.run.profiles=h2"

# Full mode (Memory + JDBC + Redis)
mvn spring-boot:run "-Dspring-boot.run.profiles=h2,redis-session"
```

### 3. Access the Lab
[http://localhost:8080/compare](http://localhost:8080/compare)

---

## 🎮 Rigorous Proof Features (v5.0)

The laboratory now includes fourteen critical areas of proof:

### 1. End-to-End I/O Comparison (NEW v5.0)
A specialized "Three-Backend" benchmark that runs side-by-side across:
- **Local Memory**: Pure serialization overhead.
- **Redis (NoSQL)**: High-speed binary key-value storage.
- **JDBC (SQL)**: Relational BLOB storage (H2 or SQL Server).
This proves Fory's efficiency is compounding—latency savings are realized in both CPU cycles and I/O wait times.

### 2. Redis/IO Impact Analytics (NEW v5.0)
A deep-dive card that isolates **Network Efficiency**:
- **Payload Reductions**: Quantifies the % reduction in bytes traveling over the wire.
- **I/O Transit Speedup**: Measures how much faster smaller packets travel between the app and Redis.

### 3. Object Summary (NEW v5.0)
Immediately after preparing a test object, the lab displays a structural summary:
- **Class Map**: Maps the generated object to its serialization-ready class.
- **Entity Counts**: Shows the number of nested Vehicles, Drivers, or Map entries created by the industrial factory.

### 4. JVM Impact Card
A dedicated UI visualization for system overhead:
- **Memory Δ**: Absolute heap usage change during the test.
- **GC Collections**: Number of garbage collection events triggered.
- **GC Time**: Total JVM pause time spent in garbage collection.

### 5. Advanced Statistical Metrics
Go beyond simple averages with high-fidelity latency tracking:
- **P50 (Median)**: Typical user experience.
- **P99 (Tail)**: Captures the worst-case outliers.
- **StdDev**: Measures performance consistency and "jitter."

### 6. Payload Size Analytics
Fory produces significantly smaller byte arrays than Java. Smaller payloads reduce database storage costs and network bandwidth usage. The UI features a horizontal bar chart and explicit byte breakdowns.

### 7. Latency Histogram with Log Scale
Visualize the distribution of samples. Features a **Log scale toggle** to better inspect tail latency outliers (P99 spikes).

### 8. Circular Reference Test
Prove robustness with complex domain models. Use the "Circular References" toggle to verify reference tracking (e.g., Parent -> Child -> Parent).

### 9. Industrial Export (CSV)
Download a comprehensive **CSV Export** containing all 14 data points for technical reports.

### 10. One-Click Benchmark Report (PDF-Ready)
Generate beautifully styled summaries including:
- **Executive Summary**: Speedup calculations and payload ratios.
- **Protocol Analysis JSON**: A machine-readable report with backend key formats.

### 11. Smart Winner Highlighting
Identifies which protocol "won" based on memory allocation and GC pressure.

### 12. Multi-Object Type Benchmarking
- **Quote**: Standard flat object.
- **Insurance Policy**: Deeply nested structure.
- **Collections Blob**: Map/List heavy generic payloads.

### 13. Recommended Defaults
Context-aware iterations and warmup logic to ensure JIT optimization (C2 compiler) is reached before measurement begins.

### 14. Reset Lab (NEW v5.0)
One-click hard reset to clear all bench data, charts, and state for a fresh comparison run.

---

## 🧪 Deep Dive: What These Tests Prove

The laboratory is designed to simulate high-pressure production environments. Here is the scientific significance of each benchmark:

### **1. Serialization (Write) Performance**
*   **The Proof**: Measures the time taken to convert a session object into bytes before saving to the database.
*   **Real-World Impact**: High serialization latency blocks the application thread before the database I/O even begins. Faster serialization reduces **Time-to-First-Byte (TTFB)** and improves overall system responsiveness.

### **2. Deserialization (Read) Performance**
*   **The Proof**: Measures the time to reconstruct the object from bytes upon every HTTP request.
*   **Real-World Impact**: This is the most frequent operation in stateful apps. Speeding this up directly lowers the "overhead cost" of using Spring Session, making it feel as fast as an in-memory session.

### **3. Payload Size & I/O Reduction**
*   **The Proof**: Compares the raw byte count of the serialized data.
*   **Real-World Impact**: Since we are using **JDBC-based sessions**, the CPU speed is only half the story. Smaller payloads mean **less data traveling over the network** and **faster database writes/reads**. This reduces the load on your database server (Postgres, SQL Server, etc.).

### **4. Reference Tracking & Safety (Circular Refs)**
*   **The Proof**: Tests if the serializer can handle objects that point back to themselves without infinite loops or stack overflows.
*   **Real-World Impact**: Most "fast" serializers (like JSON or older binary protocols) break on complex business objects. Proving Fory handles circular graphs confirms it is a **safe, drop-in replacement** for native Java serialization without requiring code changes.

### **3. GC Pressure & JVM Longevity**
*   **The Proof**: Uses the **jvmImpact** metrics (GC Time/Memory Delta) to see how "stressed" the JVM becomes during high iteration bursts.
*   **Real-World Impact**: High-performance apps often suffer from "tail latency" (P99) spikes caused by Garbage Collection pauses. Fory uses optimized buffer recycling to reduce temporary object creation, leading to a **flatter latency curve** and a more stable production environment.

### **4. Storage Efficiency (Redis/SQL)**
*   **The Proof**: Isolates I/O wait times in the **Redis/IO Impact** section.
*   **Real-World Impact**: Serialization efficiency is the "multiplier" for storage costs. 50% smaller objects in Redis mean **50% lower cloud infrastructure costs** and faster network transit.

---

## 🗄️ Database Switching: MS SQL Server

By default, the lab uses an in-memory **H2 Database** (via the `h2` profile). To test against **MS SQL Server**:

1.  **Configure**: Edit `src/main/resources/application-sqlserver.properties` with your SQL Server connection string and credentials.
2.  **Run with Profile**: Use the `sqlserver` Spring profile. Because of Windows PowerShell quoting rules, use the following exact command:
    ```powershell
    mvn spring-boot:run "-Dspring-boot.run.profiles=sqlserver"
    ```
    *Note: If you are using a standard CMD prompt, you can omit the quotes.*

---

## 🔬 How to Run a Rigorous Test

1.  **Prepare**: Choose **Test Object** and click **"Prepare Object"**.
2.  **Heat the JIT**: Run a **JVM Comparison** first to ensure classes are loaded and C2 compilation has kicked in.
3.  **Execute E2E**: Click **"⚡ Run End-to-End I/O Comparison"** to see side-by-side results for Memory, Redis, and JDBC.
4.  **Analyze**: 
    - Check the **Redis/IO Impact** for network savings.
    - Inspect the **Protocol Analysis** JSON for machine-readable evidence.
5.  **Export**: Click **"Generate Report"** for a printable summary.

---

## 🛠 Project Structure

- `ForyConfig.java`: Configures the `ThreadSafeFory` bean (v0.14.1+).
- `QuoteFactory.java`: Industrial domain model generator supporting circular graphs.
- `CompareApiController.java`: High-concurrency benchmark engine with JVM metric capture and metadata-driven regression.
- `compare.jsp`: Professional dashboard with Chart.js analytics and v5.0 UX polish.

---

## 🔴 Redis Session Backend (Optional)

The app supports **Redis** as a session store via Spring Session.

### Running with Redis Sessions

1. **Start Redis**:
   ```bash
   docker run --name redis -p 6379:6379 redis:7-alpine
   ```

2. **Run with Redis Profile**:
   ```powershell
   # Use h2 + redis-session
   mvn spring-boot:run "-Dspring-boot.run.profiles=h2,redis-session"
   ```

### Profile Combinations

| Profiles | Database | Session Store |
|----------|----------|---------------|
| `h2` | H2 (in-memory) | Servlet Container |
| `sqlserver` | SQL Server | JDBC Session |
| `h2,redis-session` | H2 | Redis |
| `sqlserver,redis-session` | SQL Server | Redis |

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
