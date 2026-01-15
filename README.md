# Enrollment Management System

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-7.x-02303A)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A comprehensive web-based enrollment management system built with Spring Boot, designed to streamline student enrollment processes for educational institutions. This application provides separate dashboards for administrators and students, enabling efficient management of users, courses, classrooms, schedules, and enrollments.

## 🚀 Features

### Admin Dashboard
- **User Management**: Create, view, and manage student and admin accounts
- **Course Management**: Add and organize courses offered by the institution
- **Classroom Management**: Manage classroom resources and assignments
- **Schedule Management**: Create and maintain class schedules
- **Enrollment Oversight**: Monitor and manage student enrollments across courses

### Student Dashboard
- **Profile Management**: View and update personal information
- **Course Enrollment**: Browse available courses and enroll in classes
- **Schedule Viewing**: Access personal class schedules
- **Enrollment History**: Track past and current enrollments

### Security & Authentication
- Role-based access control (Admin/Student)
- Secure login system
- Password encryption and session management

## 🛠️ Technologies Used

- **Backend**: Java 17, Spring Boot 3.x
- **Build Tool**: Gradle
- **Database**: JPA/Hibernate (configurable via application.properties)
- **Frontend**: Thymeleaf templates, Tailwind CSS, PostCSS
- **Security**: Spring Security
- **Testing**: JUnit, Spring Boot Test

## 📋 Prerequisites

- Java 17 or higher
- Gradle 7.x or higher
- Node.js and npm (for frontend assets)
- A relational database (e.g., MySQL, PostgreSQL, H2 for development)

## 🔧 Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/enroll-management.git
   cd enroll-management
   ```

2. **Install frontend dependencies**:
   ```bash
   cd src/main/resources/static
   npm install
   ```

3. **Configure the database**:
   - Update `src/main/resources/application.properties` with your database settings:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/enrollment_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     ```

4. **Build the application**:
   ```bash
   ./gradlew build
   ```

5. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

The application will be available at `http://localhost:8080`.

## 📖 Usage

### Accessing the Application
- Navigate to `http://localhost:8080` in your web browser
- Use the login page to authenticate

### Default Credentials (for development)
- **Admin**: username: `admin`, password: `admin123`
- **Student**: username: `student`, password: `student123`

### Admin Operations
1. Log in with admin credentials
2. Access the admin dashboard from the sidebar
3. Manage users, courses, classrooms, schedules, and enrollments

### Student Operations
1. Log in with student credentials
2. View and update profile information
3. Browse and enroll in available courses
4. Check personal schedules and enrollment history

## 🏗️ Project Structure

```
enroll-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── enroll_management/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controllers/     # REST controllers
│   │   │       │   ├── admin/       # Admin-specific endpoints
│   │   │       │   ├── auth/        # Authentication endpoints
│   │   │       │   ├── common/      # Shared endpoints
│   │   │       │   └── student/     # Student-specific endpoints
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── entities/        # JPA entities
│   │   │       ├── enums/           # Enumeration classes
│   │   │       ├── exception/       # Custom exceptions
│   │   │       ├── repositories/    # Data access layer
│   │   │       ├── security/        # Security configuration
│   │   │       └── services/        # Business logic layer
│   │   └── resources/
│   │       ├── application.properties  # Application configuration
│   │       ├── static/              # Static web assets
│   │       │   ├── css/             # Stylesheets
│   │       │   ├── images/          # Image assets
│   │       │   └── src/             # Source files for CSS/JS
│   │       └── templates/           # Thymeleaf templates
│   │           ├── admin/            # Admin pages
│   │           ├── auth/             # Authentication pages
│   │           ├── layout/           # Layout components
│   │           └── student/          # Student pages
│   └── test/                        # Test classes
├── build.gradle                     # Gradle build configuration
├── settings.gradle                  # Gradle settings
└── README.md                        # This file
```

## 🧪 Testing

Run the test suite using Gradle:

```bash
./gradlew test
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
