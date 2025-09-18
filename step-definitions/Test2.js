const { When, Then } = require('@cucumber/cucumber');
const { expect } = require('@playwright/test');
const logger = require('../Utility/logger');

// Import Page Objects
const  LoginPage  = require('../pages/LoginPage');
const  HomePage  = require('../pages/HomePage');
const  StudentPage  = require('../pages/StudentPage');
const  AddNewStudent  = require('../pages/AddNewStudent');

let page;
let lp, hp, sp, anp;

When('user clicks on My Schools and select the Inactive filter',{timeout:60000} ,async function () {
  page = this.page;
  lp = new LoginPage(this.page);
  hp = new HomePage(this.page);
  sp = new StudentPage(this.page);
  anp = new AddNewStudent(this.page);

  await hp.selectMySchool();
  this.logger.info('Selected My school successfully');

  await hp.selectStudents();
  this.logger.info('Selected My student successfully');

  await hp.setStatus();
  this.logger.info('Status is clicked');
});

Then('Verify all the records displayed should be inactive', async function () {
  const inactiveButton = await hp.inActiveStatus();
  expect(inactiveButton).toBe('InActive');
  this.logger.info('Inactive - Verified successfully');
});

When('Change the status to Active from the filter', async function () {
  await hp.setStatus();
  this.logger.info('Status is clicked');
});

Then('Verify all the records displayed should be active', async function () {
  const activeButton = await hp.activeStatus();
  expect(activeButton).toBe('Active');
  initLogger.info('Active - Verified successfully');
});
