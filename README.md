# Product catalog API
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00.svg?style=flat&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=flat&logo=flyway&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-E94E1B)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)
![MinIO](https://img.shields.io/badge/MinIO-C72E49?style=flat&logo=minio&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black)
![AWS SDK](https://img.shields.io/badge/AWS-SDK-FF9900)
[![MIT License](https://img.shields.io/badge/License-MIT-green)](./LICENSE)
### Challenge
A product catalog API, which stores images in a MinIO docker using the AWS SDK for S3.
#### Features
- User authentication with login/password and JWT token generation
- User and admin registration <br/>
[ADD DETAILS HERE]
- Global exception handling with standard response format
- Full API documentation via Swagger
- Role-based authorization (ADMIN and USER)
#### Class Diagram
``` mermaid
classDiagram
class User {
-UUID id
-String username
-String password
-String role
}
class Category {
-UUID id
-String name
-String description
}
class Product {
-UUID id
-String name
-String description
-Double price
-LocalDateTime createdAt
-String imageUrl
-Category category
}
Category "1"--> "*" Product
```
#### Entity Relationship Diagram
``` mermaid
erDiagram
USERS {
UUID id PK
VARCHAR(30) username
VARCHAR(200) password
String role
}
CATEGORIES ||--o{ PRODUCTS : contain
CATEGORIES {
UUID id PK
VARCHAR(100) name
VARCHAR(200) description
}
PRODUCTS {
UUID id PK
VARCHAR(100) name
VARCHAR(200) description
DECIMAL price "DECIMAL(10,2)"
TIMESTAMP created_at
VARCHAR image_url
UUID category_id FK
}
```
#### Package Structure
[ADD HERE]
#### Authentication and Authorization
- **Login** returns a JWT token valid for 2 hours
- Protected endpoints require the token in `Authorization: Bearer <token>`
#### Swagger Documentation
Once the application is running, access:
http://localhost:8080/docs
#### Testing
- Unit and integration tests for controllers
- Coverage of expected HTTP statuses (200, 400, 401, 403, 404, etc.)
## Running the Project
1. Clone the repository:
```bash
git clone https://github.com/samanthamaiaduarte/todo-api.git
```
2. Create a PostgreSQL database (e.g., `todoapi`)
3. Configure your database settings in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
```
4. Run the application
```bash
./mvnw spring-boot:run
```
The API will be available at http://localhost:8080
## Deployment
You can deploy this project on:
- Render.com
- Railway
- Fly.io
- Heroku (using JDK 21 buildpack)
  ⚠️ Be sure to configure environment variables like the token secret and database credentials on the platform's settings.
## Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request to the repository.
When contributing to this project, please follow the existing code style, [commit conventions](https://www.conventionalcommits.org/en/v1.0.0/), and submit your changes in a separate branch.

---
### Author
Made with ❤️ by Samantha Maia Duarte
contact@samanthamaiaduarte.com