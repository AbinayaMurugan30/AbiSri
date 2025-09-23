const { setWorldConstructor, Before, After, BeforeAll, AfterAll, setDefaultTimeout } = require('@cucumber/cucumber');
const playwright = require('playwright');
const { initLogger } = require('../Utility/logger');
const influxLogger = require('../Utility/testResultLogger'); // singleton

setDefaultTimeout(60 * 1000); // 60s timeout

class CustomWorld {
  constructor() {
    this.browser = null;
    this.context = null;
    this.page = null;
    this.logger = null;
    this.startTime = null;
  }

  async launchBrowser() {
    const browserName = process.env.BROWSER || 'chromium';
    const headless = process.env.HEADLESS !== 'false';
    this.browser = await playwright[browserName].launch({ headless });
    this.context = await this.browser.newContext();
    this.page = await this.context.newPage();
  }

  async closeBrowser() {
    if (this.browser) {
      await this.browser.close();
      console.log('Browser closed');
    }
  }
}
setWorldConstructor(CustomWorld);

// 🔹 Run-level setup
BeforeAll(function () {
  influxLogger.initializeRun();
});

// 🔹 Per-scenario setup
Before(async function (scenario) {
  this.logger = initLogger(scenario.pickle.name);
  this.logger.info(`Logger initialized for: ${scenario.pickle.name}`);

  this.startTime = Date.now();

  await this.launchBrowser();
  this.logger.info(`Browser launched`);

  const baseURL = process.env.BASE_URL || 'https://labs.classadia.com';
  await this.page.goto(baseURL);
  this.logger.info(`Navigated to: ${baseURL}`);
});

// 🔹 Per-scenario teardown
After(async function (scenario) {
  const endTime = Date.now();
  const duration = endTime - this.startTime;

  influxLogger.logTestResult(
    scenario.result?.status || 'unknown',
    scenario.pickle.name,
    duration
  );

  await this.closeBrowser();
});

// 🔹 Run-level teardown
AfterAll(async function () {
  influxLogger.logRunSummary();
  await influxLogger.close();
  
});
