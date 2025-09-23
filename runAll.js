// runAll.js
const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');
const influxLogger = require('./Utility/testResultLogger'); // ✅ adjust path if needed

(async () => {
  // Mark run start (for your logs/latestRun.txt)
  const markerFile = path.resolve('./logs/latestRun.txt');
  fs.writeFileSync(markerFile, new Date().toISOString());

  // Start run in Influx
  influxLogger.initializeRun();

  try {
    console.log('🔹 Running tests...');
    execSync(
      'npx cucumber-js --require-module @babel/register --require ./step-definitions/*.js --require ./step-definitions/hooks.js --format json:reports/report.json',
      { stdio: 'inherit' }
    );
  } catch (err) {
    console.error('❌ Test execution failed', err);
  } finally {
    // End run in Influx
    influxLogger.logRunSummary();
    await influxLogger.close();

    // Generate report ONCE
    console.log('🟢 generateReport.js started...');
    require(path.resolve(__dirname, './generateReport.js'));
  }
})();
