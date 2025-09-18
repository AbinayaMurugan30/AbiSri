@smoke
Feature: Verify Active Inactive
Scenario:  Verify Active / Inactive Filter features on Student Page
     Given  Login to the application with a valid credentials
      When user clicks on My Schools and select the Inactive filter
		Then Verify all the records displayed should be inactive
		When Change the status to Active from the filter
		Then Verify all the records displayed should be active