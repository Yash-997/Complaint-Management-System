# Complaint Management System - Backend

A Spring Boot backend application for managing user complaints with JWT Authentication and Role-Based Authorization.

This backend provides REST APIs for users and admins to manage complaints efficiently.

---

# Features

## Authentication
- User Registration
- User Login
- JWT Authentication
- Role-Based Authorization
- Spring Security Integration

---

# User Features
- Create Complaint
- View Own Complaints
- Edit Own Complaints
- Delete Own Complaints
- Track Complaint Status

---

# Admin Features
- View All Complaints
- Update Complaint Status
- Manage Complaint Workflow

---

# Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT
- Hibernate / JPA
- PostgreSQL
- Maven

---

# Project Structure

```bash
src/main/java/com/yash/Complaint_Management_System
│
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
└── service
```

---

# API Endpoints

## User APIs

### Register User
```http
POST /api/users/register
```

### Login User
```http
POST /api/users/login
```

### Get Current User
```http
GET /api/users/me
```

---

## Complaint APIs

### Create Complaint
```http
POST /api/complaints
```

### Get User Complaints
```http
GET /api/complaints/my
```

### Update Complaint
```http
PUT /api/complaints/{id}
```

### Delete Complaint
```http
DELETE /api/complaints/{id}
```

---

## Admin APIs

### Get All Complaints
```http
GET /api/admin/complaints
```

### Update Complaint Status
```http
PUT /api/admin/complaints/{id}/status
```

---

# JWT Authentication Flow

1. User logs in
2. JWT token generated
3. Token returned to frontend
4. Frontend sends token in Authorization Header
5. JwtFilter validates token
6. User authenticated successfully

---

# Database Configuration

Environment variables are used for secure configuration.

```properties
spring.datasource.url=${DB_URL}

spring.datasource.username=${DB_USERNAME}

spring.datasource.password=${DB_PASSWORD}

app.jwt.secret=${JWT_SECRET}
```

---

# Setup Instructions

## Clone Repository

```bash
git clone https://github.com/Yash-997/Complaint-Management-System.git
```

---

## Configure Environment Variables

Set these variables in IntelliJ Run Configuration:

```env
DB_URL=jdbc:postgresql://localhost:5432/complaint_db

DB_USERNAME=postgres

DB_PASSWORD=your_password

JWT_SECRET=your_secret_key
```

---

## Run Backend

```bash
mvn spring-boot:run
```

Backend runs on:

```bash
http://localhost:8080
```

---

# Future Improvements

- Email Notifications
- File Upload Support
- Complaint Categories
- Dashboard Analytics
- Docker Deployment

---

# Author

Yash Dabhade

Walchand College Of Engineering, Sangli
