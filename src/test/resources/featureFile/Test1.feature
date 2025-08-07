#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
# (Comments)
#Sample Feature Definition Template
 @MySchool
Feature: Framework Enhancement test
  

  @Smoke @Regression 
  Scenario: Add New Student
    Given  Login to the application with a valid credentials
    When user clicks on My Schools
    And tries to Add a New Student with the details
		| studentFirstName | studentLastName | studentDob   | studentPhno   | studentNationality |studentLevel                       | studentGrade  |studentClassRoom|studentTiming| studentTerms|parentFirstName|ParentLastName|relation|parentPhNo|parentEmailId|parentCountry|parentState |parentCity|
     | John             | Doe             | 15-12-2023   | 9876543210    | Indian               |Preschool Class (2 - 3 years)   |Preschool      |Preschool 1      |10am to 4pm   |Term 1      |Honey          |Siva          |Mother |7871536104|abi@gmail.com|India         |Tamilnadu  |Covai    |
		Then verify student should be successfully created
 
  