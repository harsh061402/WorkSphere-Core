#  WorkSphere – Smart Employee Management System

A production-grade REST API backend built with Spring Boot and MongoDB.

## Tech Stack
- Java 17, Spring Boot 4.0.4
- MongoDB, Spring Data MongoDB
- Spring Security, BCrypt
- Lombok, Maven

## Features
- User management with role-based access (Admin, Employee)
- Employee profile management with soft delete
- Leave management — apply, approve, reject, cancel
- 5 leave types with balance tracking and overlap detection
- 15+ REST API endpoints with centralized exception handling

## How to Run
1. Make sure MongoDB is running on localhost:27017
2. Clone the repo
3. Run: `mvn spring-boot:run`
