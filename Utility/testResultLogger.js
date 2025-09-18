// Utility/testResultLogger.js
const { InfluxDB, Point,WritePrecision } = require('@influxdata/influxdb-client');

class TestResultLogger {
  constructor() {
    this.url = process.env.INFLUX_URL || 'http://localhost:8086';
    this.token = process.env.INFLUX_TOKEN || '3ear3GbtK69VFRZet16iaJY1hVWwxpRkb7jS8N2ZO_4TRb41OOtzqFWjR6Wz8qP8UCHsAe0XTWQXlfGswSBKjQ==';
    this.org = process.env.INFLUX_ORG || 'Teknotrait';
    this.bucket = process.env.INFLUX_BUCKET || 'teknojavascript-1';

    this.client = new InfluxDB({ url: this.url, token: this.token });
    this.writeApi = this.client.getWriteApi(this.org, this.bucket, 'ms');

    // Store all test results
    this.testResults = [];
    this.runStartTime = 0;
  }

  logBuildInfo() {
  // Access environment variables using process.env
  let buildNumber = process.env.BUILD_NUMBER;
  let buildId = process.env.BUILD_ID;

  // Provide fallback values if environment variables are not set (e.g., in a local run)
  if (!buildNumber) {
    buildNumber = 'local-run';
  }

  if (!buildId) {
    // Generate a timestamp similar to the Java SimpleDateFormat
    // .slice(0, 19) removes the milliseconds and 'Z' timezone from the ISO string
    buildId = new Date().toISOString().slice(0, 19);
  }

  // Corrected: Using the imported 'Point' and 'WritePrecision' directly
  const buildPoint = new Point('build_info')
    .tag('job', 'WebAutomation') 
    .stringField('build_number', buildNumber) 
    .stringField('build_date', buildId) 
    .timestamp(new Date(), WritePrecision.MS);

  this.write(buildPoint);

  console.log(`🏗️ Build Info logged: Build #${buildNumber} at ${buildId}`);
}

  initializeRun() {
    this.testResults = [];
    this.runStartTime = Date.now();
    console.log(`🚀 Test run initialized at: ${new Date(this.runStartTime).toISOString()}`);
   // this.logBuildInfo();
  }

  normalizeStatus(status) {
    switch (status.toLowerCase()) { // Use toLowerCase for case-insensitive comparison
      case 'passed':
      case 'pass':
        return 'pass';
      case 'failed':
      case 'fail':
        return 'fail';
      case 'skipped':
      case 'skip':
        return 'skipped';
      default:
        return 'unknown';
    }
  }

  logTestResult(status, testName, duration = 0) {
    const endTime = Date.now();
    const startTime = endTime - duration;
    const normStatus = this.normalizeStatus(status);

    // Store the result
    this.testResults.push({ status: normStatus, name: testName, duration: duration });

    // Corrected: Using the imported 'Point' and 'WritePrecision' directly
    const testPoint = new Point('test_results')
      .tag('status', normStatus)
      .tag('test_name', testName || 'Unnamed Test')
      .floatField('duration_ms', duration)
      .floatField('start_time', startTime)
      .floatField('end_time', endTime)
      .floatField('duration_sec', duration / 1000.0)
      .timestamp(endTime);

    this.write(testPoint);
    console.log(`📊 Test logged: ${testName} - ${normStatus.toUpperCase()} (Duration: ${duration}ms)`);
  }

  logRunSummary() {
    if (this.runStartTime === 0) {
      console.error('❌ Run not initialized. Call initializeRun() first.');
      return;
    }

    const endTime = Date.now();
    const totalDuration = endTime - this.runStartTime;

    // Calculate counts from the stored results
    const totalPass = this.testResults.filter(r => r.status === 'pass').length;
    const totalFail = this.testResults.filter(r => r.status === 'fail').length;
    const totalSkipped = this.testResults.filter(r => r.status === 'skipped').length;
    const totalExecuted = totalPass + totalFail + totalSkipped;

    // Corrected: Using the imported 'Point' and 'WritePrecision' directly
    const summaryPoint = new Point('test_summary')
      .tag('type', 'run_summary')
      .intField('Total_executed', totalExecuted)
      .intField('Total_pass', totalPass)
      .intField('Total_fail', totalFail)
      .intField('Total_skipped', totalSkipped)
      .intField('run_start_time', this.runStartTime)
      .intField('run_end_time', endTime)
      .floatField('total_duration_ms', totalDuration)
      .floatField('total_duration_sec', totalDuration / 1000.0)
      .floatField('pass_rate', totalExecuted > 0 ? (totalPass * 100.0 / totalExecuted) : 0.0)
      .timestamp(endTime);

    this.write(summaryPoint);
    console.log(`📈 Run Summary - Total: ${totalExecuted} | Pass: ${totalPass} | Fail: ${totalFail} | Skip: ${totalSkipped} | Duration: ${(totalDuration / 1000.0)}s`);
  }

  write(point) {
    try {
      this.writeApi.writePoint(point);
      console.log('✅ Point sent to InfluxDB successfully.');
    } catch (err) {
      console.error('❌ Error writing to InfluxDB:', err.message);
    }
  }

  async close() {
    try {
      await this.writeApi.close();
      console.log('🔒 InfluxDB connection closed.');
    } catch (err) {
      console.error('❌ Error closing InfluxDB client:', err.message);
    }
  }
}

module.exports = new TestResultLogger();
