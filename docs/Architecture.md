

# High-Level System Architecture Diagram (4+1 Views Architecture)
## Logical View:  
Description:  
(TO DO)
## Process View:

In this analysis, we will examine various process views within the system, each representing distinct functionalities and user interactions. The process views to be analyzed are as follows:

| Process                      |
|------------------------------|
| **Create a Portfolio**      |
| **Put a Photo up for sale** |
| **Sign Up**                  |
| **Login**                    |
| **Browse Photos**            |
| **Add a photo to cart**      |
| **Checkout Cart**            |
| **View Purchased Photos**    |
| **Suspend a User**           |
| **Edit a User's Role**      |
| **Delete a photo**           |
| **Browse Users**             |


### Level 2:

#### Login
![Login](Level2/ProcessView/LoginL2.svg)

#### Browse Photos
![BrowsePhotos](Level2/ProcessView/BrowsePhotosL2.svg)

#### Create a Portfolio
![CreatePortfolio.png](image%2FLevel2%2FProcessView%2FCreatePortfolio.png)

#### Add Photo to Cart
![AddPhotoToCart.png](image%2FLevel3%2FProcessView%2FAddPhotoToCart.png)


### Level 3:

#### Login
In this process view, the user is able to login to the system. The user is required to enter their username and password. The system will then verify the user's credentials and grant access to the system if the credentials are correct. If the credentials are incorrect, the system will display an error message and prompt the user to try again.
Also the user needs to have specific requirements in order to login, such as the user must be registered in the system and the user must have a valid username and password.
![Login2](Level3/ProcessView/LoginL3.svg)

#### Browse Photos
![BrowsePhotos2](Level3/ProcessView/BrowsePhotosL3.svg)

#### Create a Portfolio
![CreatePortfolio.png](image%2FLevel3%2FProcessView%2FCreatePortfolio.png)

#### Add Photo to Cart
![AddPhotoToCart.png](image%2FLevel3%2FProcessView%2FAddPhotoToCart.png)

## Implementation View:



## Deployment View:

## Data Flow Diagram

In the Data flow Diagram we Divided the diagrams between the processes in order to clarify the flow of data of the processes in question.
### Login Register DFD
![Login_RegisterDFD.drawio.svg](DFD%2FLogin_RegisterDFD.drawio.svg)
### Create Portfolio DFD
![Create a Portfolio.drawio.svg](DFD%2FCreate%20a%20Portfolio.drawio.svg)

### Browse Photos DFD
![BrowsePhoto.drawio.svg](DFD%2FBrowsePhoto.drawio.svg)
### Add to Cart DFD
![AddToCart.drawio.svg](DFD%2FAddToCart.drawio.svg)
### Put a Photo up for sale DFD
![AddPhotoForSale.drawio.svg](DFD%2FAddPhotoForSale.drawio.svg)
