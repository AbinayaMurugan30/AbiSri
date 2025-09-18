

module.exports = {
  default: [
    
    '--require-module @babel/register',
    '--require step-definitions/*.js',
    '--require hooks.js',             // <-- add this
    '--format progress',
    '--format json:reports/report.json'
  ].join(' '),
};
