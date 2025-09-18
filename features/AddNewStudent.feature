Feature: Verify Add New Student

Scenario: Verify Add New Student
    Given  Login to the application with a valid credentials
    When user clicks on My Schools
    And tries to Add a New Student with the details
  | studentFirstName | Nish             |
  | studentLastName  | Miun              |
  | studentDob       | 12-03-2023       |
  | studentPhno      | 9876543220       |
  | studentNationality | India         |
  | studentLevel     | Preschool Class (2 - 3 years) |
  | studentGrade     | Preschool        |
  | studentClassRoom | Preschool 1      |
  | studentTiming    | 10am to 4pm      |
  | studentTerms     | Term 1           |
  | parentFirstName  | Nidhu            |
  | ParentLastName   | Nidhesh          |
  | relation         | Mother           |
  | parentPhNo       | 9234567890       |
  | parentEmailId    | nidhi@gmail.com    |
  | parentCountry    | India            |
  | parentState      | Tamilnadu        |
  | parentCity       | Chennai           |

		Then verify student should be successfully created