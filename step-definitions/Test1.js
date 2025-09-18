// stepDefinitions/test1.steps.js
const { Given, When, Then } = require('@cucumber/cucumber');
const logger = require('../Utility/logger');
const { expect } = require('@playwright/test');
const LoginPage = require('../pages/LoginPage');
const HomePage = require('../pages/HomePage');
const StudentPage = require('../pages/StudentPage');
const AddNewStudent = require('../pages/AddNewStudent');

let lp, hp, sp, anp;
let studentData;

  

Given('Login to the application with a valid credentials', {timeout:80000},async function () {

  lp = new LoginPage(this.page);   
  hp = new HomePage(this.page);
  sp = new StudentPage(this.page);
  anp = new AddNewStudent(this.page);

  await lp.login('croissance.abhik@gmail.com', 'Abcd@1234');
  console.log('✅ Application Launched successfully with Valid Credentials');
  this.logger.info('✅ Application Launched successfully with Valid Credentials');
});

When('user clicks on My Schools', async function () {
  await hp.selectMySchool();
  console.log('✅ Selected My school successfully');
  this.logger.info('✅ Selected My school successfully');

  await hp.selectStudents();
  console.log('✅ Selected Students successfully');
  this.logger.info('✅ Selected Students successfully');
});

When('tries to Add a New Student with the details',{ timeout: 80000 }, async function (dataTable) {
  studentData = dataTable.rowsHash(); // Converts table into key-value pairs
  //  studentData = dataTable.hashes()[0];
  await sp.selectAddNewStudent();
  console.log('✅ Add New Student button selected successfully');
  this.logger.info('✅ Add New Student button selected successfully');

  await anp.setStudent(
    studentData.studentFirstName,
    studentData.studentLastName,
    studentData.studentDob,
    studentData.studentPhno,
    studentData.studentNationality,
    studentData.studentLevel,
    studentData.studentGrade,
    studentData.studentClassRoom,
    studentData.studentTiming,
    studentData.studentTerms,
    studentData.parentFirstName,
    studentData.ParentLastName,
    studentData.relation,
    studentData.parentPhNo,
    studentData.parentEmailId,
    studentData.parentCountry,
    studentData.parentState,
    studentData.parentCity
  );

  console.log('✅ Student Details are added successfully');
  this.logger.info('✅ Student Details are added successfully');
});

Then('verify student should be successfully created',{timeout:80000} ,async function () {
 // const data = dataTable.rowsHash(); // convert to object
  //  await anp.checkNewlyAddedStudentVerification(studentData);
  console.log('Verification part is not done');
    

});
