

# High-Level System Architecture Diagram (4+1 Views Architecture)

## Domain Model

The concept of this project consists in a shop that has clients and has photographers, in which the photographers can have portfolios to sell their photos to the clients. The clients can buy photos from the portfolios and add them to their cart. The clients can also have a cart to add photos to it and then checkout the cart to buy the photos.

![Domain Model.jpg](Domain%20Model.jpg)

## Class Diagram

![Class Diagram](image/CD.png)

## Logical View:

Description:  

The logical view shows the app's architecture from above. It focuses on how the 
parts are organized and interact conceptually. The logical view highlights 
the system's structure, functions, and how different parts work together.

### Level 2:

![LogicalViewLevel2.png](image%2FLevel2%2FLogicalViewLevel2.png)

## Process View:

In this analysis, we will examine various process views within the system, each representing distinct functionalities and user interactions. The process views to be analyzed are as follows:

| Process                      |
|------------------------------|
| **Create a Portfolio**       |
| **Put a Photo up for sale**  |
| **Sign Up**                  |
| **Login**                    |
| **Browse Photos**            |
| **Add a photo to cart**      |
| **Checkout Cart**            |
| **View Purchased Photos**    |
| **Suspend a User**           |
| **Edit a User's Role**       |
| **Delete a photo**           |
| **Browse Users**             |

### Use Case Diagram

The following Use Case Diagram shows the various use cases and which user roles are allowed perform them.

![Use Case Diagram](image/UseCaseDiagram.svg)

### Level 1:

#### Add Photo to Cart
![Add Photo to Cart](image/Level1/ProcessView/AddPhotoToCart.png)

#### Add Photo to Portfolio
![Add Photo to Portfolio](image/Level1/ProcessView/AddPhotoToPortfolio.png)

#### BrowseUsers
![BrowseUsers](image/Level1/ProcessView/BrowseUsers.png)

#### Checkout Cart
![Checkout Cart](image/Level1/ProcessView/CheckoutCart.png)

#### Create Portfolio
![Create Portfolio](image/Level1/ProcessView/CreatePortfolio.png)

#### Deactivate User
![Deactivate User](image/Level1/ProcessView/DeactiveUser.png)

#### Delete Photo
![Delete Photo](image/Level1/ProcessView/DeletePhoto.png)

#### Edit User Roles
![Edit User Roles](image/Level1/ProcessView/EditUserRoles.svg)

#### View Purchased Photos
![View Purchased Photos](image/Level1/ProcessView/ViewPurchasedPhotos.svg)

### Level 2:

#### Login
![Login](Level2/ProcessView/LoginL2.svg)

#### Browse Photos
![BrowsePhotos](Level2/ProcessView/BrowsePhotosL2.svg)

#### Add Photo to Cart
![AddPhotoToCart.png](image%2FLevel2%2FProcessView%2FAddPhotoToCart.png)

#### Create Portfolio
![CreatePortfolio.png](image%2FLevel2%2FProcessView%2FCreatePortfolio.png)

#### Add Photo to Portfolio
![Add Photo to Portfolio](image/Level2/ProcessView/AddPhotoToPortfolio.png)

#### Browse Users
![Browse Users](image/Level2/ProcessView/AddPhotoToPortfolio.png)

#### Checkout Cart
![Checkout Cart](image/Level2/ProcessView/CheckoutCart.png)

#### Deactivate User
![Deactivate User](image/Level2/ProcessView/DeactivateUser.png)

#### Delete Photo
![Delete Photo](image/Level2/ProcessView/DeletePhoto.png)

#### Edit User Roles
![Edit User Roles](image/Level2/ProcessView/EditUserRoles.svg)

#### View Purchased Photos
![View Purchased Photos](image/Level2/ProcessView/ViewPurchasedPhotos.svg)

### Level 3:

#### Login
In this process view, the user is able to login to the system. The user is required to enter their username and password. The system will then verify the user's credentials and grant access to the system if the credentials are correct. If the credentials are incorrect, the system will display an error message and prompt the user to try again.
Also, the user needs to have specific requirements in order to login, such as the user must be registered in the system and the user must have a valid username and password.
![Login](Level3/ProcessView/LoginL3.svg)

#### Browse Photos
![BrowsePhotos](Level3/ProcessView/BrowsePhotosL3.svg)

#### Add Photo to Cart
![AddPhotoToCart.png](image%2FLevel3%2FProcessView%2FAddPhotoToCart.png)

#### Add Photo to Portfolio
![Add Photo to Portfolio](image/Level3/ProcessView/AddPhotoToPortfolio.png)

#### Browse Users
![Browse Users](image/Level3/ProcessView/BrowseUsers.png)

#### Checkout Cart
![Checkout Cart](image/Level3/ProcessView/CheckoutCart.png)

#### Create Portfolio
![Create Portfolio](image/Level3/ProcessView/CreatePortfolio.png)

#### Deactivate User
![Deactivate User](image/Level3/ProcessView/AddPhotoToPortfolio.png)

#### Delete Photo
![Delete Photo](image/Level3/ProcessView/DeletePhoto.png)

#### Edit User Roles
![Edit User Roles](image/Level3/ProcessView/EditUserRoles.svg)

#### View Purchased Photos
![Edit User Roles](image/Level3/ProcessView/ViewPurchasedPhotos.svg)

## Implementation View:

![Implementation View](image/VistaImplementacao.png)

## Deployment View:

This is the deployment view of the system when the commit is applied to the master branch, in which the Github actions will then build and Execute tests in order to check if the project is ready for production.

![Deployment View](image/DeploymentView.png)

And then we have the deployment diagram, which shows the API communicating through HTTP/S.
In here, as TLS is compatible with HTTP/S, we will communicate through secure channels.
![Deployment Diagram1.jpg](Deployment%20Diagram1.jpg)

