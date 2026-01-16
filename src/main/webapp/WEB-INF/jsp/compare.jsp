<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Serialization Lab v3.2 | Rigorous Performance Proof</title>
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
                padding: 1rem;
            }

            .container {
                max-width: 1400px;
                margin: 0 auto;
            }

            header {
                margin-bottom: 1.5rem;
                text-align: center;
            }

            h1 {
                font-size: 2.2rem;
                font-weight: 600;
                background: linear-gradient(to right, #818cf8, #c084fc);
                -webkit-background-clip: text;
                background-clip: text;
                -webkit-text-fill-color: transparent;
                margin-bottom: 0.3rem;
            }

            .subtitle {
                color: var(--text-muted);
                font-size: 1rem;
            }

            .dashboard-grid {
                display: grid;
                grid-template-columns: 350px 1fr 1fr;
                gap: 1.5rem;
            }

            @media (max-width: 1100px) {
                .dashboard-grid {
                    grid-template-columns: 1fr;
                }
            }

            .card {
                background: var(--card-bg);
                border-radius: 1rem;
                padding: 1.5rem;
                box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3);
                border: 1px solid var(--border);
                margin-bottom: 1.5rem;
            }

            h2 {
                font-size: 1.2rem;
                margin-bottom: 1rem;
                color: var(--text);
                display: flex;
                align-items: center;
                gap: 0.5rem;
            }

            .form-group {
                display: flex;
                flex-direction: column;
                gap: 0.4rem;
                margin-bottom: 1rem;
            }

            label {
                font-weight: 500;
                color: var(--text-muted);
                font-size: 0.85rem;
            }

            input,
            select {
                background: var(--bg);
                border: 1px solid var(--border);
                border-radius: 0.5rem;
                padding: 0.6rem;
                color: var(--text);
                font-size: 0.95rem;
                transition: border-color 0.2s;
            }

            input:focus {
                outline: none;
                border-color: var(--primary);
            }

            .checkbox-group {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                margin-bottom: 1rem;
            }

            .checkbox-group input {
                width: 1.2rem;
                height: 1.2rem;
            }

            .button-group {
                display: flex;
                flex-direction: column;
                gap: 0.8rem;
                margin-top: 1rem;
            }

            button {
                cursor: pointer;
                padding: 0.7rem 1.2rem;
                border-radius: 0.5rem;
                font-weight: 600;
                font-size: 0.9rem;
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

            .btn-outline {
                background: transparent;
                border: 1px solid var(--border);
                color: var(--text);
            }

            .btn-outline:hover {
                background: var(--border);
            }

            .btn-success {
                background: var(--success);
                color: white;
            }

            .results-table {
                width: 100%;
                border-collapse: collapse;
            }

            .results-table th,
            .results-table td {
                padding: 0.8rem;
                text-align: left;
                border-bottom: 1px solid var(--border);
                font-size: 0.9rem;
            }

            .results-table th {
                color: var(--text-muted);
                font-weight: 600;
                text-transform: uppercase;
                font-size: 0.75rem;
            }

            .mode-badge {
                display: inline-block;
                padding: 0.2rem 0.6rem;
                border-radius: 9999px;
                font-size: 0.75rem;
                font-weight: 700;
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
                font-size: 0.8rem;
                max-height: 150px;
                overflow-y: auto;
                color: #10b981;
            }

            .loader {
                width: 16px;
                height: 16px;
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

            .chart-container {
                position: relative;
                height: 220px;
                width: 100%;
                margin-top: 1rem;
            }

            .analytics-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 1rem;
                margin-top: 1rem;
            }
        </style>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>

    <body>
        <div class="container">
            <header>
                <h1>Serialization Lab v3.2</h1>
                <p class="subtitle">Rugorous Proof Benchmarking | JIT | Throughput | Payload Size</p>
            </header>

            <div class="dashboard-grid">
                <!-- Sidebar Controls -->
                <div class="sidebar">
                    <section class="card">
                        <h2>Configuration</h2>
                        <div class="form-group">
                            <label for="sizeKb">Object Size (KB)</label>
                            <input type="number" id="sizeKb" value="500" min="1" max="5000">
                        </div>
                        <div class="checkbox-group">
                            <input type="checkbox" id="circularRefs">
                            <label for="circularRefs">Circular References</label>
                        </div>
                        <div class="form-group">
                            <label for="benchType">Operation</label>
                            <select id="benchType">
                                <option value="deserialize">Deserialize (Read)</option>
                                <option value="serialize">Serialize (Write)</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="iterations">Iterations</label>
                            <input type="number" id="iterations" value="500" min="1" max="10000">
                        </div>
                        <div class="form-group">
                            <label for="warmup">Warm-up Cycles</label>
                            <input type="number" id="warmup" value="1000" min="0" max="100000">
                        </div>
                        <div class="button-group">
                            <button id="btnStore" class="btn-primary">
                                <span id="storeLoader" class="loader"></span>
                                Prepare Object
                            </button>
                            <button id="btnRunBoth" class="btn-outline" disabled>Run Comparison</button>
                            <button id="btnReRun" class="btn-outline" onclick="reRun()" disabled>🔁 Re-run Same
                                Config</button>
                            <button id="btnExport" class="btn-outline" onclick="exportToCSV()" disabled>Export
                                CSV</button>
                        </div>
                    </section>

                    <div id="statusSection">
                        <div class="card">
                            <h2>Payload Size</h2>
                            <div class="chart-container" style="height: 150px;">
                                <canvas id="sizeChart"></canvas>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Main Charts Column -->
                <div class="main-charts">
                    <section class="card">
                        <h2>Latency Comparison (Avg ms)</h2>
                        <div class="chart-container">
                            <canvas id="performanceChart"></canvas>
                        </div>
                    </section>

                    <section class="card">
                        <div class="status-header"
                            style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem;">
                            <h2>Latency Distribution Histogram</h2>
                            <div class="checkbox-group" style="margin-bottom:0">
                                <input type="checkbox" id="logScale" onchange="toggleLogScale()">
                                <label for="logScale" style="font-size:0.75rem">Log scale</label>
                            </div>
                        </div>
                        <div class="chart-container">
                            <canvas id="histogramChart"></canvas>
                        </div>
                    </section>
                </div>

                <!-- Tables Column -->
                <div class="data-column">
                    <section class="card">
                        <div class="status-header"
                            style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem;">
                            <h2>Results</h2>
                            <button class="btn-outline" style="padding: 0.3rem 0.6rem; font-size: 0.75rem;"
                                onclick="clearResults()">Clear</button>
                        </div>
                        <div style="max-height: 400px; overflow-y: auto;">
                            <table class="results-table">
                                <thead>
                                    <tr>
                                        <th>Mode</th>
                                        <th>Type</th>
                                        <th>Size</th>
                                        <th>Circ?</th>
                                        <th>Iter</th>
                                        <th>Warm</th>
                                        <th>Avg (ms)</th>
                                        <th>P50</th>
                                        <th>P95</th>
                                        <th>P99</th>
                                        <th>StdDev</th>
                                        <th>Throughput</th>
                                    </tr>
                                </thead>
                                <tbody id="resultsBody"></tbody>
                            </table>
                        </div>
                    </section>

                    <section class="card">
                        <div class="status-header"
                            style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem;">
                            <h2>Protocol Analysis</h2>
                            <div style="display: flex; gap: 0.5rem;">
                                <button class="btn-outline" style="padding: 0.3rem 0.6rem; font-size: 0.75rem;"
                                    onclick="copyJson()">Copy</button>
                                <button class="btn-outline" style="padding: 0.3rem 0.6rem; font-size: 0.75rem;"
                                    onclick="downloadJson()">Download JSON</button>
                            </div>
                        </div>
                        <div id="jsonOutput" class="json-display">System ready...</div>
                    </section>
                </div>
            </div>
        </div>

        <script>
            const btnStore = document.getElementById('btnStore');
            const btnRunBoth = document.getElementById('btnRunBoth');
            const btnReRun = document.getElementById('btnReRun');
            const btnExport = document.getElementById('btnExport');
            const jsonOutput = document.getElementById('jsonOutput');
            const resultsBody = document.getElementById('resultsBody');
            const storeLoader = document.getElementById('storeLoader');

            let perfChart, sizeChart, histChart;
            let lastBenchmarks = [];

            function initCharts() {
                const chartOptions = {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: { beginAtZero: true, grid: { color: '#334155' } },
                        x: { grid: { display: false } }
                    },
                    plugins: { legend: { display: false } }
                };

                perfChart = new Chart(document.getElementById('performanceChart'), {
                    type: 'bar',
                    data: {
                        labels: ['Java Native', 'Apache Fory'],
                        datasets: [{
                            data: [0, 0],
                            backgroundColor: ['rgba(245, 158, 11, 0.6)', 'rgba(6, 182, 212, 0.6)'],
                            borderColor: ['#f59e0b', '#06b6d4'],
                            borderWidth: 1
                        }]
                    },
                    options: chartOptions
                });

                sizeChart = new Chart(document.getElementById('sizeChart'), {
                    type: 'bar',
                    data: {
                        labels: ['Java', 'Fory'],
                        datasets: [{
                            data: [0, 0],
                            backgroundColor: ['rgba(245, 158, 11, 0.4)', 'rgba(6, 182, 212, 0.4)'],
                            borderColor: ['#f59e0b', '#06b6d4'],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        indexAxis: 'y',
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            x: { beginAtZero: true, grid: { color: '#334155' } },
                            y: { grid: { display: false } }
                        },
                        plugins: { legend: { display: false } }
                    }
                });

                histChart = new Chart(document.getElementById('histogramChart'), {
                    type: 'line',
                    data: {
                        labels: [],
                        datasets: [
                            { label: 'Java', data: [], borderColor: '#f59e0b', tension: 0.4 },
                            { label: 'Fory', data: [], borderColor: '#06b6d4', tension: 0.4 }
                        ]
                    },
                    options: {
                        ...chartOptions,
                        plugins: { legend: { display: true, labels: { color: '#fff' } } },
                        scales: { y: { beginAtZero: true }, x: { display: false } }
                    }
                });
            }

            function formatBytes(bytes) {
                if (bytes === 0) return '0 B';
                const k = 1024;
                const sizes = ['B', 'KB', 'MB'];
                const i = Math.floor(Math.log(bytes) / Math.log(k));
                return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
            }

            btnStore.addEventListener('click', async () => {
                const sizeKb = parseInt(document.getElementById('sizeKb').value, 10);
                const circular = document.getElementById('circularRefs').checked;
                storeLoader.style.display = 'inline-block';
                btnStore.disabled = true;

                try {
                    const res = await fetch(`/api/compare/store?sizeKb=\${sizeKb}&circular=\${circular}`, { method: 'POST' });
                    const data = await res.json();

                    document.getElementById('statusSection').style.display = 'block';
                    sizeChart.data.datasets[0].data = [data.javaBytesSize, data.foryBytesSize];
                    sizeChart.update();

                    jsonOutput.innerText = JSON.stringify({
                        status: "Object Prepared",
                        payloads: {
                            java: formatBytes(data.javaBytesSize),
                            fory: formatBytes(data.foryBytesSize)
                        },
                        ratio: `\${(data.javaBytesSize / data.foryBytesSize).toFixed(1)}x smaller`
                    }, null, 2);

                    // ✅ LOCK circular setting after object creation
                    document.getElementById('circularRefs').disabled = true;

                    btnRunBoth.disabled = false;
                    btnExport.disabled = false;
                } catch (e) {
                    jsonOutput.innerText = "Error: " + e.message;
                } finally {
                    storeLoader.style.display = 'none';
                    btnStore.disabled = false;
                }
            });


            async function runBench(mode) {
                const iterations = parseInt(document.getElementById('iterations').value, 10);
                const warmup = parseInt(document.getElementById('warmup').value, 10);
                const type = document.getElementById('benchType').value;

                const circular = document.getElementById('circularRefs').checked;

                const res = await fetch('/api/compare/bench', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        iterations,
                        warmup,
                        circular,
                        mode, type
                    })
                });
                const data = await res.json();

                const row = document.createElement('tr');
                const badgeClass = data.mode === 'java' ? 'badge-java' : 'badge-fory';
                row.innerHTML = `
                    <td><span class="mode-badge \${badgeClass}">\${data.mode}</span></td>
                    <td>\${data.type === 'serialize' ? 'Write' : 'Read'}</td>
                    <td>\${formatBytes(data.payloadBytes)}</td>
                    <td>\${data.circular ? 'Yes' : 'No'}</td>
                    <td>\${data.iterations}</td>
                    <td>\${data.warmup}</td>
                    <td><strong>\${data.avgMs.toFixed(3)}</strong></td>
                    <td>\${(data.p50Ms ?? 0).toFixed(3)}</td>
                    <td>\${(data.p95Ms ?? 0).toFixed(3)}</td>
                    <td>\${(data.p99Ms ?? 0).toFixed(3)}</td>
                    <td>\${(data.stddevMs ?? 0).toFixed(3)}</td>
                    <td>\${Math.round(data.throughput).toLocaleString()}</td>
                    `;

                resultsBody.prepend(row);
                return data;
            }

            btnRunBoth.addEventListener('click', async () => {
                btnRunBoth.disabled = true;
                btnRunBoth.innerText = '🏃 Testing...';

                const java = await runBench('java');
                const fory = await runBench('fory');

                perfChart.data.datasets[0].data = [java.avgMs, fory.avgMs];
                perfChart.update();

                // Histogram Update (use first 50 samples for clarity)
                const count = Math.min(java.samples.length, 50);
                histChart.data.labels = Array.from({ length: count }, (_, i) => i);
                histChart.data.datasets[0].data = java.samples.slice(0, count);
                histChart.data.datasets[1].data = fory.samples.slice(0, count);
                histChart.update();

                const sizeKb = parseInt(document.getElementById('sizeKb').value, 10);
                const circular = document.getElementById('circularRefs').checked;
                const iterations = parseInt(document.getElementById('iterations').value, 10);
                const warmup = parseInt(document.getElementById('warmup').value, 10);
                const op = document.getElementById('benchType').value;

                function pickStats(b) {
                    return {
                        avgMs: b.avgMs,
                        p50Ms: b.p50Ms,
                        p95Ms: b.p95Ms,
                        p99Ms: b.p99Ms,
                        stddevMs: b.stddevMs,
                        throughputOpsSec: Math.round(b.throughput),
                        payloadBytes: b.payloadBytes,
                        jvmImpact: {
                            memDelta: formatBytes(b.memoryDeltaBytes),
                            gcCollections: b.gcCollectionsDelta,
                            gcTime: b.gcTimeMsDelta + "ms"
                        }
                    };
                }

                jsonOutput.innerText = JSON.stringify({
                    outcome: "Comparison Finished",
                    config: { sizeKb, circular, iterations, warmup, operation: op },
                    headline: {
                        avgSpeedup: `\${(java.avgMs / fory.avgMs).toFixed(2)}x`,
                        p95Speedup: `\${((java.p95Ms ?? java.avgMs) / (fory.p95Ms ?? fory.avgMs)).toFixed(2)}x`,
                        payloadRatio: `\${(java.payloadBytes / fory.payloadBytes).toFixed(2)}x`,
                        throughputDeltaOpsSec: Math.round(fory.throughput - java.throughput)
                    },
                    java: pickStats(java),
                    fory: pickStats(fory),
                    raw: { java, fory }
                }, null, 2);


                lastBenchmarks.push(java, fory);
                btnRunBoth.disabled = false;
                btnRunBoth.innerText = 'Run Comparison';
                btnReRun.disabled = false;
            });

            async function reRun() {
                btnRunBoth.click();
            }

            function toggleLogScale() {
                const isLog = document.getElementById('logScale').checked;
                histChart.options.scales.y.type = isLog ? 'logarithmic' : 'linear';
                histChart.update();
            }

            function exportToCSV() {
                if (lastBenchmarks.length === 0) return;

                let csv =
                    "Mode,Operation,Object Size (Bytes),Circular Refs,Iterations,Warmup Cycles," +
                    "Avg Latency (ms),P50 (ms),P95 (ms),P99 (ms),StdDev (ms)," +
                    "Throughput (ops/s),Mem Delta (Bytes),GC Time (ms)\n";

                lastBenchmarks.forEach(b => {
                    csv += `\${b.mode},\${b.type},\${b.payloadBytes},\${b.circular},\${b.iterations},\${b.warmup},` +
                        `\${b.avgMs},\${b.p50Ms ?? ""},\${b.p95Ms ?? ""},\${b.p99Ms ?? ""},\${b.stddevMs ?? ""},` +
                        `\${Math.round(b.throughput)},\${b.memoryDeltaBytes},\${b.gcTimeMsDelta}\n`;
                });

                const blob = new Blob([csv], { type: 'text/csv' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.setAttribute('hidden', '');
                a.setAttribute('href', url);
                a.setAttribute('download', 'serialization_results.csv');
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
            }


            function copyJson() {
                const content = jsonOutput.innerText;
                if (content === 'System ready...' || content === 'Initialize environment...') return;

                navigator.clipboard.writeText(content).then(() => {
                    const btn = event.target;
                    const originalText = btn.innerText;
                    btn.innerText = '✅ Copied!';
                    setTimeout(() => btn.innerText = originalText, 2000);
                });
            }

            function downloadJson() {
                const content = jsonOutput.innerText;
                if (content === 'System ready...' || content === 'Initialize environment...') return;

                const blob = new Blob([content], { type: 'application/json' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.setAttribute('hidden', '');
                a.setAttribute('href', url);
                a.setAttribute('download', 'protocol_analysis.json');
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
            }

            function clearResults() {
                resultsBody.innerHTML = '';
                lastBenchmarks = [];
                btnReRun.disabled = true;

                document.getElementById('circularRefs').disabled = false;
                // optional (recommended):
                // document.getElementById('sizeKb').disabled = false;

                if (perfChart) perfChart.data.datasets[0].data = [0, 0];
                if (sizeChart) sizeChart.data.datasets[0].data = [0, 0];
                if (histChart) {
                    histChart.data.datasets[0].data = [];
                    histChart.data.datasets[1].data = [];
                }
                perfChart.update();
                sizeChart.update();
                histChart.update();
            }


            initCharts();
        </script>
    </body>

    </html>