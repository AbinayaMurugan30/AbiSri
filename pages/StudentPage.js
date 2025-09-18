class StudentPage {
    constructor(page) {
        this.page = page;
        
        // Element locators
        this.addNewStudent = page.locator("//button[normalize-space()='Add New Student']");
    }

    // Actions
    async selectAddNewStudent() {
        // Wait for element to be clickable and click
        await this.addNewStudent.waitFor({ state: 'visible', timeout: 10000 });
        await this.addNewStudent.click();
    }

    // Additional utility methods for better test functionality
    async isAddNewStudentButtonVisible() {
        try {
            await this.addNewStudent.waitFor({ state: 'visible', timeout: 5000 });
            return true;
        } catch (error) {
            return false;
        }
    }

    async isAddNewStudentButtonEnabled() {
        return await this.addNewStudent.isEnabled();
    }

    async getAddNewStudentButtonText() {
        return await this.addNewStudent.textContent();
    }

    // Method to wait for the page to be fully loaded
    async waitForPageLoad() {
        await this.addNewStudent.waitFor({ state: 'visible', timeout: 10000 });
    }
}

module.exports = StudentPage;