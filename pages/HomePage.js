const { expect } = require('@playwright/test');

class HomePage {
    constructor(page) {
        this.page = page;
        
        // Element locators
        this.mySchool = page.locator("//body/div[@id='root']/div[@id='wrapper']/div/div/div[@id='left-sidebar']/div[@class='sidebar-scroll']/div[@id='menu']/div[@id='left-sidebar-nav']/ul[@id='main-menu']/li[3]/a[1]");
        this.students = page.locator("//a[@href='/student']");
        this.status = page.locator("//div[@aria-label='Dropdown select']").nth(1);
        this.active = page.locator("//span[@role='option' and contains(text(), 'Active')]");
        this.inactive = page.locator("//span[@role='option' and contains(text(), 'Inactive')]");
        this.inactiveButton = page.locator("//button[normalize-space()='InActive']");
        this.studentName = page.locator("//th[normalize-space()='Students Name']//img[@class='ml-1 fa-sort']");
        this.studentTable = page.locator("//table[@class='table']");
        
        // Column data locators
        this.studentNameColumnData = page.locator("//table[@class='table']//tbody//tr//td[1], //div[@class='student-name-column']");
        this.studentIdColumnData = page.locator("//table[@class='table']//tbody//tr//td[2], //div[@class='student-id-column']");
        
        // Column headers
        this.studentNameColumnHeader = page.locator("//th[normalize-space()='Students Name']");
        this.studentIdColumnHeader = page.locator("//th[normalize-space()='Student ID']");
        
        // CSS selectors for fallback
        this.studentNameCells = "td:nth-child(1), .student-name-cell";
        this.studentIdCells = "td:nth-child(2), .student-id-cell";
    }

    // Navigation actions
    async selectMySchool() {
        await this.mySchool.click();
    }

    async selectStudents() {
        await this.students.click();
    }

    async setStatus() {
        await this.status.click();
    }

    async inActiveStatus() {
        await this.inactive.waitFor({ state: 'visible', timeout: 10000 });
        await this.inactive.click();
        return await this.inactive.textContent();
    }

    async activeStatus() {
        await this.active.waitFor({ state: 'visible', timeout: 10000 });
        await this.active.click();
        return await this.active.textContent();
    }

    async verifyAscendingDescendingOrder() {
        await this.studentName.click();
    }

    // Data extraction methods
    async getStudentNames() {
        await this.studentTable.waitFor({ state: 'visible', timeout: 10000 });
        
        // Try primary locator first, fallback to CSS selector if needed
        let nameElements;
        const primaryCount = await this.studentNameColumnData.count();
        
        if (primaryCount > 0) {
            nameElements = this.studentNameColumnData;
        } else {
            nameElements = this.page.locator(this.studentNameCells);
        }
        
        const names = [];
        const count = await nameElements.count();
        
        for (let i = 0; i < count; i++) {
            const name = await nameElements.nth(i).textContent();
            if (name && name.trim() !== '') {
                names.push(name.trim());
            }
        }
        
        return names;
    }

    async getStudentIds() {
        await this.studentTable.waitFor({ state: 'visible', timeout: 10000 });
        
        // Try primary locator first, fallback to CSS selector if needed
        let idElements;
        const primaryCount = await this.studentIdColumnData.count();
        
        if (primaryCount > 0) {
            idElements = this.studentIdColumnData;
        } else {
            idElements = this.page.locator(this.studentIdCells);
        }
        
        const ids = [];
        const count = await idElements.count();
        
        for (let i = 0; i < count; i++) {
            const id = await idElements.nth(i).textContent();
            if (id && id.trim() !== '') {
                ids.push(id.trim());
            }
        }
        
        return ids;
    }

    // Sorting verification methods
    async isStudentNamesSortedAscending() {
        const actualNames = await this.getStudentNames();
        const sortedNames = [...actualNames].sort((a, b) => 
            a.toLowerCase().localeCompare(b.toLowerCase())
        );
        
        return JSON.stringify(actualNames) === JSON.stringify(sortedNames);
    }

    async isStudentNamesSortedDescending() {
        const actualNames = await this.getStudentNames();
        const sortedNames = [...actualNames].sort((a, b) => 
            b.toLowerCase().localeCompare(a.toLowerCase())
        );
        
        return JSON.stringify(actualNames) === JSON.stringify(sortedNames);
    }

    async isStudentIdsSortedAscending() {
        const actualIds = await this.getStudentIds();
        
        // Try numeric comparison first
        try {
            const numericIds = actualIds.map(id => parseInt(id));
            
            // Check if all conversions were successful
            if (numericIds.every(id => !isNaN(id))) {
                const sortedIds = [...numericIds].sort((a, b) => a - b);
                return JSON.stringify(numericIds) === JSON.stringify(sortedIds);
            }
        } catch (error) {
            // Fall through to string comparison
        }
        
        // Fallback to string comparison for alphanumeric IDs
        const sortedIds = [...actualIds].sort((a, b) => 
            a.toLowerCase().localeCompare(b.toLowerCase())
        );
        
        return JSON.stringify(actualIds) === JSON.stringify(sortedIds);
    }

    async isStudentIdsSortedDescending() {
        const actualIds = await this.getStudentIds();
        
        // Try numeric comparison first
        try {
            const numericIds = actualIds.map(id => parseInt(id));
            
            // Check if all conversions were successful
            if (numericIds.every(id => !isNaN(id))) {
                const sortedIds = [...numericIds].sort((a, b) => b - a);
                return JSON.stringify(numericIds) === JSON.stringify(sortedIds);
            }
        } catch (error) {
            // Fall through to string comparison
        }
        
        // Fallback to string comparison for alphanumeric IDs
        const sortedIds = [...actualIds].sort((a, b) => 
            b.toLowerCase().localeCompare(a.toLowerCase())
        );
        
        return JSON.stringify(actualIds) === JSON.stringify(sortedIds);
    }

    // Column header clicking methods
    async clickStudentNameColumn() {
        await this.studentNameColumnHeader.waitFor({ state: 'visible', timeout: 10000 });
        await this.studentNameColumnHeader.click();
    }

    async clickStudentIdColumn() {
        await this.studentIdColumnHeader.waitFor({ state: 'visible', timeout: 10000 });
        await this.studentIdColumnHeader.click();
    }

    // Utility method for status selection and verification
    async selectStatusAndVerify(statusToSelect) {
        // Click the Status dropdown
        await this.status.waitFor({ state: 'visible', timeout: 10000 });
        await this.status.click();

        // Select the desired option (Active or Inactive)
        const option = this.page.locator(`//span[@role='option' and normalize-space(text())='${statusToSelect}']`);
        await option.waitFor({ state: 'visible', timeout: 10000 });
        await option.click();

        // Wait for table refresh
        await this.page.waitForTimeout(2000); // Consider replacing with more specific wait

        // Get all status buttons from the student rows
        const statusButtons = this.page.locator("//tbody//tr//td[last()]//button");
        const buttonCount = await statusButtons.count();

        // Validate each status matches the selected one
        for (let i = 0; i < buttonCount; i++) {
            const buttonText = await statusButtons.nth(i).textContent();
            console.log("Status displayed:", buttonText.trim());
            expect(buttonText.trim()).toBe(statusToSelect);
        }

        console.log(`✅ All students listed are '${statusToSelect}'`);
    }

    // Helper method to wait for elements (equivalent to WebPageActions methods)
    async waitForElementToBeVisible(locator, timeout = 10000) {
        await locator.waitFor({ state: 'visible', timeout });
    }

    async waitForElementToBeClickable(locator, timeout = 10000) {
        await locator.waitFor({ state: 'visible', timeout });
        // In Playwright, if an element is visible, it's generally clickable
    }
}

module.exports = HomePage;