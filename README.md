# Spring Boot & MongoDB  REST API

A basic CRUD API using Spring Boot with MongoDB.
The application will contain user management and product management functionalities.

## Prerequisites

- [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/javase-downloads.html) or higher installed
- [MongoDB] You can you local or cloud version

To install local version of mongodb on Mac you can use homebrew:

```shell
# Enter the latest version instead of <mongodb-community>
brew tap mongodb/brew
brew install <mongodb-community>
brew services start <mongodb-community>

#To stop service run
brew services start <mongodb-community>
```

## Getting Started

1. Clone this repository:

   ```shell
   git clone git@github.com:ManWhoSoldTheW0rld/letsplay.git

2. Change to the project directory:

   ```shell
   cd letsplay
   ```

3. Configure MongoDB connection in `src/main/resources/application.properties`:

4. Build and run the application:

   ```shell
   ./gradlew bootRun
   ```

5. The application will be accessible at [https://localhost:8443/].

## API Endpoints

### Registration and Authentication
- **POST /api/v1/auth/register**

with JSON raw Body
```shell
   {
       "name": <name>,
       "email": <email>,
       "password": <password>,
       "role" : <USER|ADMIN>
   }
```
Responds with a valid JWT

- **POST /api/v1/auth/authenticate**

with JSON raw Body
```shell
   {
       "email": <email>,
       "password": <password>,
   }
```
Responds with a valid JWT

### Products
- **GET /api/v1/product/list** : Returns list of products
- **GET /api/v1/product/item/<id>** : Returns product with <id> info 

For requests below JWT authorization required
- **POST /api/v1/product/** : Adds a new product
- **PUT /api/v1/product/<id>** : Updates product with <id>
- **DELETE /api/v1/product/<id>** : Deletes product with <id>

For adding and updating product request should contain JSON raw Body
```shell
  {
    "name": <name>,
    "price" : <price>,
    "description" : <description>
}
```

### Users
For requests below JWT authorization required
- **GET /api/v1/user/list** : Returns list of users
- **GET /api/v1/user/<id>** : Returns user with <id> info

For requests below JWT authorization required
- **PUT /api/v1/user/<id>** : Updates user with <id>
- **DELETE /api/v1/user/<id>** : Deletes user with <id>

For updating user request should contain JSON raw Body
```shell
   {
       "name": <name>,
       "email": <email>,
       "password": <password>,
       "role" : <USER|ADMIN>
   }
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

You can copy and paste this updated template into your README.md file on GitHub. Don't forget to replace `<username>`, `<password>`, `<host>`, and `<database>` in the MongoDB connection string with your actual MongoDB credentials and database information.
