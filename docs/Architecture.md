

# High-Level System Architecture Diagram (4+1 Views Architecture)

## Domain Model


![Domain Model.jpg](Domain%20Model.jpg)

## Logical View:

Description:  

The logical view shows the app's architecture from above. It focuses on how the 
parts are organized and interact conceptually. The logical view highlights 
the system's structure, functions, and how different parts work together.

### Level 2:

![LogicalViewLevel2.png](image%2FLevel2%2FLogicalViewLevel2.png)

### Level 4:

![Class Diagram](image/CD.png)

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

![Deployment View](image/DeploymentView.png)

