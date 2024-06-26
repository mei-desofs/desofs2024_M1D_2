# Threat Modeling



## 1.Data Flow Diagram

In the Data flow Diagram we Divided the diagrams between the processes in order to clarify the flow of data of the processes in question.

### Login DFD
![Login_RegisterDFD.drawio.svg](../DFD/Login.png)

### Register DFD
![Login_RegisterDFD.drawio.svg](../DFD/Register.png)

### Create Portfolio DFD
![Create a Portfolio.drawio.svg](../DFD%2FCreate%20a%20Portfolio.drawio.svg)

### Browse Photos DFD
![BrowsePhoto.drawio.svg](../DFD%2FBrowsePhoto.drawio.svg)

### Add to Cart DFD
![AddToCart.drawio.svg](../DFD%2FAddToCart.drawio.svg)

### Add a Photo up for sale DFD
![AddPhotoForSale.svg](..%2FDFD%2FAddPhotoForSale.svg)

### Browse Users DFD
![Browse Users DFD](../image/DFD/DFDBrowseUsers.svg)

### Checkout Cart DFD
![Checkout Cart DFD](../image/DFD/DFDCheckoutCart.svg)

### Delete Photo DFD
![Delete Photo DFD](../image/DFD/DFDDeletePhoto.svg)

### Edit User Roles DFD
![Edit User Roles DFD](../image/DFD/DFDEditUserRoles.svg)

### Suspend User DFD
![Suspend User DFD](../image/DFD/DFDSuspendUser.svg)

### View Purchased Photos DFD
![Suspend User DFD](../image/DFD/DFDViewPurchasedPhotos.svg)


## 2 Security Requirements Engineering  

### 2.1 Authentication and Authorization  
#### Security Requirements:
- **User Authentication**: Put in place secure ways for clients, photographers, and admins to prove who they are (e.g., username/password combo, multiple verification steps) before accessing the website.

- **Role-based Access Control**: Define roles (client, photographer, admin) and assign features that are relevant to their role.

- **Session Management**: Use secure session management techniques to prevent session hijacking and ensure that user sessions expire after a defined period of inactivity.

- **Password Policy**: Implement a strong password policy that enforces minimum length, complexity(Checking against a set of breached passwords) for better user security.

In the authentication and authorization process of the application, we used an OAuth2.0 server to manage the authentication and authorization of the users. The OAuth2.0 server is responsible for issuing access tokens to the clients after successfully authenticating the users. The access tokens are then used by the clients to access the protected resources on the server. The OAuth2.0 server also enforces the authorization rules to ensure that only authorized users can access the protected resources.
The OAuth2 was Keycloak, an open-source identity and access management solution that provides a secure and scalable authentication and authorization service. Keycloak supports various authentication mechanisms, including username/password, social login, and multi-factor authentication. It also provides role-based access control and fine-grained authorization policies to secure the application's resources.

In Keycloak we managed to communicate with the MySQL database that was used to store the other entities of the application.
For the login process we put a MFA in order to enter the application in order to harder to authenticate to a user.
![MFAInKeyCloak.png](../images/MFAInKeyCloak.png)  
<p align="center">MFA in Keycloak</p>

In the register process we put a password policy that enforces a minimum length, complexity, and checking against a set of breached passwords for better user security and also a ReCaptcha code.
![ReCaptchaRegistration.png](..%2F..%2F..%2F..%2F..%2FDesktop%2FImagensDESOFS%2FReCaptchaRegistration.png)  
<p align="center">ReCaptcha in the registration process</p>

In the SSO login there is also a session expirancy in order to prevent session hijacking.
![SessionExpirancy.png](../images/SessionExpirancy.png)  
<p align="center">Session expirancy</p>  
KeyCloak as a lot of policies in terms of security in which they will prevent certain actions to be made by the users.
![PoliciesOauth2.png](../images/PoliciesOauth2.png)
<p align="center">Policies that Keycloak provide</p>  

The algorithm used for encryption is the RS256 algorithm that is a public key encryption that uses a public and private key pair to encrypt and decrypt data.  

![AlgorythmTokenOauth.png](../images/AlgorythmTokenOauth.png)
<p align="center">Algorithm utilized</p>  

In terms of the password policy, the application has a password policy that enforces a minimum length, complexity, and checking against a set of breached passwords for better user security. The password policy ensures that users create strong and secure passwords to protect their accounts from unauthorized access. The policy includes requirements such as minimum length of 8 characters and no more than 128 characters, that is configured in keycloak.

![PasswordPolicy.png](../images/Password%20Policy.png)
<p align="center">Password Policy of Application configuration</p>  


### 2.2 Data Protection

#### Security  Requirements:

- **Data Encryption**: Sensitive data (like user credentials, payment details) must be encrypted during transmission and when stored to prevent unauthorized access.

- **Secure Storage**: Develop appropriate storage for photos and user information.

- **Data Anonymization**: Anonymize user data where possible to protect the privacy of users.

- **Read-Once Objects**: Objects containing sensitive information should be overwritten with random data after being read to prevent data leakage. See example in this file [UserDTO.java](../../AngularProject/src/main/java/backend/service/dto/UserDTO.java)

```
    public String getPassword() {
        final String returnValue = new String(password);
        Arrays.fill(password, '\0'); // overwrite with zeros
        return returnValue;
    }
```
<p align="center">Read-Once Object example</p>  

### 2.3 Secure Transactions

#### Security Requirements:

- **Secure Payment Gateway**: Secure payment gateway, integrating strong encryption and fraud detection capabilities, is required to ensure secure financial transaction processing.

- **HTTPS Encryption**: Ensure that all HTTPS transactions occur including payment transactions so as to encrypt data in transit.

- **Transaction Logging**: For the sake of audit and monitoring, it is necessary to log all transaction activities intended for detecting suspicious or fraudulent transactions.

### 2.4 Content Protection

#### Security Requirements:

- **Access Control**: Only authenticated users should be able to access high-resolution photos or premium content to avoid unapproved downloads or sharing.

### 2.5 Secure Administration

#### Security Requirements:

- **Admin Access Controls**: The admin accounts should have stringent access controls in order to prevent any unauthorized entry into administrative functionalities such as sensitive data storage facility.

- **Audit Logging**: Log all admin activities and changes made to the website's configuration or user data for accountability and forensic purposes.

- **Two-Factor Authentication (2FA)**: Enforce 2FA for accounts to add an extra layer of security and prevent unauthorized access in case of compromised credentials.



### RESTful Web Service:
<a name="restful-web-service"></a>

#### Security Requirements:
- **Verify that JSON schema validation is in place and verified before accepting input.**:Add JSON schema validation to the input processing pipeline of the application. Validate input data against the predefined JSON schema before processing to ensure it meets expected formats and integrity. Provide user feedback in case of any errors.
- Verify that the REST services validate that the request's content-type is the expected one.


### Data Protection and Privacy Architecture:
<a name="data-protection-and-privacy-architecture"></a>

#### Security Requirements:
- **Verify that all sensitive data is identified and classified into protection levels.**:Conduct a comprehensive data inventory to identify and classify all sensitive data handled by the application


### Access Control Architecture:
<a name="access-control-architecture"></a>

#### Security Requirements:
- **Verify the application uses a single and well-vetted access control mechanism for accessing protected data and resources**:Ensure that the app routes all requests from different sources so it doesn't allow unauthorized entry points into the data or systems.

### Implementation of Automated Security Checks in Build Pipeline:
<a name="implementation-of-automated-security-checks-in-build-pipeline"></a>

#### Security Requirements:
1. **Verify that the build pipeline contains a build step to automatically build and verify the secure deployment of the application.**

**Solution**: Make sure to include automated security checks in the build pipeline to guarantee that the deployment process is secure and adheres to security standards.

### Implementation of Password Change Functionality:
<a name="implementation-of-password-change-functionality"></a>

#### Security Requirements:
1. **Verify users can change their password.**

**Solution**: Create a feature that enables users to change their passwords securely.

### Implementation of Attribute-Based Access Control with Immutable User and Data Attributes:
<a name="implementation-of-attribute-based-access-control-with-immutable-user-and-data-attributes"></a>

### Implementation of Multi-Factor Authentication for Administrative Interfaces:
<a name="implementation-of-multi-factor-authentication-for-administrative-interfaces"></a>

#### Security Requirements:
1. **Verify administrative interfaces use appropriate multifactor authentication to prevent unauthorized use.**

**Solution**: Add multifactor authentication (MFA) to administrative interfaces for increased security and protection against unauthorized access.

### Implementation of Secure Data Transmission Practices:
<a name="implementation-of-secure-data-transmission-practices"></a>

#### Security Requirements:
1. **Verify that sensitive data is sent to the server in the HTTP message body or headers, and that query string parameters from any HTTP verb do not contain sensitive data.**

**Solution**: Ensure sensitive data is securely transmitted to the server by implementing secure data transmission practices, to avoid exposure in URLs or query string parameters.

### Implementation of Content-Type Validation for Uploaded Files:
<a name="implementation-of-content-type-validation-for-uploaded-files"></a>

#### Security Requirements:
1. **Verify that direct requests to uploaded files will never be executed as HTML/JavaScript content.**

**Solution**: Please add content-type validation for uploaded files to prevent them from being incorrectly executed as HTML or JavaScript content if accessed directly.

### Implementation of Role-Based Access Control for RESTful API Methods:
<a name="implementation-of-role-based-access-control-for-restful-api-methods"></a>

### Cross-Site Request Forgery (CSRF) Protection:
The springSecurityFilterChain method from the SpringBoot Application configures CSRF protection using CookieServerCsrfTokenRepository.withHttpOnlyFalse(). This ensures that the CSRF token is stored in a cookie and is not accessible via JavaScript, which can help prevent CSRF attacks.  
### Content Security Policy (CSP):
The springSecurityFilterChain method from the SpringBoot Application also sets a Content Security Policy (CSP) for your application. A CSP can help to mitigate the risk of Cross-Site Scripting (XSS) attacks by specifying the domains that the browser should consider to be valid sources of executable scripts.


### HTTP response headers:

#### Security Requirements:
- HTTP responses must include a content-type header that accurately reflects the content being returned.
- The response content must match the provided content-type header.
- HTTP responses must specify the character encoding used for the content.
- Requests containing missing or unexpected content-type headers should be rejected with appropriate HTTP codes.

### API URL Structure:

#### Security Requirements:
- API URLs cannot expose sensitive information.


### File Size and Type Validation:

#### Security Requirements:
- Verify that the application validates file size(no more than 10 megabytes) and type(.png,.svg,.jpg,.jpeg) before processing.
  ![File Size](../images/FileSizeTooLarge.png)
    <p align="center">File Size Validation</p>

    ![File Type](../images/FileValidation.jpg)
    <p align="center">File Type Validation</p>

    ![spring-configuration.png](../images/spring-configuration.png)
    <p align="center">Spring file size limit configuration (backend)</p>

### Logging for Security Events:

The application also has Logs for security events relative to the application. The logs are stored in a file and are used to monitor and track security-related events such as login attempts, access control violations, and other security incidents. The logs are used for auditing, monitoring, and incident response purposes to ensure the security of the application.
Only the admin can access the logs in order to have a better control of the application.

![Logs.png](../images/LogsAdmin.png)
<p align="center">Logs</p>

### Admin Dashboard with application metrics

The application also has a dashboard for the admin to see the metrics of the application. The metrics include the HTTP requests Endpoints requested, system uptime, system startTime, CPU usage, threads count and memory usage.

![Metrics.png](../images/AdminMetrics.png)
<p align="center">Admin Metrics</p>

### Swagger for Admin

The application also has a Swagger for the admin to see the endpoints of the application. The Swagger is a tool that helps to document and test the endpoints of the application.

![Swagger.png](../images/Swagger.png)
<p align="center">Swagger for Admin</p>



### Code Coverage:

In terms of Code Coverage we used Sonarqube to check the code coverage of the application.
For this we used a docker container to run the Sonarqube and then we created a sonar project in order to get a sonar token to save the data from the coverage into a sonar project.
![Build sucess in mvn verify.png](../images/Build%20sucess%20in%20mvn%20verify.png)
<p align="center">Maven verify build success</p>  

![Sonarqube.png](../images/Sonarqube.png)
<p align="center">Sonarqube Code Coverage</p>


### Docker Hub:

The application (Database and Keycloak also) is fully dockerized and the images are stored in the Docker Hub repository. The images are tagged with the latest version and the latest commit hash to ensure traceability and version control. The images are scanned for vulnerabilities using Docker Hub's built-in security scanning feature to identify and mitigate potential security risks.
They are all in different repositories in order to have a better organization of the images.

![DockerHub.png](../images/DockerHub.png)
<p align="center">Docker Hub</p>

## Abuse Cases

### Abuse cases for Sign Up and Login

![AbuseCaseSignUpLogin.svg](AbuseCaseSignUpLogin.svg)
### Abuse cases for Browse Photos

![AbuseCaseBrowsePhotos.svg](AbuseCaseBrowsePhotos.svg)

### Abuse Cases for View Purchased Photos
![AbuseCasesViewPurchasedPhotos](../image/abuseCases/ViewPurchasedPhotosAbuseCases.svg)

### Abuse Cases for Add Photo to Cart
![AbuseCasesAddPhotoToCart](../image/abuseCases/AddPhotoToCartAbuseCase.png)

### Abuse Cases for Add Photo to Portfolio
![AbuseCasesAddPhotoToPortfolio](../image/abuseCases/AddPhotoToPortfolioAbuseCase.svg)

### Abuse Cases for Browse Users
![AbuseCasesBrowseUsers](../image/abuseCases/BrowseUsersAbuseCase.png)

### Abuse Cases for Checkout Cart
![AbuseCasesCheckoutCart](../image/abuseCases/CheckoutCartAbuseCase.png)

### Abuse Cases for Create Portfolio
![AbuseCasesCreatePortfolio](../image/abuseCases/CreatePortfolioAbuseCase.png)

### Abuse Cases for Delete Photo
![AbuseCasesDeletePhoto](../image/abuseCases/DeletePhotoAbuseCase.png)


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
| Unauthorized User Tries to Delete Photo        | An unauthorized user attempts to delete a photo without proper authentication or authorization.                     | High     | High: Potential data loss or corruption                        | Implement robust authentication and authorization mechanisms to ensure that only authorized users can perform deletion operations.       |
| Mass Deletion of Photos                        | A user attempts to delete a large number of photos at once, potentially causing system overload or data loss.        | High     | High: System overload, data loss                               | Implement rate limiting and confirmation dialogs to prevent accidental mass deletions and protect against intentional abuse.            |
| Delete Photo Belonging to Another User         | A user tries to delete a photo that belongs to another user, violating data ownership and privacy rights.           | High     | High: Data loss, privacy violation                              | Implement strict access controls and permissions to ensure that users can only delete their own photos.                                |
| Delete Non-existent Photo                      | A user attempts to delete a photo that does not exist, potentially causing system errors or data inconsistencies.  | Medium   | Medium: System errors, data inconsistencies                     | Implement error handling mechanisms to gracefully handle deletion requests for non-existent photos.                                    |
| Accidental Deletion                            | A user unintentionally deletes a photo due to interface confusion or misclicks.                                      | Medium   | Medium: Data loss, user frustration                             | Implement confirmation dialogs and undo functionalities to allow users to recover from accidental deletions.                            |
| System Overload by Searching All Users         | A user performs a search operation that retrieves a large number of user records, causing system overload.          | High     | High: System slowdown, resource exhaustion                      | Implement pagination and caching mechanisms to handle large datasets efficiently and prevent system overload.                         |
| Inadequate Filtering When Searching All Users | The search functionality lacks proper filtering, allowing users to retrieve sensitive information unintentionally. | High     | High: Data exposure, privacy violation                           | Implement robust filtering mechanisms and access controls to ensure that only authorized information is retrieved in searches.          |
| Display of Sensitive Information               | The system inadvertently displays sensitive user information, such as passwords or personal data.                  | High     | High: Data exposure, privacy violation                           | Implement data masking, encryption, and access controls to prevent unauthorized access to sensitive information.                      |
| Data Exploitation Through User Search         | An attacker exploits the search functionality to gather information about users for malicious purposes.            | High     | High: Data theft, privacy violation                              | Implement rate limiting, CAPTCHA, and access controls to detect and prevent automated data harvesting attempts.                        |
| Search for Specific User                      | A user searches for a specific user account to gather information without proper authorization.                   | Medium   | Medium: Privacy violation, unauthorized access                   | Implement access controls and logging mechanisms to track and audit user search activities, ensuring that only authorized users can access user information. |
| SQL Injection Attempt                          | An attacker attempts to inject SQL queries into the search functionality to gain unauthorized access to data.      | High     | High: Data theft, system compromise                              | Implement parameterized queries and input validation to prevent SQL injection attacks and protect against unauthorized access to sensitive data.    |
| Accessing Another User's Cart by ID           | A user attempts to access another user's shopping cart by manipulating the cart ID in the URL.                   | High     | High: Data theft, privacy violation                              | Implement proper authentication and authorization checks to ensure that users can only access their own shopping carts.                      |
| Non-existent Cart Selection                    | A user selects a shopping cart that does not exist in the system, potentially causing system errors.               | Medium   | Medium: System errors, user frustration                          | Implement error handling mechanisms to gracefully handle requests for non-existent shopping carts.                                       |
| Unauthorized Access to Shopping Cart          | An unauthorized user tries to access shopping carts without proper authentication or authorization.              | High     | High: Data theft, privacy violation                              | Implement strict access controls and session management mechanisms to prevent unauthorized access to shopping carts.                      |
| ID Manipulation for Shopping Cart Access      | An attacker manipulates shopping cart IDs to gain unauthorized access to other users' shopping carts.           | High     | High: Data theft, privacy violation                              | Implement secure session management, encryption, and access controls to prevent ID manipulation attacks and protect user privacy.          |


## Test Planning

### Login/SignUp UC
##### Unit Tests

##### Unit Tests for Login Functionality:

- **Password Encryption**:
    - Test that the provided password is properly encrypted.

- **User Authentication**:
    - Test the authentication process with valid username and password.
    - Ensure that the user is authenticated successfully.

- **Invalid Credentials**:
    - Test the authentication process with invalid username and/or password.
    - Ensure that the user is not authenticated and receives appropriate error messages.

- **Session Management**:
    - Test the session management functionality by verifying the creation and deletion of session tokens upon successful login and logout.


##### Unit Tests for Registration Functionality:

- **User Validation**:
    - Test the validation of user input fields (e.g., username, email, password) during registration.
    - Ensure that valid input formats are accepted and invalid inputs are rejected with appropriate error messages.

- **Duplicate Username or Email**:
    - Test the registration process with a username or email that already exists in the database.
    - Ensure that the user receives an error message indicating that the username or email is already taken.

- **Password Strength**:
    - Test the registration process with weak passwords (e.g., short length, no special characters).
    - Ensure that the user receives an error message indicating the password requirements.


##### Penetration Tests

#### SQL Injection
- **Objective**: Identify and mitigate SQL injection vulnerabilities in the application.
- Tools: Manual testing or SQLMap
- **Test Steps**:
    1. Submit malicious SQL queries via input fields manually to test for SQL injection vulnerabilities.
    2. Analyze the application's response and database logs for indications of SQL injection attacks.
- **Expected Result**: The application should sanitize user input and use parameterized queries or prepared statements to prevent SQL injection attacks.  

![sqlMapLogin.png](../images/sqlMapLogin.png)
<p align="center">SQLMap for Login</p>  

### Browse Photos UC

##### Unit Tests

- **User Permissions**:  
    - Test browsing photos functionality with different user roles to ensure that access permissions are enforced correctly.

- **Retrieve Photos**:
    - Test that the application can successfully retrieve photos from the database.
    - Ensure that the correct photos are returned based on the specified criteria.
- **Sorting**:
    - Test the filtering and sorting functionality to verify that users can search for photos based on various criteria.
    - Ensure that the results are displayed in the correct order and format.

##### Penetration Tests
##### Penetration Testing for Browse Photos Use Case:

#### Unauthorized Access:
- **Objective**: Attempt to access the Browse Photos functionality without proper authentication.
- Tools: Manual Testing
- **Test Steps**:
    1. Test the application's response to unauthenticated requests to the Browse Photos page.
- **Expected Result**: The application should deny access and return an error message indicating that authentication is required.


#### Denial of Service Attack:
- **Objective**: Test the application's resilience against denial of service (DoS) attacks.
- Tools: JMeter
- **Test Steps**:
    1. Use stress testing tools to simulate a high volume of requests to the Browse Photos functionality.
    2. Observe the application's response and performance under the increased load.
- **Expected Result**: The application should be able to handle a significant increase in traffic without experiencing downtime or degradation in performance.


![BrowsePhotosZAP.png](../images/ViewGalleryZAP.png)
<p align="center">OWASP ZAP Report for Browse Photos</p>

As we can see in the [report](../ZAP-Reports/ViewGallery.html) there are some vulnerabilities that need to be fixed in the Create Portfolio use case.
In particular, one high-risk vulnerability was identified: Open Redirect.
This vulnerability can be exploited by attackers to redirect users to malicious websites, potentially leading to phishing attacks or other security threats.
It is crucial to address this vulnerability promptly to prevent potential exploitation and protect user security.
In total, 22 alerts were reported, including 1 high-risk, 8 medium-risk, 7 low-risk and 6 informational.


### Create a Portfolio UC

##### 1. Unit Tests:
- **Create Portfolio UC**:
    - **Test Cases**:
        1. Validate that a portfolio is successfully created with valid input data;
        2. Ensure that the portfolio creation process handles error cases (e.g., empty input fields, maximum character limits);
        3. Test the behavior of the system when creating a portfolio with duplicate names or conflicting data;
    - **Tools/Techniques**: Mocking frameworks for isolating components, assertion libraries for verifying outputs.

##### 2. Functional Tests:

- **Create Portfolio UC**:
    - **Test Cases**:
        1. Validate that photographers can successfully create a portfolio from the dashboard;
        2. Test the behavior of the system when creating portfolios with different types of media (e.g., images, videos);
        3. Verify that portfolios are displayed correctly on the photographer's profile page after creation;
    - **Tools/Techniques**: Test automation frameworks (e.g., Cypress).


#### 4. Penetration Tests

Penetration testing helps identify security vulnerabilities by simulating real-world attacks. For the abuse cases identified in the application, penetration testing can uncover weaknesses exploited by attackers. Here's a brief plan:

#### 1. Create a Portfolio:
- **Objective**: Test for vulnerabilities data manipulation, and denial of service in the "Create a Portfolio" UC.
- **Approach**: Check resistance against content scraping, probe for unauthorized access, test data manipulation, and check resilience against denial of service.
- **Tools/Techniques**: manual testing, load tests.

#### 3. Reporting:
- Compile a report detailing discovered vulnerabilities, severity levels, and recommended mitigation measures.
- Prioritize vulnerabilities and provide actionable recommendations for mitigation.
- Use ZAP Scanning Report.

By conducting penetration tests, we can proactively identify and address security vulnerabilities, reducing the risk of successful attacks and improving the application's security.

![CreatePortfolioZAP.png](../images/CreatePortfolioZAP.png)
<p align="center">OWASP ZAP Report for Create Portfolio</p>

As we can see in the [report](../ZAP-Reports/CreatePortfolio-.html) there are some vulnerabilities that need to be fixed in the Create Portfolio use case.
These vulnerabilities include: Cross-Domain Misconfiguration and X-Content-Type-Options Header Missing, among others.
No high-risk vulnerabilities were found for this use case, which is a positive outcome.
However, it is essential to address the medium and low-risk vulnerabilities to enhance the security of the application.

### Use Case: Delete Photo

#### Test Planning:

1. **Unit Tests:**
    - **Test Cases**:
        1. Verify that unauthorized users cannot delete photos.
        2. Ensure that only authenticated and authorized users can initiate photo deletion.
        3. Test error handling for unauthorized deletion attempts.
    - **Tools/Techniques**: Mocking frameworks for authentication, assertion libraries for verifying permissions.

2. **Functional Tests:**
    - **Test Cases**:
        1. Validate that authenticated users can successfully delete their own photos.
        2. Attempt to delete photos without proper authentication and verify the system's response.
        3. Test for edge cases such as deleting photos with invalid IDs.
    - **Tools/Techniques**: Test automation frameworks, API testing tools.

3. **End-to-End (E2E) Tests:**
    - **Test Cases**:
        1. Simulate the end-to-end flow of deleting a photo, including login, navigation to the delete photo page, and deletion.
        2. Verify that only authorized users can access the delete photo functionality.
    - **Tools/Techniques**: Test automation frameworks, browser automation tools.

4. **Penetration Tests:**
    - **Objective**: Test for vulnerabilities such as insecure direct object references and insufficient access controls.
    - **Approach**: Attempt to manipulate photo IDs to access other users' photos and test for unauthorized deletion.
    - **Tools/Techniques**: Manual testing, OWASP ZAP.

### Use Case: Browse Users

#### Test Planning:

1. **Unit Tests:**
    - **Test Cases**:
        1. Verify that the search functionality returns accurate results based on input criteria.
        2. Test filtering mechanisms to ensure proper data retrieval.
        3. Validate error handling for invalid search queries.
    - **Tools/Techniques**: Mocking frameworks for database interactions, assertion libraries for result validation.

2. **Functional Tests:**
    - **Test Cases**:
        1. Validate that users can search for other users by various criteria (e.g., name, email).
        2. Test pagination functionality to handle large datasets.
        3. Verify that sensitive information is not exposed through search results.
    - **Tools/Techniques**: Test automation frameworks, API testing tools.

4. **Penetration Tests:**
    - **Objective**: Test for vulnerabilities such as data exposure.
    - **Tools/Techniques**: Manual testing.

    
### Use Case: Put up a Photo for Sale

#### Test Planning:

1. **Unit Tests:**
    - **Test Cases**:
        1. Verify the Photo Update to turn into active.
        2. Verify if the photo Goes to a Portfolio.
    - **Tools/Techniques**: Mocking frameworks for authentication and access control. Mock of service to add the photo.

2. **Functional Tests:**
    - **Test Cases**:
        1. Validate that a photographer can acess his portfolio.
        2. Attempt a valid and non-valid attempt of adding a photo to a portfolio.
    - **Tools/Techniques**: Test automation frameworks, API testing tools.

4. **Penetration Tests:**
    - **Objective**: Test for vulnerabilities such as SQL injections.
    - **Approach**: Use SQL automated tools to check if there is vulnerabilities.
    - **Tools/Techniques**: Manual testing, SQLMap.

![sqlMapPutUpforSale.png](../images/sqlmapPutupforsale.png)  

<p align="center">SQLMap for Put up a Photo for Sale</p>

![PutPhotoUpForSaleZAP.png](../images/PutPhotoUpForSaleZAP.png)
<p align="center">OWASP ZAP Report for Put Photo up for sale</p>

As we can see in the [report](../ZAP-Reports/PutPhotoUpForSale.html) there are some vulnerabilities that need to be fixed in the Create Portfolio use case.
In particular, one high-risk vulnerability was identified: Open Redirect.
This vulnerability can be exploited by attackers to redirect users to malicious websites, potentially leading to phishing attacks or other security threats.
It is crucial to address this vulnerability promptly to prevent potential exploitation and protect user security.
Other vulnerabilities identified in the report include Absence of Anti-CSRF Tokens and CSP: Wildcard Directive, among others.


### Use Case: View Purchased Photos

1. **Unit Tests:**
    - **Test Cases**:
        1. Verify that the search functionality returns accurate results based on the requesting user and on search parameters.
        2. Test filtering mechanisms to ensure proper data retrieval.
        3. Validate error handling for invalid search queries.
    - **Tools/Techniques**: Mocking frameworks for database interactions, assertion libraries for result validation.

2. **Functional Tests:**
    - **Test Cases**:
        1. Validate that users can filter their search by various criteria (e.g., photo name, photographer, portfolio).
        2. Test pagination functionality to handle large datasets.
        3. Ensure the requesting user is currently active.
    - **Tools/Techniques**: Test automation frameworks, API testing tools.

3. **End-to-End (E2E) Tests:**
    - **Test Cases**:
        1. Simulate the end-to-end flow of browsing purchased photos, including navigating to the search page, entering search criteria, and viewing results.
        2. Test for performance under high request volumes.
    - **Tools/Techniques**: Automatic browser testing frameworks, load testing tools.

4. **Penetration Tests:** 
   - **Test Cases**:
        1. Test for vulnerabilities such as SQL injection and data exposure. Approach: Attempt to inject SQL queries into search parameters and verify system responses.
        2. Simulate a malicious attack to assess the application's resilience. Approach: Use an automated tool to simulate an attack.
   - **Tools/Techniques**: SQLMap, OWASP ZAP.


![ViewPurchasedPhotosZAP.png](../images/ViewPurchasedPhotosZAP.png)
<p align="center">OWASP ZAP Report for View Purchased Photos</p>

As we can see in the [report](../ZAP-Reports/ViewGallery.html) there are some vulnerabilities that need to be fixed in the Create Portfolio use case.
Similar to other use cases, this report also highlights the high-risk vulnerability of Open Redirect.
Other alerts are the medium-risk CSP: script-src unsafe-inline, the low-risk Cookie without SameSite Attribute and the informational Information Disclosure - Suspicious Comments.


### ASVS Compliance

| ASVS Level | Requirement | Verification Requirement                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Tool Used                                                                                                                                                                                                      |
|------------|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Level 1    | 2.1.1       | Verify that user set passwords are at least 12 characters in length (after multiple spaces are combined). ([C6](https://owasp.org/www-project-proactive-controls/#div-numbering))                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | DFD and Security Requirements Engineering                                                                                                                                                                      |
| Level 1    | 2.1.2       | Verify that passwords of at least 64 characters are permitted, and that passwords of more than 128 characters are denied. ([C6](https://owasp.org/www-project-proactive-controls/#div-numbering))                                                                                                                                                                                                                                                                                                                                                                                                                                                    | DFD and Security Requirements Engineering                                                                                                                                                                      |
| Level 2    | 2.9.3       | Verify that approved cryptographic algorithms are used in the generation, seeding, and verification.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | Security Requirements Engineering                                                                                                                                                                              |
| Level 2    | 9.2.2       | Verify that encrypted communications such as TLS is used for all inbound and outbound connections, including for management ports, monitoring, authentication, API, or web service calls, database, cloud, serverless, mainframe, external, and partner connections. The server must not fall back to insecure or unencrypted protocols.                                                                                                                                                                                                                                                                                                             | Deployment Diagram                                                                                                                                                                                             |
| Level 1    | 10.1.1      | Verify that a code analysis tool is in use that can detect potentially malicious code, such as time functions, unsafe file operations and network connections.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | The usage of the Sonarqube for the code coverage and code vulnerabilities.                                                                                                                                     |
| Level 1    | 12.1.1      | Verify that the application will not accept large files that could fill up storage or cause a denial of service.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Abuse cases                                                                                                                                                                                                    |
| Level 2    | 10.2.1      | Verify that the application source code and third party libraries do not contain unauthorized phone home or data collection capabilities. Where such functionality exists, obtain the user's permission for it to operate before collecting any data.                                                                                                                                                                                                                                                                                                                                                                                                | Evidence in DFD & [Malicious code search](#malicious-code-search)                                                                                                                                              |
| Level 2    | 1.8.1       | Verify that all sensitive data is identified and classified into protection levels.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | Evidence in Views & [Data protection and Privacy Architecture](#data-protection-and-privacy-architecture)                                                                                                      |
| Level 2    | 1.4.4       | Verify the application uses a single and well-vetted access control mechanism for accessing protected data and resources. All requests must pass through this single mechanism to avoid copy and paste or insecure alternative paths. ([C7](https://owasp.org/www-project-proactive-controls/#div-numbering))                                                                                                                                                                                                                                                                                                                                        | [Access Control Architecture](#access-control-architecture)                                                                                                                                                    |
| Level 2    | 1.14.4      | Verify that the build pipeline contains a build step to automatically build and verify the secure deployment of the application, particularly if the application infrastructure is software defined, such as cloud environment build scripts.                                                                                                                                                                                                                                                                                                                                                                                                        | Deployment Diagram & [Implementation of Automated Security Checks in Build Pipeline](#implementation-of-automated-security-checks-in-build-pipeline)                                                           |
| Level 1    | 2.1.5       | Verify users can change their password.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | [Implementation of Password Change Functionality](#implementation-of-password-change-functionality)                                                                                                            |
| Level 1    | 4.1.2       | Verify that all user and data attributes and policy information used by access controls cannot be manipulated by end users unless specifically authorized.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | Evidence in DFD & [Implementation of Attribute-Based Access Control (ABAC) with Immutable User and Data Attributes](#implementation-of-attribute-based-access-control-with-immutable-user-and-data-attributes) |
| Level 1    | 4.3.1       | Verify administrative interfaces use appropriate multi-factor authentication to prevent unauthorized use.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | Abuses Cases & [Implementation of Multi-Factor Authentication (MFA) for Administrative Interfaces](#implementation-of-multi-factor-authentication-for-administrative-interfaces)                               |
| Level 1    | 8.3.1       | Verify that sensitive data is sent to the server in the HTTP message body or headers, and that query string parameters from any HTTP verb do not contain sensitive data.                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Abuse Cases & [Implementation of Secure Data Transmission Practices](#implementation-of-secure-data-transmission-practices)                                                                                    |
| Level 1    | 13.2.1      | Verify that enabled RESTful HTTP methods are a valid choice for the user or action, such as preventing normal users using DELETE or PUT on protected API or resources.                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | [Implementation of Role-Based Access Control for RESTful API Methods](#implementation-of-role-based-access-control-for-restful-api-methods)                                                                    |
| Level 1    | 4.1.3       | Verify that the principle of least privilege exists - users should only be able to access functions, data files, URLs, controllers, services, and other resources, for which they possess specific authorization. This implies protection against spoofing and elevation of privilege. ([C7](https://owasp.org/www-project-proactive-controls/#div-numbering))                                                                                                                                                                                                                                                                                       | Abuse Case (EditUserRolesAbuseCase)                                                                                                                                                                            |
| Level 2    | 1.1.2       | Verify the use of threat modeling for every design change or sprint planning to identify threats, plan for countermeasures, facilitate appropriate risk responses, and guide security testing.                                                                                                                                                                                                                                                                                                                                                                                                                                                       | [Threat-Modeling](#threat-modeling)                                                                                                                                                                            |
| Level 1    | 8.3.4       | Verify that all sensitive data created and processed by the application has been identified, and ensure that a policy is in place on how to deal with sensitive data. ([C8](https://owasp.org/www-project-proactive-controls/#div-numbering))                                                                                                                                                                                                                                                                                                                                                                                                        | Logical View Diagram (Using Dto to send only required data)                                                                                                                                                    |
| Level 2    | 1.11.1      | Verify the definition and documentation of all application components in terms of the business or security functions they provide.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Use Cases shows business logic of our application                                                                                                                                                              |
| Level 1    | 11.1.5      | Verify the application has business logic limits or validation to protect against likely business risks or threats, identified using threat modeling or similar methodologies.                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | [Threat-Modeling](#threat-modeling) documentation, design use and abuse cases                                                                                                                                  |
| Level 1    | 5.1.3       | Verify that all input (HTML form fields, REST requests, URL parameters, HTTP headers, cookies, batch files, RSS feeds, etc) is validated using positive validation (allow lists). ([C5](https://owasp.org/www-project-proactive-controls/#div-numbering))                                                                                                                                                                                                                                                                                                                                                                                            | In the Process views level 3 the parameters that each requests receive are validated using allowed list                                                                                                        |
| Level 1    | 14.4.1      | Verify that every HTTP response contains a Content-Type header. Also specify a safe character set (e.g., UTF-8, ISO-8859-1) if the content types are text/*, /+xml and application/xml. Content must match with the provided Content-Type header.                                                                                                                                                                                                                                                                                                                                                                                                    | Security Requirements Engineering. See [HTTP Response Headers](#http-response-headers).                                                                                                                        |
| Level 1    | 13.1.3      | Verify API URLs do not expose sensitive information, such as the API key, session tokens etc.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | Security Requirements Engineering. See [API URL Structure](#api-url-structure).                                                                                                                                |
| Level 2    | 13.1.5      | Verify that requests containing unexpected or missing content types are rejected with appropriate headers (HTTP response status 406 Unacceptable or 415 Unsupported Media Type).                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Security Requirements Engineering. See [HTTP Response Headers](#http-response-headers).                                                                                                                        |
| Level 2    | 12.1.3      | Verify that a file size quota and maximum number of files per user is enforced to ensure that a single user cannot fill up the storage with too many files, or excessively large files.                                                                                                                                                                                                                                                                                                                                                                                                                                                              | Threat Modeling: [Abuse Cases](#abuse-cases) (Put a Photo up for sale).                                                                                                                                        |
| Level 2    | 8.3.6       | Verify that sensitive information contained in memory is overwritten as soon as it is no longer required to mitigate memory dumping attacks, using zeroes or random data.                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | Security Requirements Engineering. See [Data Protection](#Security--Requirements).                                                                                                                             |