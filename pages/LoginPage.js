class LoginPage {
    constructor(page) {
        this.page = page;
        
        // Element locators
        this.email = page.locator("//input[@placeholder='Email address']");
        this.password = page.locator("//input[@id='signin-password']");
        this.loginButton = page.locator("//button[@type='submit']");
    }

    // Actions
    async login(userEmail, userPassword) {
        // Fill email and password fields
        await this.email.fill(userEmail);
        await this.password.fill(userPassword);
        
        // Click login button
        await this.loginButton.click();
    }

    // Additional utility methods for better test reliability
    async waitForLoginForm() {
        await this.email.waitFor({ state: 'visible', timeout: 10000 });
        await this.password.waitFor({ state: 'visible', timeout: 10000 });
        await this.loginButton.waitFor({ state: 'visible', timeout: 10000 });
    }

    async isLoginFormVisible() {
        try {
            await this.waitForLoginForm();
            return true;
        } catch (error) {
            return false;
        }
    }

    // Method to clear fields (useful for test scenarios)
    async clearFields() {
        await this.email.clear();
        await this.password.clear();
    }

    // Method to get field values (useful for validation)
    async getEmailValue() {
        return await this.email.inputValue();
    }

    async getPasswordValue() {
        return await this.password.inputValue();
    }

    // Check if login button is enabled
    async isLoginButtonEnabled() {
        return await this.loginButton.isEnabled();
    }
}

module.exports = LoginPage;