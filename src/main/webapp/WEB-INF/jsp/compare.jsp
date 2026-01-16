<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Session Serialization Comparison</title>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #6366f1;
            --primary-hover: #4f46e5;
            --bg: #0f172a;
            --card-bg: #1e293b;
            --text: #f8fafc;
            --text-muted: #94a3b8;
            --success: #22c55e;
            --java-color: #f59e0b;
            --fory-color: #06b6d4;
            --border: #334155;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Outfit', sans-serif;
            background-color: var(--bg);
            color: var(--text);
            line-height: 1.6;
            padding: 2rem;
        }

        .container {
            max-width: 1000px;
            margin: 0 auto;
        }

        header {
            margin-bottom: 2rem;
            text-align: center;
        }

        h1 {
            font-size: 2.5rem;
            font-weight: 600;
            background: linear-gradient(to right, #818cf8, #c084fc);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 0.5rem;
        }

        .subtitle {
            color: var(--text-muted);
            font-size: 1.1rem;
        }

        .card {
            background: var(--card-bg);
            border-radius: 1rem;
            padding: 2rem;
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3);
            border: 1px solid var(--border);
            margin-bottom: 2rem;
        }

        .controls {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
            align-items: end;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        label {
            font-weight: 500;
            color: var(--text-muted);
            font-size: 0.9rem;
        }

        input {
            background: var(--bg);
            border: 1px solid var(--border);
            border-radius: 0.5rem;
            padding: 0.75rem;
            color: var(--text);
            font-size: 1rem;
            transition: border-color 0.2s;
        }

        input:focus {
            outline: none;
            border-color: var(--primary);
        }

        .button-group {
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
            margin-top: 1.5rem;
        }

        button {
            cursor: pointer;
            padding: 0.75rem 1.5rem;
            border-radius: 0.5rem;
            font-weight: 600;
            font-size: 0.95rem;
            transition: all 0.2s;
            border: none;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
        }

        .btn-primary {
            background: var(--primary);
            color: white;
        }

        .btn-primary:hover {
            background: var(--primary-hover);
            transform: translateY(-1px);
        }

        .btn-java {
            background: var(--java-color);
            color: white;
        }

        .btn-java:hover {
            opacity: 0.9;
            transform: translateY(-1px);
        }

        .btn-fory {
            background: var(--fory-color);
            color: white;
        }

        .btn-fory:hover {
            opacity: 0.9;
            transform: translateY(-1px);
        }

        .btn-outline {
            background: transparent;
            border: 1px solid var(--border);
            color: var(--text);
        }

        .btn-outline:hover {
            background: var(--border);
        }

        .results-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
        }

        .results-table th,
        .results-table td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid var(--border);
        }

        .results-table th {
            color: var(--text-muted);
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.8rem;
            letter-spacing: 0.05em;
        }

        .mode-badge {
            display: inline-block;
            padding: 0.25rem 0.75rem;
            border-radius: 9999px;
            font-size: 0.8rem;
            font-weight: 600;
            text-transform: uppercase;
        }

        .badge-java {
            background: rgba(245, 158, 11, 0.2);
            color: var(--java-color);
        }

        .badge-fory {
            background: rgba(6, 182, 212, 0.2);
            color: var(--fory-color);
        }

        .json-display {
            background: #000;
            border-radius: 0.5rem;
            padding: 1rem;
            font-family: 'Consolas', 'Monaco', monospace;
            font-size: 0.85rem;
            max-height: 300px;
            overflow-y: auto;
            color: #10b981;
            margin-top: 1rem;
        }

        .status-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }

        .loader {
            width: 20px;
            height: 20px;
            border: 2px solid rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            border-top-color: white;
            animation: spin 0.8s linear infinite;
            display: none;
        }

        @keyframes spin {
            to {
                transform: rotate(360deg);
            }
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 1rem;
            margin-bottom: 1rem;
        }

        .stat-card {
            background: rgba(255, 255, 255, 0.03);
            padding: 1rem;
            border-radius: 0.5rem;
            border: 1px solid var(--border);
        }

        .stat-value {
            font-size: 1.5rem;
            font-weight: 600;
            display: block;
        }

        .stat-label {
            color: var(--text-muted);
            font-size: 0.8rem;
        }
    </style>
</head>

<body>
    <div class="container">
        <header>
            <h1>Serialization Lab</h1>
            <p class="subtitle">Benchmarking Java Native vs Apache Fury (Fory) in Spring Session</p>
        </header>

        <section class="card">
            <div class="controls">
                <div class="form-group">
                    <label for="sizeKb">Payload Size (KB)</label>
                    <input type="number" id="sizeKb" value="100" min="1" max="5000">
                </div>
                <div class="form-group">
                    <label for="iterations">Iterations</label>
                    <input type="number" id="iterations" value="200" min="1" max="10000">
                </div>
            </div>
            <div class="button-group">
                <button id="btnStore" class="btn-primary">
                    <span id="storeLoader" class="loader"></span>
                    Store in Session
                </button>
                <button id="btnBenchJava" class="btn-java" disabled>Bench Java</button>
                <button id="btnBenchFory" class="btn-fory" disabled>Bench Fory</button>
                <button id="btnRunBoth" class="btn-outline" disabled>Run Both</button>
            </div>
        </section>

        <div id="statusSection" style="display: none;">
            <div class="stats-grid">
                <div class="stat-card">
                    <span class="stat-label">Java Payload</span>
                    <span id="javaSize" class="stat-value">0 B</span>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Fory Payload</span>
                    <span id="forySize" class="stat-value">0 B</span>
                </div>
            </div>
        </div>

        <section class="card">
            <div class="status-header">
                <h2>Benchmark Results</h2>
                <button class="btn-outline" onclick="clearResults()">Clear</button>
            </div>
            <table class="results-table">
                <thead>
                    <tr>
                        <th>Mode</th>
                        <th>Avg (ms)</th>
                        <th>P95 (ms)</th>
                        <th>Min/Max</th>
                        <th>Payload</th>
                    </tr>
                </thead>
                <tbody id="resultsBody">
                    <!-- Results will be injected here -->
                </tbody>
            </table>
        </section>

        <section class="card">
            <h2>Raw JSON Responses</h2>
            <div id="jsonOutput" class="json-display">System ready. Initialize by storing a quote...</div>
        </section>
    </div>

    <script>
        const btnStore = document.getElementById('btnStore');
        const btnBenchJava = document.getElementById('btnBenchJava');
        const btnBenchFory = document.getElementById('btnBenchFory');
        const btnRunBoth = document.getElementById('btnRunBoth');
        const jsonOutput = document.getElementById('jsonOutput');
        const resultsBody = document.getElementById('resultsBody');
        const storeLoader = document.getElementById('storeLoader');

        function updateJson(obj) {
            jsonOutput.innerText = JSON.stringify(obj, null, 2);
        }

        function formatBytes(bytes) {
            if (bytes === 0) return '0 B';
            const k = 1024;
            const sizes = ['B', 'KB', 'MB'];
            const i = Math.floor(Math.log(bytes) / Math.log(k));
            return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
        }

        btnStore.addEventListener('click', async () => {
            const sizeKb = document.getElementById('sizeKb').value;
            storeLoader.style.display = 'inline-block';
            btnStore.disabled = true;

            try {
                const res = await fetch(`/api/compare/store?sizeKb=${sizeKb}`, { method: 'POST' });
                const data = await res.json();
                updateJson(data);

                document.getElementById('statusSection').style.display = 'block';
                document.getElementById('javaSize').innerText = formatBytes(data.javaBytesSize);
                document.getElementById('forySize').innerText = formatBytes(data.foryBytesSize);

                btnBenchJava.disabled = false;
                btnBenchFory.disabled = false;
                btnRunBoth.disabled = false;
            } catch (e) {
                updateJson({ error: e.message });
            } finally {
                storeLoader.style.display = 'none';
                btnStore.disabled = false;
            }
        });

        async function runBench(mode) {
            const iterations = document.getElementById('iterations').value;
            const res = await fetch('/api/compare/bench', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ iterations: parseInt(iterations), mode })
            });
            const data = await res.json();
            addResultRow(data);
            return data;
        }

        function addResultRow(data) {
            const row = document.createElement('tr');
            const badgeClass = data.mode === 'java' ? 'badge-java' : 'badge-fory';
            row.innerHTML = `
                <td><span class="mode-badge \${badgeClass}">\${data.mode}</span></td>
                <td><strong>\${data.avgMs.toFixed(3)}</strong></td>
                <td>\${data.p95Ms.toFixed(3)}</td>
                <td>\${data.minMs.toFixed(3)} / \${data.maxMs.toFixed(3)}</td>
                <td>\${formatBytes(data.payloadBytes)}</td>
            `;
            resultsBody.prepend(row);
            updateJson(data);
        }

        btnBenchJava.addEventListener('click', () => runBench('java'));
        btnBenchFory.addEventListener('click', () => runBench('fory'));
        btnRunBoth.addEventListener('click', async () => {
            btnRunBoth.disabled = true;
            await runBench('java');
            await runBench('fory');
            btnRunBoth.disabled = false;
        });

        function clearResults() {
            resultsBody.innerHTML = '';
        }
    </script>
</body>

</html>