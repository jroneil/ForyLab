# Serialization Lab v2.0: Spring Session + Apache Fory

This project is a high-performance benchmark laboratory designed to compare **Java Native Serialization** with **Apache Fory (formerly Apache Fury)** in a Spring Session (JDBC) environment. 

Version 2.0 introduces **JIT Warm-up cycles**, **Serialization (Write) benchmarking**, and **Real-time Visual Analytics**.

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
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

## 🎮 Dashboard Configuration

The Laboratory UI allows you to tune the benchmark parameters to simulate production conditions:

| Setting | Purpose | Why it matters |
| :--- | :--- | :--- |
| **Payload Size** | KB of data | Simulates simple vs. deep nested object graphs (Insurance Quotes). |
| **Bench Type** | Read vs. Write | **Deserialize (Read)** tests session retrieval. **Serialize (Write)** tests session updates. |
| **Iterations** | Number of tests | Higher numbers provide more statistically significant averages. |
| **Warm-up Cycles**| JIT Preparation | Fory uses JIT optimization. Warm-ups allow the code to reach peak "hot" performance. |

---

## 📊 New in v2.0

- **Visual Latency Charts**: Real-time bar graphs built with Chart.js to visualize the performance gap.
- **Bi-Directional Testing**: You can now test both "Serialization" (Object to Bytes) and "Deserialization" (Bytes to Object).
- **JIT Awareness**: Explicit warm-up controls to showcase how Fory gains speed as the JVM optimizes the execution path.

---

## 🔬 How to Run a Rigorous Test

1.  **Prepare Environment**: Click **"Prepare Object"**. This generates the insurance quote and caches it in memory for the tests.
2.  **Toggle Mode**: Choose **"Serialize (Write)"** to see how fast Fory encodes data compared to Java.
3.  **Heat the JIT**: Set **Warm-up Cycles** to `1000`. This ensures you aren't measuring Fory while it is still "cold."
4.  **Execute**: Click **"🔥 Run Comparison"**.
5.  **Analyze**: 
    - The **Chart** shows a side-by-side comparison of average latency.
    - The **Recent Results** table tracks P95 (tail latency), which is critical for real-world application responsiveness.

---

## 🛠 Project Structure

- `ForyConfig.java`: Configures the `ThreadSafeFory` bean for the application.
- `QuoteCodec.java`: A wrapper around Fory to handle business object serialization.
- `CompareApiController.java`: The REST back-end driving the benchmarks.
- `compare.jsp`: The modern dashboard featuring Chart.js and Glassmorphism design.

## 📝 Troubleshooting

### "C:/Users/.../test" not found (H2 Console)
The H2 console defaults to a file-based URL. Ensure you change the **JDBC URL** to:
`jdbc:h2:mem:testdb`

### "ThreadLocalFory cannot be resolved"
Ensure you are using Fory version `0.14.1` or later in your `pom.xml`. Run `mvn clean install` to force a dependency refresh.

---
*Built with ❤️ for performance-obsessed developers.*
