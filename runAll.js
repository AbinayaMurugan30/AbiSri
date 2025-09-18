// runAll.js
const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

(async () => {
  const markerFile = path.resolve('./logs/latestRun.txt');
  fs.writeFileSync(markerFile, new Date().toISOString());

  try {
    console.log('🔹 Running tests...');
    execSync(
      'npx cucumber-js --require-module @babel/register --require ./step-definitions/*.js --require ./step-definitions/hooks.js --format json:reports/report.json',
      { stdio: 'inherit' }
    );
  } catch (err) {
    console.warn('⚠️ Tests failed — continuing to generate report');
  }

  // ✅ Reports are generated regardless of pass/fail
  console.log('🟢 generateReport.js started...');
  require(path.resolve(__dirname, './generateReport.js'));
})();
