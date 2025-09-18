const nodemailer = require('nodemailer');
const fs = require('fs');
const path = require('path');
const zipReportFolder = require('../zipReport');
const zippedReportPath = zipReportFolder(); // e.g., ./reports/TestReport.txt

async function sendEmail() {
  const logsDir = path.resolve(__dirname, '../logs');
  const markerFile = path.resolve(logsDir, 'latestRun.txt');
  const attachments = [];

  // Read the timestamp from latest run
  let latestRunTime = new Date(0); // default to epoch
  if (fs.existsSync(markerFile)) {
    const timestampStr = fs.readFileSync(markerFile, 'utf-8');
    latestRunTime = new Date(timestampStr);
  }

  // Attach only logs newer than latestRunTime
  const logFiles = fs.readdirSync(logsDir).filter(file => file.endsWith('.log'));
  for (const file of logFiles) {
    const fullPath = path.join(logsDir, file);
    const stats = fs.statSync(fullPath);

    if (stats.mtime > latestRunTime) {
      attachments.push({
        filename: file,
        path: fullPath,
        contentType: 'text/plain'
      });
    }
  }

  // ✅ Attach renamed zipped report as .txt
  attachments.push({
    filename: 'TestReport.txt',
    path: zippedReportPath,
    contentType: 'text/plain' // or leave it undefined
  });

  let transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: 'abiteknotrait@gmail.com',
      pass: 'wdoldqmahxpbilho' // App password
    }
  });

  const mailOptions = {
    from: '"QA Automation Report" <abiteknotrait@gmail.com>',
    to: 'abinaya.j@teknotrait.com',
    subject: 'Automation Report with Logs',
    text: 'Attached are the latest run logs and HTML report.',
    attachments
  };

  await transporter.sendMail(mailOptions);
}

module.exports = sendEmail;
