const { createLogger, format, transports } = require('winston');
const path = require('path');
const fs = require('fs');

const logsDir = path.join(__dirname, '../logs');

// Create logs folder if it doesn't exist
if (!fs.existsSync(logsDir)) {
  fs.mkdirSync(logsDir);
}

function initLogger(scenarioName) {
  const timestamp = new Date().toISOString().replace(/[:.]/g, '_');
  const safeScenario = scenarioName.replace(/[^a-zA-Z0-9]/g, '_');
  const logFileName = path.join(logsDir, `${safeScenario}_${timestamp}.log`);

  return createLogger({
    level: 'info',
    format: format.combine(
      format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
      format.printf(({ timestamp, level, message }) => `${timestamp} [${level.toUpperCase()}]: ${message}`)
    ),
    transports: [
      new transports.File({ filename: logFileName }),
      new transports.Console()
    ]
  });
}

module.exports = { initLogger };
