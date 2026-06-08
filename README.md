# 🌐 WorkSphere — Smart Employee Management System

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.5-green?style=for-the-badge&logo=springboot)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-brightgreen?style=for-the-badge&logo=mongodb)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-blue?style=for-the-badge&logo=springsecurity)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-yellow?style=for-the-badge&logo=swagger)
![License](https://img.shields.io/badge/License-MIT-red?style=for-the-badge)

---

## 📖 Overview

**WorkSphere** is a production-grade **Employee Management System** REST API built with **Java 21**, **Spring Boot 4.x**, and **MongoDB Atlas**. It provides a complete HR management solution with role-based access control, JWT authentication, attendance tracking, leave management, and email notifications.

> 🔗 **GitHub:** [https://github.com/harsh061402/WorkSphere-Core](https://github.com/harsh061402/WorkSphere-Core)

---

## ✨ Features

### 🔐 Authentication & Security
- JWT-based stateless authentication
- Role-based access control (ADMIN / EMPLOYEE)
- Password reset via email (Mailtrap / SMTP)
- BCrypt password encoding
- Account status management (ACTIVE, PENDING, SUSPENDED, DELETED)
- Grace period based late arrival tracking

### 👥 User Management
- User registration and login
- Role assignment (ADMIN / EMPLOYEE)
- User status management
- Last login date tracking

### 👨‍💼 Employee Management
- Complete employee profile management
- Department and designation tracking
- Salary management
- Address management (current & permanent)
- Soft delete support
- Dynamic search and filter (by name, mobile, designation, department, status)

### 🏖️ Leave Management
- 5 leave types: CASUAL, SICK, UNPAID, ANNUAL, MATERNITY
- Leave allocation per employee per year
- Leave request with overlap detection
- Approve / Reject / Cancel leave requests
- Automatic leave balance deduction and restoration
- Email notifications on approval/rejection

### 🕐 Attendance Management
- Clock in / Clock out system
- Grace period support (configurable)
- Late arrival tracking
- Automatic status calculation (PRESENT, HALF_DAY, INCOMPLETE)
- Overtime calculation
- Date range based attendance reports

### 📧 Email Notifications
- Async email sending (@Async)
- Leave approval/rejection notifications
- Password reset emails

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Core language |
| Spring Boot | 4.0.5 | Application framework |
| Spring Security | 7.0.4 | Authentication & Authorization |
| Spring Data MongoDB | 5.0.4 | Database ORM |
| MongoDB Atlas | Latest | Cloud database |
| JJWT | 0.13.0 | JWT token generation & validation |
| Spring Mail | 4.0.5 | Email notifications |
| SpringDoc OpenAPI | 3.0.2 | API documentation (Swagger) |
| Lombok | 1.18.44 | Boilerplate reduction |
| Maven | 3.9.x | Build tool |

---

## 📁 Project Structure

```
src/main/java/com/harshkumar0614jain/worksphere/
├── config/
│   ├── MongoConfig.java          # MongoDB configuration
│   ├── SecurityConfig.java       # Spring Security + JWT config
│   └── SwaggerConfig.java        # OpenAPI documentation config
├── controller/
│   ├── AuthController.java       # Login, Register, Forgot/Reset Password
│   ├── UserController.java       # User CRUD operations
│   ├── EmployeeController.java   # Employee CRUD + Search
│   ├── LeaveAllocationController.java
│   ├── LeaveRequestController.java
│   └── AttendanceController.java
├── entity/
│   ├── User.java
│   ├── Employee.java
│   ├── LeaveAllocation.java
│   ├── LeaveRequest.java
│   ├── Attendance.java
│   └── PasswordResetToken.java
├── enums/
│   ├── Role.java                 # ADMIN, EMPLOYEE
│   ├── UserStatus.java           # PENDING, ACTIVE, INACTIVE, SUSPENDED, DELETED
│   ├── EmployeeStatus.java       # ACTIVE, INACTIVE, DELETED
│   ├── Department.java           # DEVELOPER, TESTER, HR, etc.
│   ├── LeaveType.java            # CASUAL, SICK, UNPAID, ANNUAL, MATERNITY
│   ├── LeaveStatus.java          # PENDING, APPROVED, REJECTED, CANCELLED
│   └── AttendanceStatus.java     # PRESENT, HALF_DAY, INCOMPLETE
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── AuthEntryPoint.java
│   ├── BusinessException.java
│   ├── ResourceNotFoundException.java
│   └── ResourceAlreadyExistsException.java
├── filter/
│   └── JwtAuthFilter.java        # JWT request filter
├── model/                        # Request/Response DTOs
├── repository/                   # MongoDB repositories
├── seeder/
│   └── DataSeeder.java           # Default admin user seeder
├── service/                      # Business logic
└── util/
    └── JwtUtil.java              # JWT utility
```

---

## 📡 API Endpoints

### 🔐 Authentication (`/api/auth`)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register new user | ❌ |
| `POST` | `/api/auth/login` | Login and get JWT token | ❌ |
| `POST` | `/api/auth/forgot-password` | Send password reset email | ❌ |
| `POST` | `/api/auth/reset-password` | Reset password using token | ❌ |

### 👥 User Management (`/api/users`)

| Method | Endpoint | Description | Role |
|---|---|---|---|
| `GET` | `/api/users` | Get all users | ADMIN |
| `GET` | `/api/users/{id}` | Get user by ID | ADMIN |
| `POST` | `/api/users` | Create new user | ADMIN |
| `PATCH` | `/api/users/{id}` | Update user | ADMIN |
| `DELETE` | `/api/users/{id}` | Delete user (soft) | ADMIN |

### 👨‍💼 Employee Management (`/api/employees`)

| Method | Endpoint | Description | Role |
|---|---|---|---|
| `GET` | `/api/employees` | Get all employees | ADMIN |
| `GET` | `/api/employees/{id}` | Get employee by ID | ADMIN |
| `GET` | `/api/employees/department/{dept}` | Get by department | ADMIN |
| `GET` | `/api/employees/search` | Search & filter employees | ADMIN |
| `POST` | `/api/employees` | Create employee | ADMIN |
| `PATCH` | `/api/employees/{id}` | Update employee | ADMIN |
| `DELETE` | `/api/employees/{id}` | Delete employee (soft) | ADMIN |

### 🏖️ Leave Allocation (`/api/leave-allocations`)

| Method | Endpoint | Description | Role |
|---|---|---|---|
| `POST` | `/api/leave-allocations` | Allocate leaves to employee | ADMIN |
| `GET` | `/api/leave-allocations/{employeeId}` | Get allocations by employee | ADMIN / EMPLOYEE |

### 📝 Leave Requests (`/api/leave-requests`)

| Method | Endpoint | Description | Role |
|---|---|---|---|
| `POST` | `/api/leave-requests` | Apply for leave | EMPLOYEE |
| `GET` | `/api/leave-requests` | Get all leave requests | ADMIN |
| `GET` | `/api/leave-requests/{id}` | Get leave request by ID | ADMIN / EMPLOYEE |
| `GET` | `/api/leave-requests/employee/{id}` | Get leaves by employee | ADMIN / EMPLOYEE |
| `PATCH` | `/api/leave-requests/{id}/decision` | Approve or reject leave | ADMIN |
| `PATCH` | `/api/leave-requests/{id}/cancel` | Cancel leave request | ADMIN / EMPLOYEE |

### 🕐 Attendance (`/api/attendance`)

| Method | Endpoint | Description | Role |
|---|---|---|---|
| `POST` | `/api/attendance/clock-in` | Clock in for today | EMPLOYEE |
| `POST` | `/api/attendance/clock-out` | Clock out for today | EMPLOYEE |
| `GET` | `/api/attendance/{employeeId}` | Get all attendance | ADMIN / EMPLOYEE |
| `GET` | `/api/attendance/{employeeId}/range` | Get attendance by date range | ADMIN / EMPLOYEE |

---

## ⚙️ Setup & Installation

### Prerequisites

- Java 21+
- Maven 3.9+
- MongoDB Atlas account (free tier works)
- Mailtrap account (for email testing)

### Steps

**1 — Clone the repository:**
```bash
git clone https://github.com/harsh061402/WorkSphere-Core.git
cd WorkSphere-Core
```

**2 — Create `application-local.properties` in `src/main/resources/`:**
```properties
MONGODB_URI=mongodb+srv://username:password@cluster/WorkSphere?appName=Cluster0
JWT_SECRET=your-base64-encoded-secret-key
JWT_EXP_TIME=1800000
MAIL_USERNAME=your-mailtrap-username
MAIL_PASSWORD=your-mailtrap-password
```

**3 — Build the project:**
```bash
mvn clean install
```

**4 — Run the application:**
```bash
mvn spring-boot:run
```

**5 — Access Swagger UI:**
```
http://localhost:8080/swagger-ui/index.html
```

**6 — Login with default admin:**
```json
{
    "username": "admin",
    "password": "Harsh@123"
}
```

---

## 🌍 Environment Variables

| Variable | Description | Example |
|---|---|---|
| `MONGODB_URI` | MongoDB Atlas connection string | `mongodb+srv://user:pass@cluster/db` |
| `JWT_SECRET` | Base64 encoded JWT secret (min 256 bits) | `V29ya1NwaGVyZUA...` |
| `JWT_EXP_TIME` | JWT expiration in milliseconds | `1800000` (30 mins) |
| `MAIL_USERNAME` | Mailtrap SMTP username | `abc123` |
| `MAIL_PASSWORD` | Mailtrap SMTP password | `xyz456` |

---

## 📧 Email Configuration

WorkSphere uses **Mailtrap** for development email testing. All emails are captured in the Mailtrap inbox without reaching real users.

**To configure:**
1. Create a free account at [mailtrap.io](https://mailtrap.io)
2. Go to **Email Sandbox → Inboxes**
3. Select your inbox → **SMTP Settings**
4. Copy credentials to `application-local.properties`

**For production**, replace Mailtrap credentials with your SMTP provider (Gmail, SendGrid, AWS SES etc.)

---

## 🔐 Security

- All endpoints except `/api/auth/**` and Swagger UI require JWT token
- Include token in request header:
```
Authorization: Bearer <your-jwt-token>
```
- Tokens expire after 30 minutes (configurable)
- Passwords are encoded using BCrypt
- Sensitive credentials are stored in environment variables

---

## 📊 Attendance Rules

| Rule | Value |
|---|---|
| Work start time | 10:00 AM |
| Grace period | 15 minutes |
| Standard hours | 8 hours |
| Half day threshold | 4 hours |
| Overtime starts after | 8 hours |

---

## 🗄️ Database Collections

| Collection | Description |
|---|---|
| `users` | Authentication and role data |
| `employees` | Employee profile and HR data |
| `leave_allocations` | Leave quota per employee per year |
| `leave_requests` | Leave request records |
| `attendance` | Daily clock in/out records |
| `password_reset_tokens` | Temporary tokens for password reset |

---

## 👨‍💻 Author

**Harsh Kumar Jain**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/harshkumar0614jain/)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?style=for-the-badge&logo=github)](https://github.com/harsh061402)

---

## 📄 License

This project is licensed under the MIT License.

---

> ⭐ If you found this project helpful, please give it a star on GitHub!
