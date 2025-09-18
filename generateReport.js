const report = require('multiple-cucumber-html-reporter');
const sendEmail = require('./Utility/emailSender');
const fs = require('fs');
const path = require('path');

(async () => {
  try {
    const jsonDir = path.resolve('./reports/json');
   // const htmlReport = path.resolve('./reports/html/index.html');
    const htmlReport = path.resolve('./reports/html/index.html');
    const logsDir = path.resolve('./logs');
    const markerFile = path.join(logsDir, 'latestRun.txt');

    if (!fs.existsSync(jsonDir)) {
      throw new Error('❌ JSON report folder not found');
    }

    // Step 1: Get latest run timestamp
    let latestRunTime = new Date(0);
    if (fs.existsSync(markerFile)) {
      latestRunTime = new Date(fs.readFileSync(markerFile, 'utf-8'));
    }

  // Step 2: Read only recent log files and embed content as collapsible blocks
const embeddedLogs = fs.readdirSync(logsDir)
  .filter(file => file.endsWith('.log'))
  .map(file => {
    const filePath = path.join(logsDir, file);
    const stats = fs.statSync(filePath);
    return { file, mtime: stats.mtime, path: filePath };
  })
  .filter(fileObj => fileObj.mtime > latestRunTime)
  .map(({ file, path: filePath }) => {
    const logContent = fs.readFileSync(filePath, 'utf-8');
    return {
      label: file,
      value: `
        <details style="margin-bottom:10px;">
          <summary style="font-weight:bold;cursor:pointer;">${file}</summary>
          <pre style="max-height:300px;overflow:auto;background:#f9f9f9;padding:10px;border:1px solid #ccc;">
${logContent}
          </pre>
        </details>`
    };
  });


    // Step 3: Generate HTML report with embedded logs
    report.generate({
      jsonDir,
      reportPath: './reports/html',
      metadata: {
        browser: { name: 'chrome', version: 'latest' },
        device: 'Local test machine',
        platform: { name: 'windows', version: '10' }
      },
      customData: {
        title: 'Execution Logs',
        data: embeddedLogs.length > 0 ? embeddedLogs : [{ label: 'Logs', value: 'No recent logs found' }]
      }
    });

    if (!fs.existsSync(htmlReport)) {
      throw new Error('❌ HTML report not generated.');
    }

    console.log('✅ HTML report generated successfully');
    await sendEmail();
    console.log('✅ Email sent successfully');
  } catch (error) {
    console.error('❌ Error:', error.message);
  }
})();
