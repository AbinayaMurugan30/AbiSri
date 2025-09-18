const { expect } = require('@playwright/test');

class AddNewStudent {
    constructor(page) {
        this.page = page;
        
        // Element locators
        this.firstName = page.locator("//div[@class='input-filed']//input[@class='form-control custom-input ']");
        this.lastName = page.locator("//div[@class='row text-start mt-3']//div[2]//div[1]//input[1]");
        this.dob = page.locator("//img[@class='svg-icon calender-icon']");
        this.monthSelectionFromCalendar = page.locator("//select[@class='react-datepicker__month-select']");
        this.yearSelectionFromCalendar = page.locator("//select[@class='react-datepicker__year-select']");
        this.phno = page.locator("//input[@value='+91']");
        this.nationality = page.locator("//select[@class='select-dropdown form-select custom-input']");
        this.continueButton = page.locator("//button[@class='btn primary-btn ']");
        this.selectLevel = page.locator("//div[@class='input-filed']//select[contains(@class,'select-dropdown form-select custom-input')]");
        this.selectGrade = page.locator("//div[contains(@class,'col-lg-6')]//div[2]//div[1]//select[1]");
        this.classRoom = page.locator("//div[3]//div[1]//select[1]");
        this.timing = page.locator("//div[4]//div[1]//select[1]");
        this.weekMonday = page.locator("//span[normalize-space()='M']");
        this.terms = page.locator("//div[6]//div[1]//select[1]");
        this.parentsFName = page.locator("//span[text()='First Name']/following-sibling::input");
        this.parentsLName = page.locator("//div[contains(@class,'col-lg-6')]//div[2]//div[1]//input[1]");
        this.relationSelection = page.locator("//div[3]//div[1]//select[1]");
        this.parentsPhNo = page.locator("//input[@type='tel']");
        this.parentsEmailAddress = page.locator("//div[5]//div[1]//input[1]");
        this.country = page.locator("//div[6]//div[1]//select[1]");
        this.state = page.locator("//div[7]//div[1]//input[1]");
        this.city = page.locator("//span[text()='City']/following-sibling::input");
        this.finishButton = page.locator(".btn.primary-btn");
    }
      // ✅ Add this helper
    async safeClick(locator) {
        try {
            await locator.waitFor({ state: 'visible', timeout: 10000 });
            await locator.click();
        } catch (error) {
            throw new Error(`Failed to safely click on locator: ${locator}. Error: ${error.message}`);
        }
    }
    async setStudent(
        studentFirstName, 
        studentLastName, 
        studentDob, 
        studentPhno,
        studentNationality, 
        studentlevel, 
        studentGrade, 
        studentClassRoom,
        studentTiming, 
        studentTerms, 
        parentFirstName, 
        ParentLastName, 
        relation,
        parentPhNo, 
        parentEmailId, 
        parentCountry, 
        parentState, 
        parentCity
    ) {
        // Fill student basic information
        await this.firstName.fill(studentFirstName);
        await this.lastName.fill(studentLastName);

        try {
            // Handle date selection
            await this.dob.click();
            
            // Split the input DOB: dd-MM-yyyy
            const dobParts = studentDob.split("-");
            const day = dobParts[0];
            const month = dobParts[1];
            const year = dobParts[2];

            // Select Year
            await this.yearSelectionFromCalendar.selectOption(year);

            // Select Month (0-indexed, so subtract 1)
            await this.monthSelectionFromCalendar.selectOption({ index: parseInt(month) - 1 });

            // Click on the correct day
            const dateXpath = `//div[contains(@class,'react-datepicker__day') and text()='${parseInt(day)}'][not(contains(@class,'--outside-month'))]`;
            await this.page.locator(dateXpath).click();

        } catch (error) {
            throw new Error(`Failed to select DOB: ${studentDob}. Error: ${error.message}`);
        }

        // Fill remaining student fields
        await this.phno.fill(studentPhno);
       
        await this.nationality.selectOption({ label: studentNationality });
        await this.safeClick(this.continueButton);

        // Academic information
        await this.selectLevel.click();
        console.log("Select Level:", await this.selectLevel.textContent());
        
        if (studentlevel && studentlevel.trim()) {
            await this.selectLevel.selectOption({ label: studentlevel });
        } else {
            throw new Error("studentlevel is null or empty");
        }

        if (studentGrade && studentGrade.trim()) {
            await this.selectGrade.selectOption({ label: studentGrade });
        } else {
            throw new Error("studentgrade is null or empty");
        }

        if (studentClassRoom && studentClassRoom.trim()) {
            await this.classRoom.selectOption({ label: studentClassRoom });
        } else {
            throw new Error("student Class room value is null or empty");
        }

        if (studentTiming && studentTiming.trim()) {
            await this.timing.selectOption({ label: studentTiming });
        } else {
            throw new Error("student timing is null or empty");
        }

        await this.weekMonday.click();

        if (studentTerms && studentTerms.trim()) {
            await this.terms.selectOption({ label: studentTerms });
        } else {
            throw new Error("student terms selection is null or empty");
        }

        await this.safeClick(this.continueButton);
        
        // Wait for parent form to be visible
        await this.parentsFName.waitFor({ state: 'visible', timeout: 10000 });
        
        // Fill parent information
        await this.parentsFName.fill(parentFirstName);
        await this.parentsLName.fill(ParentLastName);

        if (relation && relation.trim()) {
            await this.relationSelection.selectOption({ label: relation });
        } else {
            throw new Error("Relation selection is null or empty");
        }

        await this.parentsPhNo.fill(parentPhNo);
        await this.parentsEmailAddress.fill(parentEmailId);
        await this.country.selectOption({ label: parentCountry });
        await this.state.fill(parentState);
        await this.city.fill(parentCity);

     //   await this.finishButton.click();
    }

   async checkNewlyAddedStudentVerification(data) {
    const fullName = `${data.studentFirstName} ${data.studentLastName}`;
    const expectedParentName = data.parentFirstName;
    const expectedParentPhone = data.parentPhNo;
    const expectedGrade = data.studentGrade;
    const expectedClassroom = data.studentClassRoom;

    // Locate the row of the newly created student
    const studentRow = this.page.locator(`//a[normalize-space()='${fullName}']/ancestor::tr`);
    await expect(studentRow).toBeVisible({ timeout: 80000 });

    // Verify Parent Name
    const parentNameText = await studentRow.locator('td:nth-child(4) div:nth-child(1)').textContent();
    expect(parentNameText.trim()).toContain(expectedParentName);

    // Verify Parent Phone
    const parentPhoneText = await studentRow.locator('td:nth-child(4) div:nth-child(2)').textContent();
    expect(parentPhoneText.trim()).toContain(expectedParentPhone);

    // ✅ Correct way: use CSS nth-child
    const actualGrade = await studentRow.locator('td:nth-child(5)').textContent();
    expect(actualGrade.trim()).toBe(expectedGrade);

    const actualClassroom = await studentRow.locator('td:nth-child(6)').textContent();
    expect(actualClassroom.trim()).toBe(expectedClassroom);

    console.log(`✅ Student '${fullName}' successfully created and verified.`);
}

}

module.exports = AddNewStudent;