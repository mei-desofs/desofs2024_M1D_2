# Threat Modeling



## 1.Data Flow Diagram

In the Data flow Diagram we Divided the diagrams between the processes in order to clarify the flow of data of the processes in question.

### Login Register DFD
![Login_RegisterDFD.drawio.svg](../DFD%2FLogin_RegisterDFD.drawio.svg)

### Create Portfolio DFD
![Create a Portfolio.drawio.svg](../DFD%2FCreate%20a%20Portfolio.drawio.svg)

### Browse Photos DFD
![BrowsePhoto.drawio.svg](../DFD%2FBrowsePhoto.drawio.svg)

### Add to Cart DFD
![AddToCart.drawio.svg](../DFD%2FAddToCart.drawio.svg)

### Put a Photo up for sale DFD
![AddPhotoForSale.drawio.svg](../DFD%2FAddPhotoForSale.drawio.svg)

### Browse Users DFD
![Browse Users DFD](../image/DFD/DFDBrowseUsers.svg)

### Checkout Cart DFD
![Checkout Cart DFD](../image/DFD/DFDBrowseUsers.svg)

### Delete Photo DFD
![Delete Photo DFD](../image/DFD/DFDDeletePhoto.svg)

### Edit User Roles DFD
![Edit User Roles DFD](../image/DFD/DFDEditUserRoles.svg)

### Suspend User DFD
![Suspend User DFD](../image/DFD/DFDSuspendUser.svg)

### Suspend User DFD
![Suspend User DFD](../image/DFD/DFDViewPurchasedPhotos.svg)


## 2 Security Requirements Engineering  
### 2.1 Authentication and Authorization  
#### Security Requirements:
- **User Authentication**: Put in place secure ways for clients, photographers, and admins to prove who they are (e.g., username/password combo, multiple verification steps) before accessing the website.

- **Role-based Access Control**: Define roles (client, photographer, admin) and assign features that are relevant to their role.

- **Session Management**: Use secure session management techniques to prevent session hijacking and ensure that user sessions expire after a defined period of inactivity.

### 2.2 Data Protection

#### Security Requirements:

- **Data Encryption**: Sensitive data (like user credentials, payment details) must be encrypted during transmission and when stored to prevent unauthorized access.

- **Secure Storage**: To ward off any probable anomalies or unauthorized data breaches, develop appropriate storage for photographs and user information.

- **Data Anonymization**: Anonymize or pseudonymize user data where possible to protect the privacy of users and abide by the provisions of the legislation on personal data protection.

### 2.3 Secure Transactions

#### Security Requirements:

- **Secure Payment Gateway**: Secure payment gateway, integrating strong encryption and fraud detection capabilities, is required to ensure secure financial transaction processing.

- **HTTPS Encryption**: Ensure that all HTTPS transactions occur including payment transactions so as to encrypt data in transit.

- **Transaction Logging**: For the sake of audit and monitoring, it is necessary to log all transaction activities intended for detecting suspicious or fraudulent transactions.

### 2.4 Content Protection

#### Security Requirements:

- **Watermarking**: Implement watermarked image techniques for photos thus discouraging unauthorized use or distribution without proper referencing.

- **Access Control**: Only authenticated users should be able to access high-resolution photos or premium content to avoid unapproved downloads or sharing.

- **Digital Rights Management (DRM)**: DRM technologies are implemented here to protect copyright and determine photo access, copying as well as distribution.

### 2.5 Secure Administration

#### Security Requirements:

- **Admin Access Controls**: The admin accounts should have stringent access controls in order to prevent any unauthorized entry into administrative functionalities such as sensitive data storage facility.

- **Audit Logging**: Log all admin activities and changes made to the website's configuration or user data for accountability and forensic purposes.

- **Two-Factor Authentication (2FA)**: Enforce 2FA for admin accounts to add an extra layer of security and prevent unauthorized access in case of compromised credentials.

## Abuse Cases

### Abuse cases for Sign Up and Login

![AbuseCaseSignUpLogin.svg](AbuseCaseSignUpLogin.svg)
### Abuse cases for Browse Photos

![AbuseCaseBrowsePhotos.svg](AbuseCaseBrowsePhotos.svg)


Here we have the list of abuse cases with a description associated and a mitigation for each one.

| Abuse Case                  | Description                                                                   | Priority | Risk                                                                              | Mitigation                                                                                                                                                                         |
|-----------------------------|-------------------------------------------------------------------------------|----------|-----------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Brute Force Attack          | The attacker keeps trying to break into user accounts without permission      | High     | High: Potential data breaches and unauthorized access                             | Implement measures like account lockouts, strong password policies, and monitoring for strange login behavior.                                                                     |
| Automated Registration      | The attacker uses automated tools to create many fake accounts, which can overwhelm the system and drain resources. | High     | High: Drain the system                                                            | CAPTCHA, rate limiting, and account verification to detect and stop automated registrations.                                                                                       |
| Credential Theft            | The attacker obtains login credentials through phishing or malware, resulting in unauthorized access.  | High     | High: Unauthorized access to user accounts and potential data breaches            | Implement 2-Factor authentication, ensure better credentials, regularly update security software.                                                                                   |
| Session Hijacking           | The attacker intercepts or steals session IDs to gain unauthorized access      | High     | High: Hijacking active user sessions and accessing sensitive data                 | Implementation of HTTPS, use secure cookies, and regularly rotate session IDs.                                                                                                      |
| Account Takeover            | The attacker gains unauthorized access to user accounts, compromising user account integrity and privacy.   | High     | High: Compromising user account integrity and privacy                             | Essential to implement strong authentication mechanisms, monitor for unusual account activity, and provide users with security awareness training.                                 |
| Unauthorized Access         | The attacker exploits vulnerabilities to gain access to restricted information | High     | High: Violating user privacy and confidentiality                                  | Implement access controls, encryption, and security patches to mitigate vulnerabilities and protect against unauthorized access.                                                   |
| Content Scraping            | The attacker uses automated tools to copy or download content without authorization | Medium | High: Undermining intellectual property rights and compromising content integrity | Implement rate limiting, CAPTCHA, and access controls to prevent unauthorized access to content and deter scraping.                                                                |
| Data Harvesting             | The attacker collects metadata or information associated with user accounts or system resources for malicious purposes | Medium | High: Threatening user privacy and confidentiality                                | Implement data encryption, anonymization techniques, and user consent mechanisms to protect against unauthorized data harvesting.                                                  |
| Denial of Service Attack    | The attacker floods the system with a high volume of requests, overwhelming resources and causing service downtime | High     | High: Disrupting normal system operations and causing loss of user trust          | Implement DDoS protection measures, such as rate limiting, traffic filtering, and distributed caching, and maintain scalable infrastructure to mitigate the impact of DoS attacks. |



## Test Planning

### Login/SignUp UC
##### Penetration Tests

#### 1. Automated Registration
- **Objective**: Assess the resistance of the automated registration process to exploitation.
- **Test Steps**:
    1. Utilize automated tools to submit registration requests with a large volume of fake user data.
    2. Monitor the application's response to identify potential vulnerabilities or weaknesses.
- **Expected Result**: The application should detect and prevent automated registration attempts, such as CAPTCHA verification or rate limiting.

#### 2. Brute Force Attack
- **Objective**: Evaluate the strength of the login mechanism against brute force attacks.
- **Test Steps**:
    1. Employ automated tools to conduct a brute force attack on the login page, attempting various username/password combinations.
    2. Monitor the application's response and server logs for signs of excessive login attempts.
- **Expected Result**: The application should implement account lockout mechanisms or rate limiting to mitigate brute force attacks.


#### 4. Session Hijacking
- **Objective**: Assess the susceptibility of the application to session hijacking attacks.
- **Test Steps**:
    1. Attempt to intercept and steal session cookies transmitted over insecure channels using tools like Wireshark.
    2. Use stolen session cookies to impersonate authenticated users and gain unauthorized access to their accounts.
- **Expected Result**: The application should implement secure session management techniques, such as HTTPS encryption and session tokens with short expiration times, to prevent session hijacking.

#### 5. Account Takeover
- **Objective**: Determine if the application is vulnerable to account takeover attacks.
- **Test Steps**:
    1. Exploit weaknesses in the password reset mechanism to reset the password of a user account without proper authentication.
    2. Attempt to gain unauthorized access to authenticated user accounts by stealing session cookies or exploiting other vulnerabilities.
- **Expected Result**: The application should enforce strong authentication mechanisms, secure session management, and account recovery procedures to prevent unauthorized account takeovers.

#### 6. SQL Injection
- **Objective**: Identify and mitigate SQL injection vulnerabilities in the application.
- **Test Steps**:
    1. Submit malicious SQL queries via input fields (e.g., login form, registration form) to test for SQL injection vulnerabilities.
    2. Analyze the application's response and database logs for indications of SQL injection attacks.
- **Expected Result**: The application should sanitize user input and use parameterized queries or prepared statements to prevent SQL injection attacks.

### Add Photo to cart and Create a Portfolio UC

##### 1. Unit Tests:
- **Add Photo to Cart UC**:
    - **Test Cases**:
        1. Verify that the correct item is added to the cart;
        2. Ensure that the quantity of the item in the cart is updated correctly;
        3. Test the calculation of the total price after adding items to the cart;
    - **Tools/Techniques**: Mocking frameworks for isolating components, assertion libraries for verifying outputs.

- **Create Portfolio UC**:
    - **Test Cases**:
        1. Validate that a portfolio is successfully created with valid input data;
        2. Ensure that the portfolio creation process handles error cases (e.g., empty input fields, maximum character limits);
        3. Test the behavior of the system when creating a portfolio with duplicate names or conflicting data;
    - **Tools/Techniques**: Mocking frameworks for isolating components, assertion libraries for verifying outputs.

##### 2. Functional Tests:
- **Add Photo to Cart UC**:
    - **Test Cases**:
        1. Validate that users can add items to the cart from different product pages;
        2. Test the behavior of the system when adding items to the cart with different quantities;
        3. Verify that users cannot add out-of-stock items to the cart;
    - **Tools/Techniques**: Test automation frameworks (e.g., Cypress).

- **Create Portfolio UC**:
    - **Test Cases**:
        1. Validate that photographers can successfully create a portfolio from the dashboard;
        2. Test the behavior of the system when creating portfolios with different types of media (e.g., images, videos);
        3. Verify that portfolios are displayed correctly on the photographer's profile page after creation;
    - **Tools/Techniques**: Test automation frameworks (e.g., Cypress).

##### 3. End-to-End (E2E) Tests:
- **Add Photo to Cart UC**:
    - **Test Cases**:
        1. Validate the end-to-end flow of adding items to the cart, including navigating product pages, adding items, and viewing the cart contents;
    - **Tools/Techniques**: Test automation frameworks (e.g., Cypress).

- **Create Portfolio UC**:
    - **Test Cases**:
        1. Validate the end-to-end flow of creating a portfolio, including logging into the photographer's account, navigating to the portfolio creation page, entering portfolio details, and saving the portfolio;
        2. Test the visibility of the new created portfolio on the photographer's profile page or portfolios page;
    - **Tools/Techniques**: Test automation frameworks (e.g., Cypress), headless browsers, browser automation tools.


