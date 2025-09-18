// globalTeardown.js
const sendEmail = require('./Utility/emailSender');
module.exports = async () => {
console.log('[INFO] Global teardown started: Sending email...');
  await sendEmail(); // attach report + logs
  console.log('[INFO] Report email sent successfully');
};
