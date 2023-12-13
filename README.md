# Used Cars Offers Management System

## Overview

This Spring Boot project is designed to manage offers for used cars. The application utilizes a PostgreSQL database for data storage and retrieval. It incorporates the Thymeleaf view engine for server-side rendering and includes a role-based security system using Spring Security.

## Features

- **User Roles:**
    - **User:** Can view and create offers.
    - **Administrator:** Has access to all functionalities, including user role management.

- **Database:**
    - Utilizes PostgresSQL for data storage.

- **View Engine:**
    - Thymeleaf is used for server-side rendering, providing dynamic and responsive user interfaces.

- **Security:**
    - Spring Security is implemented for role-based access control.

- **Validation:**
    - Form validation is applied to ensure data integrity.

- **Exception Handling:**
    - Business logic is wrapped with appropriate exception handling.

- **Testing:**
    - Achieves over 90% line coverage in tests.

## Technologies Used

- Spring Boot
- PostgresSQL
- Thymeleaf
- Spring Security
- JUnit
- Mockito

## Getting Started

### Prerequisites

- Java 17 or later
- Maven
- PostgresSQL

### Configuration

1. Clone the repository:

    ```
   git clone https://github.com/vmirrchev/2drive.git
    ```

2. Setup postgres database server
3. Set environment variables:

    ```
   DB_PASSWORD=<password>
   DB_USERNAME=<username>
   DB_URL=<jdbc url>
    ```
   
4. Run the application

    ```
    mvn spring-boot:run
    ```

5. Access the application at http://localhost:8081

## Usage

1. **User Access:**
- Log in as a user to create offers, view my offers and edit profile

2. **Administrator Access:**
  - Log in as an administrator to create engines, models and manage user roles

## Testing

To run tests, execute the following command:

  ```
  mvn test
  ```
## License

This project is licensed under the [MIT License](LICENSE).